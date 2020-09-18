package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import count.jgame.models.Game;

@Component
public interface GameRepository extends JpaRepository<Game, Long> {
}