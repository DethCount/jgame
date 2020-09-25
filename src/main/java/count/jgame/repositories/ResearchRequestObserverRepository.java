package count.jgame.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import count.jgame.models.ProductionRequestStatus;
import count.jgame.models.ResearchRequestObserver;

public interface ResearchRequestObserverRepository extends JpaRepository<ResearchRequestObserver, Long>
{

	@Query("select o, r"
		+ " from ResearchRequestObserver o"
		+ " inner join o.request r"
		+ " where o.administrableLocation.id = :administrableLocationId"
			+ " and o.status IN (:status)"
			+ " and o.id <> :id"
			+ " and (r.level < :level or (r.level = :level and o.id < :id))"
		+ " order by o.id ASC"
	)
	List<ResearchRequestObserver> 
	findBlockingObservers(
		ProductionRequestStatus[] status, 
		Long administrableLocationId, 
		Long id,
		Integer level
	);
}
