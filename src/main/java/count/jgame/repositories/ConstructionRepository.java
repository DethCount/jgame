package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.Construction;

public interface ConstructionRepository extends JpaRepository<Construction, Long> {
}
