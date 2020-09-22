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

import count.jgame.models.Game;
import count.jgame.models.ProductionRequestStatus;
import count.jgame.models.ShipRequestObserver;
import count.jgame.models.ShipType;
import count.jgame.repositories.GameRepository;
import count.jgame.repositories.ShipRequestObserverRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShipyardListener {
	final String DESTINATION;
	final String FAILED_DESTINATION;
	final Boolean RETRY_FAILED;
	final Integer MIN_DELAY;
	final Integer RETRY_DELAY;
	final Integer FAILED_DELAY;
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	ShipRequestObserverRepository observerRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	ShipyardListener(
		@Value("${jgame.jms.shipyard.destination:Shipyard}")
		String destination,
		@Value("${jgame.jms.shipyard.failedDestination:Shipyard_FAILED}")
		String failedDestination,
		@Value("${jgame.jms.shipyard.retryFailed:false}")
		Boolean retryFailed,
		@Value("${jgame.jms.shipyard.minDelay:100}")
		Integer minDelay,
		@Value("${jgame.jms.shipyard.retryDelay:1000}")
		Integer retryDelay,
		@Value("${jgame.jms.shipyard.failedDelay:2000}")
		Integer failedDelay
	) {
		DESTINATION = destination;
		FAILED_DESTINATION = failedDestination;
		RETRY_FAILED = retryFailed;
		MIN_DELAY = minDelay;
		RETRY_DELAY = retryDelay;
		FAILED_DELAY = failedDelay;
	}
	
	@JmsListener(destination = "${jgame.jms.shipyard.destination:Shipyard}")
	void handleRequest(ShipRequestObserver observer) {
		log.info("ShipyardListener called: {}", observer.toString());
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
			
			List<ShipRequestObserver> currentProduction = observerRepository
				.findBlockingObservers(
					new ProductionRequestStatus[] {
						ProductionRequestStatus.Running, 
						ProductionRequestStatus.Waiting
					}, 
					game,
					observer.getId()
				);
			
			log.debug("prod en cours: {}, observer: {}", currentProduction.size(), observer.getId());
			
			if (currentProduction.size() > 0) {
				log.info("waiting: {}", observer.getId());
				observer.waiting();
				retry(observer, (int)(observer.getUnitLeadTime() * 1000));
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
			
			Integer shouldHaveDone 
				= Math.min(
					observer.getRequest().getNb(),
					(int)(
						((now.getTime() - observer.getStartedAt().getTime()) / 1000.0)
						/ observer.getUnitLeadTime()
					)
				)
				- observer.getNbDone();
			
			log.debug("shouldHaveDone: {}", shouldHaveDone);
			
			if (shouldHaveDone > 0) {
				// update game with new construction
				this.produce(game, observer, shouldHaveDone);
				
				if (observer.getFinishedAt() == null) {
					retry(observer, (int)(observer.getUnitLeadTime() * 1000));
				}
			} else {
				// retry in 1/10th of lead time
				retry(observer, (int)(observer.getUnitLeadTime() * 100));
			}
		} catch(Exception e) {
			log.error("error: {}: {}", e.getClass().getSimpleName(), e.getMessage());
			observer.fail();
			retryFailed(observer);
		} finally {
			log.debug("ShipyardListener end: {}", observer.toString());
		}
	}
	
	void retry(ShipRequestObserver observer, String destination, Integer delay) 
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
	
	void retry(ShipRequestObserver observer, Integer delay)
	{
		this.retry(observer, DESTINATION, delay);
	}
	
	void retry(ShipRequestObserver observer)
	{
		this.retry(observer, DESTINATION, RETRY_DELAY);
	}

	void retryFailed(ShipRequestObserver observer)
	{
		if (!this.RETRY_FAILED) {
			return;
		}
		
		this.retry(observer, FAILED_DESTINATION, FAILED_DELAY);
	}
	
	void produce(Game game, ShipRequestObserver observer, Integer nbProduced)
	{
		ShipType type = observer.getRequest().getType();
		Integer nb = nbProduced;
		
		log.debug("before produce, hasStock: {}, type: {}, stock: {}, directAccess: {}", 
			game.getShips().containsKey(type), 
			type.toString(),
			game.getShips().toString(),
			game.getShips().get(type)
		);
		
		if (game.getShips().containsKey(type)) {
			nb += game.getShips().get(type);
		}

		log.info("now owns {} {} ship(s)", nb, type.getName());
		
		game.getShips().put(type, nb);
		observer.setNbDone(observer.getNbDone() + nbProduced);
		
		if (observer.getNbDone() >= observer.getRequest().getNb()) {
			observer.finish();
		}
		
		gameRepository.saveAndFlush(game);
		observerRepository.saveAndFlush(observer);
	}
}
