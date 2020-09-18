package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ProductionRequestObserver;

public interface ProductionRequestObserverRepository
	extends JpaRepository<ProductionRequestObserver, Long> 
{
}
