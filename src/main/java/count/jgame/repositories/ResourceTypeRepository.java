package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ResourceType;

public interface ResourceTypeRepository extends JpaRepository<ResourceType, Long>
{
	Optional<ResourceType> findOneByName(String name);
}
