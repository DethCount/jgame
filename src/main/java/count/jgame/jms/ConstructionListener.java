package count.jgame.jms;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityNotFoundException;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.ConstructionType;
import count.jgame.models.Game;
import count.jgame.models.ProductionRequestStatus;
import count.jgame.repositories.ConstructionRequestObserverRepository;
import count.jgame.repositories.GameRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ConstructionListener {
	final String DESTINATION;
	final String FAILED_DESTINATION;
	final Boolean RETRY_FAILED;
	
	final Integer MIN_DELAY; // ms
	final Integer RETRY_DELAY; // ms
	final Integer FAILED_DELAY; // ms
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	ConstructionRequestObserverRepository observerRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	ConstructionListener(
		@Value("${jgame.jms.constructions.destination:Constructions}")
		String destination,
		@Value("${jgame.jms.constructions.failedDestination:Constructions_FAILED}")
		String failedDestination,
		@Value("${jgame.jms.constructions.retryFailed:false}")
		Boolean retryFailed,
		@Value("${jgame.jms.constructions.minDelay:100}")
		Integer minDelay,
		@Value("${jgame.jms.constructions.retryDelay:1000}")
		Integer retryDelay,
		@Value("${jgame.jms.constructions.failedDelay:2000}")
		Integer failedDelay
	) {
		DESTINATION = destination;
		FAILED_DESTINATION = failedDestination;
		RETRY_FAILED = retryFailed;
		MIN_DELAY = minDelay;
		RETRY_DELAY = retryDelay;
		FAILED_DELAY = failedDelay;
		
		log.info(
			"ConstructionListener starting with config: destination: {}"
				+ ", failedDestination: {}, retryFailed: {}, minDelay: {}"
				+ ", retryDelay: {}, failedDelay: {}",
			DESTINATION,
			FAILED_DELAY,
			RETRY_FAILED,
			MIN_DELAY,
			RETRY_DELAY,
			FAILED_DELAY
		);
	}
	
	@JmsListener(destination = "${game.jms.constructions.destination:Constructions}")
	void handleRequest(ConstructionRequestObserver observer)
	{
		log.info("ConstructionListener called: {}", observer.toString());
		try {
			// retrieve and check game
			Game game = gameRepository.preloadGame(observer.getGame().getId()).orElse(null);
			if (null == game) {
				throw new EntityNotFoundException("game not found");
			}
			
			// make sure observer is in db
			if (null == observer.getId()) {
				observerRepository.saveAndFlush(observer);
			}

			// wait for existing constructions for this game
			
			List<ConstructionRequestObserver> currentProduction = observerRepository
				.findBlockingObservers(
					new ProductionRequestStatus[] {
						ProductionRequestStatus.Running, 
						ProductionRequestStatus.Waiting
					}, 
					game,
					observer.getId(),
					observer.getRequest().getLevel()
				);
			
			log.debug("prod en cours: {}, observer: {}", currentProduction.size(), observer.getId());
			
			if (currentProduction.size() > 0) {
				log.info("waiting: {}", observer.getId());
				observer.waiting();
				retry(observer, (int)(observer.getUnitLeadTime() * 1000));
				return;
			}
			
			// check requested construction level
			
			log.debug(
				"{} VS {}", 
				observer.getRequest().toString(),
				game.getConstructions().getOrDefault(observer.getRequest().getType(), 0) + 1
			);
			
			if (observer.getRequest().getLevel() 
				!= game.getConstructions().getOrDefault(observer.getRequest().getType(), 0) + 1
			) {
				log.info("cancel: level not next for {}", observer.getId());
				observer.cancel();
				observerRepository.saveAndFlush(observer);
				return;
			}
			
			// if not started, start and retry after construction
			
			if (null == observer.getStartedAt()) {
				observer.start();
				log.info("start and sleep: {}", observer.getId());
				retry(observer, (int)(observer.getUnitLeadTime() * 1000));
				return;
			}
			
			// check if construction should have been done
			
			Date now = new Date();
			
			Integer shouldHaveDone = Math.min(
				1, 
				(int)(
					((now.getTime() - observer.getStartedAt().getTime()) / 1000.0)
						/ observer.getUnitLeadTime()
				)
			);
			
			log.debug("shouldHaveDone: {}", shouldHaveDone);
			
			if (shouldHaveDone > 0) {
				// update game with new construction
				this.produce(game, observer, shouldHaveDone);
			} else {
				// retry in 1/10th of lead time
				retry(observer, (int)(observer.getUnitLeadTime() * 100));
			}
		} catch(Exception e) {
			log.error("error: {}: {}", e.getClass().getSimpleName(), e.getMessage());
			e.printStackTrace(System.err);
			observer.fail();
			retryFailed(observer);
		} finally {
			log.debug("ConstructionListener end: {}", observer.toString());
		}
	}
	
	void retry(ConstructionRequestObserver observer, String destination, Integer delay) 
		throws JmsException 
	{
		delay = Math.max(MIN_DELAY, delay);
		log.debug("retry {} to {} in {}", observer.getId(), destination, delay);
		observerRepository.saveAndFlush(observer);
		
		final long finalDelay = delay;
		jmsTemplate.convertAndSend(destination, observer, new MessagePostProcessor() {
			
			@Override
			public Message postProcessMessage(Message message) throws JMSException {
				message.setLongProperty(
					ScheduledMessage.AMQ_SCHEDULED_DELAY,
					finalDelay
				);
				
			    return message;
			}
		});
	}
	
	void retry(ConstructionRequestObserver observer, Integer delay)
	{
		this.retry(observer, DESTINATION, delay);
	}
	
	void retry(ConstructionRequestObserver observer)
	{
		this.retry(observer, DESTINATION, RETRY_DELAY);
	}

	void retryFailed(ConstructionRequestObserver observer)
	{
		if (!this.RETRY_FAILED) {
			return;
		}
		
		this.retry(observer, FAILED_DESTINATION, FAILED_DELAY);
	}
	
	void produce(Game game, ConstructionRequestObserver observer, Integer nbProduced)
	{
		ConstructionType type = observer.getRequest().getType();
		Integer level = observer.getRequest().getLevel();
		
		if (!game.getConstructions().containsKey(type)) {
			level = 1;
		}

		log.info("building construction {} at level {}", type.getName(), level);
		
		game.getConstructions().put(type, level);
		observer.finish();
		
		gameRepository.saveAndFlush(game);
		observerRepository.saveAndFlush(observer);
	}
}
