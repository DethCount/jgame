package count.jgame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import count.jgame.models.ConstructionType;
import count.jgame.repositories.ConstructionTypeRepository;

@Service
public class ConstructionTypeService {
	@Autowired
	private ConstructionTypeRepository repository;
	
	public List<ConstructionType> getAll() {
		return repository.findAll();
	}
}
