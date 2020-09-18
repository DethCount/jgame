package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ShipRequestObserver;

public interface ShipRequestObserverRepository
extends JpaRepository<ShipRequestObserver, Long>
{
}
