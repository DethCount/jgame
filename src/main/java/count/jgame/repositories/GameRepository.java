package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import count.jgame.models.Game;

@Component
public interface GameRepository extends JpaRepository<Game, Long> {
	
	@Query("select g from Game g left join fetch g.constructions c left join fetch g.ships where g.id = :id")
	Optional<Game> preloadGame(@Param("id") Long id);
}