package count.jgame.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import count.jgame.models.ConstructionType;

public interface ConstructionTypeRepository extends JpaRepository<ConstructionType, Long>
{
	Optional<ConstructionType> findOneByName(String name);
	
	@Transactional
	@Modifying
	@Query(
		"update ConstructionType ct"
		+ " set ct.administrableLocationType.id = :administrableLocationTypeId"
		+ " where id = :id"
	)
	void updateAdministrableLocationType(Long id, Long administrableLocationTypeId);
}
