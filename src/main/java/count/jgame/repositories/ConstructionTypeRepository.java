package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ConstructionType;

public interface ConstructionTypeRepository extends JpaRepository<ConstructionType, Long>
{
	Optional<ConstructionType> findOneByName(String name);
}
