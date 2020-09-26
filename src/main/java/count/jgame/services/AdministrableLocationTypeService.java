package count.jgame.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import count.jgame.models.AdministrableLocation;
import count.jgame.models.AdministrableLocationType;
import count.jgame.models.ConstructionType;
import count.jgame.models.Research;
import count.jgame.models.ShipType;
import count.jgame.repositories.AdministrableLocationTypeRepository;

@Service
public class AdministrableLocationTypeService
{
	@Autowired
	private AdministrableLocationTypeRepository repository;
	
	public AdministrableLocationType get(Long id)
	{
		return repository.findById(id).get();
	}

	public boolean canConstructBuildingType(Long id, Long constructionTypeId) {
		return repository.canConstructBuildingType(id, constructionTypeId);
	}
	
	public boolean canBuildShipType(Long id, Long shipTypeId) {
		return repository.canBuildShipType(id, shipTypeId);
	}
	
	public boolean canDoResearch(Long id, Long researchId) {
		return repository.canDoResearch(id, researchId);
	}

	@Transactional
	public void addTypeDefaults(AdministrableLocation location)
	{
		AdministrableLocationType type = repository.preloadById(location.getType().getId());
		
		for (ConstructionType ct : type.getConstructions()) {
			location.getConstructions().put(ct, 0);
		}
		
		for (ShipType st : type.getShips()) {
			location.getShips().put(st, 0);
		}
		
		for (Research rt : type.getResearches()) {
			location.getResearches().put(rt, 0);
		}
	}
	
	public Integer check(Long gameId)
	{
		System.err.println("check: "+repository.check(gameId));
		System.err.println("check2: "+repository.check2(gameId));
		
		return 11111;
	}
}
