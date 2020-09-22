package count.jgame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.ProductionRequestStatus;
import count.jgame.repositories.ConstructionRequestObserverRepository;

@Service
public class ConstructionRequestObserverService
{
	@Autowired
	private ConstructionRequestObserverRepository repository;
	
	public ConstructionRequestObserver save(ConstructionRequestObserver observer)
	{
		return repository.saveAndFlush(observer);
	}
	
	public List<ConstructionRequestObserver> getBlocking(Long administrableLocationId, Long observerId, Integer level)
	{
		return repository.findBlockingObservers(
			new ProductionRequestStatus[] {
				ProductionRequestStatus.Running,
				ProductionRequestStatus.Waiting
			}, 
			administrableLocationId, 
			observerId, 
			level
		);
	}
}
