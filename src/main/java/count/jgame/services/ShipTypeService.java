package count.jgame.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import count.jgame.models.ShipType;
import count.jgame.repositories.ShipTypeRepository;

@Service
public class ShipTypeService
{
	@Autowired
	private ShipTypeRepository repository;
	
	public List<ShipType> getAll()
	{
		return repository.findAll();
	}
}
