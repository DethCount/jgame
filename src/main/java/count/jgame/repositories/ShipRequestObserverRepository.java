package count.jgame.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import count.jgame.models.Game;
import count.jgame.models.ProductionRequestStatus;
import count.jgame.models.ShipRequestObserver;

public interface ShipRequestObserverRepository
extends JpaRepository<ShipRequestObserver, Long>
{
	@Query("select o, r"
		+ " from ShipRequestObserver o"
		+ " inner join o.request r"
		+ " where o.game = :game"
			+ " and o.status IN (:status)"
			+ " and o.id <> :id"
		+ " order by o.id ASC"
	)
	List<ShipRequestObserver> 
	findBlockingObservers(
		ProductionRequestStatus[] status, 
		Game game, 
		Long id
	);
}
