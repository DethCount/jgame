package count.jgame.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.Game;
import count.jgame.models.ProductionRequestStatus;

public interface ConstructionRequestObserverRepository 
extends JpaRepository<ConstructionRequestObserver, Long>
{
	@Query("select o, r"
		+ " from ConstructionRequestObserver o"
		+ " inner join o.request r"
		+ " where o.game = :game"
			+ " and o.status IN (:status)"
			+ " and o.id <> :id"
			+ " and (r.level < :level or (r.level = :level and o.id < :id))"
		+ " order by o.id ASC"
	)
	List<ConstructionRequestObserver> 
	findBlockingObservers(
		ProductionRequestStatus[] status, 
		Game game, 
		Long id,
		Integer level
	);
}
