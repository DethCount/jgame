package count.jgame.jms;

import java.util.Date;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import count.jgame.models.Game;
import count.jgame.models.ShipRequestObserver;
import count.jgame.repositories.GameRepository;
import count.jgame.repositories.ShipRequestObserverRepository;

@Component
public class ShipyardListener {
	static final String DESTINATION = "Shipyard";
	static final String FAILED_DESTINATION = "Shipyard_FAILED";
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	ShipRequestObserverRepository observerRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@JmsListener(destination = DESTINATION)
	void handleRequest(ShipRequestObserver observer) {
		System.out.println("ShipyardListener called" + observer.toString());
		try {
			Game game = observer.getGame();
			if (null == game) {
				throw new EntityNotFoundException("game not found");
			}
			
			if (null == observer.getStartedAt()) {
				observer.start();
				retry(DESTINATION, observer);
				return;
			}
			
			Date now = new Date();
			
			Integer shouldHaveDone = (int)(
				((now.getTime() - observer.getStartedAt().getTime()) / 1000.0)
					/ observer.getUnitLeadTime()
			);
			
			System.out.println("shouldHaveDone: " + shouldHaveDone);
			
			int produced = shouldHaveDone - observer.getNbDone();
			if (produced > 0 && produced + observer.getNbDone() <= observer.getRequest().getNb()) {
				System.out.println("produced: " + produced);
				if (!game.getShips().containsKey(observer.getRequest().getType())) {
					game.getShips().put(observer.getRequest().getType(), produced);
				} else {
					game.getShips().put(
						observer.getRequest().getType(), 
						game.getShips().get(observer.getRequest().getType())
							+ produced
					);
				}
				
				gameRepository.saveAndFlush(game);
				
				observer.setNbDone(observer.getNbDone() + produced);
			}
			
			finish(DESTINATION, observer);
		} catch(Exception e) {
			e.printStackTrace(System.err);
			observer.fail();
			observerRepository.saveAndFlush(observer);
			if (observer.getNbDone() < observer.getRequest().getNb()) {
				retry(FAILED_DESTINATION, observer);
			}
		} finally {
			System.out.println("ShipyardListener end: " + observer.toString());
		}
	}
	
	void retry(String destination, ShipRequestObserver observer) throws JmsException {
		System.out.println("retry to " + destination);
		try {
			Thread.sleep(1000);
		} catch(Exception e) {
			e.printStackTrace(System.out);
		}
		
		jmsTemplate.convertAndSend(destination, observer);
	}
	
	void finish(String destination, ShipRequestObserver observer) {
		if (observer.getNbDone() < observer.getRequest().getNb()) {
			observerRepository.saveAndFlush(observer);
			retry(destination, observer);
			return;
		}
		
		observer.finish();
		observerRepository.saveAndFlush(observer);
	}
}
