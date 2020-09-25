package count.jgame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import count.jgame.models.ProductionRequestStatus;
import count.jgame.models.ResearchRequestObserver;
import count.jgame.repositories.ResearchRequestObserverRepository;

@Service
public class ResearchRequestObserverService
{
	@Autowired
	ResearchRequestObserverRepository repository;
	
	public ResearchRequestObserver save(ResearchRequestObserver observer)
	{
		return repository.saveAndFlush(observer);
	}
	
	public List<ResearchRequestObserver> getBlocking(
		Long administrableLocationId,
		Long id,
		Integer level
	) {
		return repository.findBlockingObservers(
			new ProductionRequestStatus[] {
				ProductionRequestStatus.Running,
				ProductionRequestStatus.Waiting
			},
			administrableLocationId, 
			id, 
			level
		);
	}
}
