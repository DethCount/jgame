package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import count.jgame.models.AdministrableLocation;

public interface AdministrableLocationRepository extends JpaRepository<AdministrableLocation, Long>
{
	@Query(
		"select al from AdministrableLocation al"
		+ " left join fetch al.type alt"
		+ " left join fetch al.constructions c"
		+ " left join fetch al.ships s"
		+ " where al.id = :id"
	)
	Optional<AdministrableLocation> preloadById(@Param("id") Long id);

	@Query(
		"select al from AdministrableLocation al"
		+ " left join fetch al.type alt"
		+ " left join fetch al.constructions c"
		+ " left join fetch al.ships s"
		+ " where al.game.id = :gameId"
			+ " and al.slug = :slug"
	)
	Optional<AdministrableLocation> preloadByGameIdAndSlug(
		@Param("gameId") Long gameId, 
		@Param("slug") String slug
	);
	
	Optional<AdministrableLocation> findByGameIdAndSlug(
		@Param("gameId") Long gameId, 
		@Param("slug") String slug
	);
}
