package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ShipType;

public interface ShipTypeRepository extends JpaRepository<ShipType, Long>
{
	Optional<ShipType> findOneByName(String name);
}
