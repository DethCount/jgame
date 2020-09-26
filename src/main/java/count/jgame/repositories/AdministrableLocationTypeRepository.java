package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import count.jgame.models.AdministrableLocationType;

public interface AdministrableLocationTypeRepository extends JpaRepository<AdministrableLocationType, Long>
{
	@Query("select alt"
		+ " from AdministrableLocationType alt"
		+ " left join fetch alt.constructions altc"
		+ " left join fetch alt.ships alts"
		+ " left join fetch alt.researches altr"
		+ " where alt.id = :id"
		)
	AdministrableLocationType preloadById(Long id);
	
	@Query(
		"select case when count(*) > 0 then 'true' else 'false' end"
		+ " from AdministrableLocationType alt"
		+ " left join alt.constructions altc"
		+ " where alt.id = :id"
		+ " and alt.canConstructBuildings = true"
		+ " and altc.id = :constructionTypeId"
	)
	boolean canConstructBuildingType(Long id, Long constructionTypeId);
	
	@Query(
		"select case when count(*) > 0 then 'true' else 'false' end"
		+ " from AdministrableLocationType alt"
		+ " left join alt.ships alts"
		+ " where alt.id = :id"
		+ " and alt.canBuildShips = true"
		+ " and alts.id = :shipTypeId"
	)
	boolean canBuildShipType(Long id, Long shipTypeId);
	
	@Query(
		"select case when count(*) > 0 then 'true' else 'false' end"
		+ " from AdministrableLocationType alt"
		+ " left join alt.researches altr"
		+ " where alt.id = :id"
		+ " and alt.canDoResearch = true"
		+ " and altr.id = :researchId"
	)
	boolean canDoResearch(Long id, Long researchId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into administrable_location_type_resources values (:id, :resourceId)")
	void addResource(Long id, Long resourceId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into administrable_location_type_constructions values (:id, :constructionId)")
	void addConstruction(Long id, Long constructionId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into administrable_location_type_ships values (:id, :shipId)")
	void addShip(Long id, Long shipId);

	@Transactional
	@Modifying
	@Query(nativeQuery = true, value = "insert into administrable_location_type_researches values (:id, :researchId)")
	void addResearch(Long id, Long researchId);

	@Query(
		nativeQuery = true,
		value = "insert into administrable_location_constructions (id_administrable_location, id_construction_type, level)"
		+ " select :locationId, t.id_construction_type, :defaultValue"
		+ " from administrable_location_type_constructions t"
		+ " left join administrable_location al on al.id_administration_location_type = t.id_administrable_location_type"
		+ " where al.id = :locationId"
	)
	void addTypeDefaultConstructions(Long locationId, Integer defaultValue);
	
	@Query(
		nativeQuery = true,
		value = "insert into administrable_location_ships (id_administrable_location, id_ship_type, nb)"
		+ " select :locationId, t.id_ship_type, :defaultValue"
		+ " from administrable_location_type_ships t"
		+ " left join administrable_location al on al.id_administration_location_type = t.id_administrable_location_type"
		+ " where al.id = :locationId"
	)
	void addTypeDefaultShips(Long locationId, Integer defaultValue);
	
	@Query(
		nativeQuery = true,
		value = "insert into administrable_location_researches (id_administrable_location, id_research, level)"
		+ " select :locationId, t.id_research, :defaultValue"
		+ " from administrable_location_type_researches t"
		+ " left join administrable_location al on al.id_administration_location_type = t.id_administrable_location_type"
		+ " where al.id = :locationId"
	)
	void addTypeDefaultResearches(Long locationId, Integer defaultValue);

	@Query(nativeQuery = true, value = "select count(*) from administrable_location_constructions alc where alc.id_administrable_location = :gameId")
	Integer check(Long gameId);
	
	@Query(nativeQuery = true, value = "select count(*) from administrable_location_type_constructions t left join administrable_location al on al.id_administration_location_type = t.id_administrable_location_type where al.id = :alId")
	Integer check2(Long alId);
}
