package count.jgame.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.Game;
import count.jgame.models.ProductionRequestStatus;

public interface ConstructionRequestObserverRepository 
extends JpaRepository<ConstructionRequestObserver, Long>
{
	List<ConstructionRequestObserver> 
	findByStatusAndGameAndIdNot(ProductionRequestStatus status, Game game, Long id);
}
