package count.jgame.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
	Optional<AdministrableLocation> preloadById(Long id);

	@Query(
		"select al from AdministrableLocation al"
		+ " left join fetch al.type alt"
		+ " left join fetch al.constructions c"
		+ " left join fetch al.ships s"
		+ " where al.game.id = :gameId"
			+ " and al.path = :path"
	)
	Optional<AdministrableLocation> preloadByGameIdAndPath(Long gameId, String path);
	
	Optional<AdministrableLocation> findByGameIdAndPath(Long gameId, String path);
	
	Optional<AdministrableLocation> findOneByGameIdAndId(Long gameId, Long id);
	
	@Query(
		"update AdministrableLocation al"
		+ " set path = FUNCTION('REPLACE', al.path, :oldPath, :newPath)"
		+ " where game.id = :gameId"
		+ " and path like :oldPath%"
	)
	void updatePath(Long gameId, String oldPath, String newPath);

	@Query(
		nativeQuery = true,
		value = "select case when count(*) <= 0 then 'true' else 'false' end"
			+ " from construction_type_deps_research_level ctdrl1"
			+ " where ctdrl1.id_construction_type = :constructionTypeId"
				+ " and 1 NOT IN (" 
					+ "select 1"
					+ " from construction_type_deps_research_level ctdrl"
					+ " left join administrable_location_researches alr"
						+ " on alr.id_research = ctdrl.id_research"
					+ " where alr.id_administrable_location = :id"
					+ " and ctdrl.id_construction_type = :constructionTypeId"
					+ " and ctdrl.level <= alr.level"
				+ ")"
	)
	boolean fulfillsConstructionTypeDependencies(Long id, Long constructionTypeId);
	
	@Query(
		nativeQuery = true,
		value = "select case when count(*) <= 0 then 'true' else 'false' end"
			+ " from ship_type_deps_research_level stdrl1"
			+ " where stdrl1.id_ship_type = :shipTypeId"
				+ " and 1 NOT IN (" 
					+ "select 1"
					+ " from ship_type_deps_research_level stdrl"
					+ " left join administrable_location_researches alr"
						+ " on alr.id_research = stdrl.id_research"
					+ " where alr.id_administrable_location = :id"
					+ " and stdrl.id_ship_type = :shipTypeId"
					+ " and stdrl.level <= alr.level"
				+ ")"
	)
	boolean fulfillsShipTypeDependencies(Long id, Long shipTypeId);

	@Query(
		nativeQuery = true,
		value = "select case when count(*) <= 0 then 'true' else 'false' end from(("
			+ " select 1"
			+ " from research_deps_research_level rdrl1"
			+ " where rdrl1.id_parent = :researchId"
				+ " and 1 NOT IN (" 
					+ "select 1"
					+ " from research_deps_research_level rdrl"
					+ " left join administrable_location_researches alr"
						+ " on alr.id_research = rdrl.id_child"
					+ " where alr.id_administrable_location = :id"
					+ " and rdrl.id_parent = :researchId"
					+ " and rdrl.level <= alr.level"
				+ ")"
			+ " ) union ("
				+ " select 2"
				+ " from research_deps_construction_type_level rdctl1"
				+ " where rdctl1.id_parent = :researchId"
					+ " and 1 NOT IN (" 
						+ "select 1"
						+ " from research_deps_construction_type_level rdctl"
						+ " left join administrable_location_constructions alc"
							+ " on alc.id_construction_type = rdctl.id_construction_type"
						+ " where alc.id_administrable_location = :id"
						+ " and rdctl.id_research = :researchId"
						+ " and rdctl.level <= alc.level"
					+ ")"
			+ " )) foo"
	)
	boolean fulfillsResearchDependencies(Long id, Long researchId);
}
