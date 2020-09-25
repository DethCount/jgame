package count.jgame.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import count.jgame.models.AdministrableLocationType;

public interface AdministrableLocationTypeRepository extends JpaRepository<AdministrableLocationType, Long>
{
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
}
