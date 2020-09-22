package count.jgame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import count.jgame.models.ProductionRequestStatus;
import count.jgame.models.ShipRequestObserver;
import count.jgame.repositories.ShipRequestObserverRepository;

@Service
public class ShipRequestObserverService
{
	@Autowired
	private ShipRequestObserverRepository repository;
	
	public ShipRequestObserver save(ShipRequestObserver observer)
	{
		return repository.saveAndFlush(observer);
	}
	
	public List<ShipRequestObserver> getBlocking(
		Long administrableLocationId,
		Long id
	) {
		return repository.findBlockingObservers(
			new ProductionRequestStatus[] {
				ProductionRequestStatus.Running,
				ProductionRequestStatus.Waiting
			}, 
			administrableLocationId, 
			id
		);
	}
}
