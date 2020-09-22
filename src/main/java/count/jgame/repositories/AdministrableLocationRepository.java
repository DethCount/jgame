package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.AdministrableLocation;

public interface AdministrableLocationRepository extends JpaRepository<AdministrableLocation, Long>
{
}
