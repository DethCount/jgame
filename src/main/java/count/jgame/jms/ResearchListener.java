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

import count.jgame.exceptions.AbilityException;
import count.jgame.models.AdministrableLocation;
import count.jgame.models.ResearchRequestObserver;
import count.jgame.models.Research;
import count.jgame.services.AdministrableLocationService;
import count.jgame.services.ResearchRequestObserverService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ResearchListener
{
	final String DESTINATION;
	final String FAILED_DESTINATION;
	final Boolean RETRY_FAILED;
	
	final Integer MIN_DELAY; // ms
	final Integer RETRY_DELAY; // ms
	final Integer FAILED_DELAY; // ms
	
	@Autowired
	AdministrableLocationService administrableLocationService;
	
	@Autowired
	ResearchRequestObserverService observerService;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	ResearchListener(
		@Value("${jgame.jms.researches.destination:Researches}")
		String destination,
		@Value("${jgame.jms.researches.failedDestination:Researches_FAILED}")
		String failedDestination,
		@Value("${jgame.jms.researches.retryFailed:false}")
		Boolean retryFailed,
		@Value("${jgame.jms.researches.minDelay:100}")
		Integer minDelay,
		@Value("${jgame.jms.researches.retryDelay:1000}")
		Integer retryDelay,
		@Value("${jgame.jms.researches.failedDelay:2000}")
		Integer failedDelay
	) {
		DESTINATION = destination;
		FAILED_DESTINATION = failedDestination;
		RETRY_FAILED = retryFailed;
		MIN_DELAY = minDelay;
		RETRY_DELAY = retryDelay;
		FAILED_DELAY = failedDelay;
		
		log.info(
			"ResearchListener starting with config: destination: {}"
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
	
	@JmsListener(destination = "${game.jms.researches.destination:Researches}")
	void handleRequest(ResearchRequestObserver observer)
	{
		log.info("ResearchListener called: {}", observer.toString());
		try {
			// retrieve and check game
			AdministrableLocation location = administrableLocationService
				.preloadById(
					observer.getAdministrableLocation().getId()
				);
			
			if (null == location) {
				throw new EntityNotFoundException("Location not found");
			}
			
			if (null == location.getType()) {
				throw new EntityNotFoundException("Location type not found");
			}
			
			if (!location.getType().getCanDoResearch()) {
				throw new AbilityException(location.getType(), "do research");
			}
			
			// make sure observer is in db
			if (null == observer.getId()) {
				observerService.save(observer);
			}

			// wait for existing researches
			
			List<ResearchRequestObserver> currentProduction = observerService
				.getBlocking(
					location.getId(),
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
			
			// check requested level
			
			log.debug(
				"{} VS {}", 
				observer.getRequest().toString(),
				location.getResearches().getOrDefault(observer.getRequest().getType(), 0) + 1
			);
			
			if (observer.getRequest().getLevel() 
				!= location.getResearches().getOrDefault(observer.getRequest().getType(), 0) + 1
			) {
				log.info("cancel: level not next for {}", observer.getId());
				observer.cancel();
				observerService.save(observer);
				return;
			}
			
			// if not started, start and retry after research time
			
			if (null == observer.getStartedAt()) {
				observer.start();
				log.info("start and sleep: {}", observer.getId());
				retry(observer, (int)(observer.getUnitLeadTime() * 1000));
				return;
			}
			
			// check if research should have been done
			
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
				// update game with new research
				this.produce(location, observer, shouldHaveDone);
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
			log.debug("ResearchListener end: {}", observer.toString());
		}
	}
	
	void retry(ResearchRequestObserver observer, String destination, Integer delay) 
		throws JmsException 
	{
		delay = Math.max(MIN_DELAY, delay);
		log.debug("retry {} to {} in {}", observer.getId(), destination, delay);
		observerService.save(observer);
		
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
	
	void retry(ResearchRequestObserver observer, Integer delay)
	{
		this.retry(observer, DESTINATION, delay);
	}
	
	void retry(ResearchRequestObserver observer)
	{
		this.retry(observer, DESTINATION, RETRY_DELAY);
	}

	void retryFailed(ResearchRequestObserver observer)
	{
		if (!this.RETRY_FAILED) {
			return;
		}
		
		this.retry(observer, FAILED_DESTINATION, FAILED_DELAY);
	}
	
	void produce(AdministrableLocation location, ResearchRequestObserver observer, Integer nbProduced)
	{
		Research type = observer.getRequest().getType();
		Integer level = observer.getRequest().getLevel();
		
		administrableLocationService.produceResearch(location, type, level);

		observer.finish();
		observerService.save(observer);
	}
}

