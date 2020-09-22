package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import count.jgame.models.AdministrableLocationType;

public interface AdministrableLocationTypeRepository extends JpaRepository<AdministrableLocationType, Long> {

}
