package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.Research;

public interface ResearchRepository extends JpaRepository<Research, Long>
{
	Optional<Research> findOneByName(String name);
}
