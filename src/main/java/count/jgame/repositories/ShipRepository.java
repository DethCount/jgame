package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.Ship;

public interface ShipRepository extends JpaRepository<Ship, Long> {
}
