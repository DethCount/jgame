package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import count.jgame.models.Game;

@Component
public interface GameRepository extends JpaRepository<Game, Long> {
	
	@Query("select g"
		+ " from Game g"
		+ " left join fetch g.administrableLocations al"
		+ " left join fetch al.type alt"
		+ " left join fetch al.constructions c"
		+ " left join fetch al.ships s"
		+ " left join fetch al.researches r"
		+ " left join fetch alt.constructions tc"
		+ " left join fetch alt.ships ts"
		+ " left join fetch alt.researches tr"
		+ " where g.id = :id")
	Optional<Game> preloadGame(@Param("id") Long id);
}