package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.ResearchRequest;

public interface ResearchRequestRepository extends JpaRepository<ResearchRequest, Long>
{
}
