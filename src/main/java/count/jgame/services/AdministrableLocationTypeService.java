package count.jgame.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import count.jgame.models.AdministrableLocationType;
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
}
