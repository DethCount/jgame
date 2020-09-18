package count.jgame.jms;

import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Component;

import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.Game;
import count.jgame.models.ProductionRequestStatus;
import count.jgame.repositories.ConstructionRequestObserverRepository;
import count.jgame.repositories.GameRepository;

@Component
public class ConstructionListener {
	static final String DESTINATION = "Constructions";
	static final String FAILED_DESTINATION = "Constructions_FAILED";
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	ConstructionRequestObserverRepository observerRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@JmsListener(destination = DESTINATION)
	void handleRequest(ConstructionRequestObserver observer) {
		System.out.println("ConstructionListener called" + observer.toString());
		try {
			Game game = observer.getGame();
			if (null == game) {
				throw new EntityNotFoundException("game not found");
			}
			
			observerRepository.saveAndFlush(observer);
			
			List<ConstructionRequestObserver> currentProduction = observerRepository
				.findByStatusAndGameAndIdNot(
					ProductionRequestStatus.Running, 
					game, 
					observer.getId()
				);
			System.out.println("prod en cours:" + currentProduction.toString());
			System.out.println("observer en cours" + observer.getId());
			if (currentProduction.size() > 1 
				&& currentProduction.get(0).getId() != observer.getId()
			){
				System.out.println("canceling: " + observer.getId());
				observer.cancel();
				observerRepository.saveAndFlush(observer);
				return;
			}
			
			if (null == observer.getStartedAt()) {
				observer.start();
				System.out.println("start save: " + observer.toString());
				observerRepository.saveAndFlush(observer);
				retry(DESTINATION, observer);
				return;
			}
			
			Date now = new Date();
			
			Integer shouldHaveDone = (int)(
				((now.getTime() - observer.getStartedAt().getTime()) / 1000.0)
					/ observer.getUnitLeadTime()
			);
			
			System.out.println("shouldHaveDone: " + shouldHaveDone);
			
			if (shouldHaveDone > 0) {
				if (!game.getConstructions().containsKey(observer.getRequest().getType())) {
					game.getConstructions().put(observer.getRequest().getType(), 1);
					System.out.println("game add:" + 1);
				} else {
					game.getConstructions().put(
						observer.getRequest().getType(), 
						observer.getRequest().getLevel()
					);
					System.out.println("game put: " + observer.getRequest().getLevel());
				}
				
				System.out.println("game bf flush:" + game.getConstructions().toString());
				gameRepository.saveAndFlush(game);
				observer.finish();
				observerRepository.saveAndFlush(observer);
			} else {
				observerRepository.saveAndFlush(observer);
				retry(DESTINATION, observer);
			}
		} catch(Exception e) {
			e.printStackTrace(System.err);
			observer.fail();
			observerRepository.saveAndFlush(observer);
			retry(FAILED_DESTINATION, observer);
		} finally {
			System.out.println("ConstructionListener end: " + observer.toString());
		}
	}
	
	void retry(String destination, ConstructionRequestObserver observer) throws JmsException {
		System.out.println("retry to " + destination);
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		jmsTemplate.convertAndSend(destination, observer);
	}
}
