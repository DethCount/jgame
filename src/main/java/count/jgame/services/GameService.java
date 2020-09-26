package count.jgame.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import count.jgame.models.AdministrableLocation;
import count.jgame.models.AdministrableLocationType;
import count.jgame.models.Game;

import count.jgame.repositories.GameRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameService {
	@Autowired
	private GameRepository repository;
	
	@Autowired
	private AdministrableLocationTypeService administrableLocationTypeService;

	@Autowired
	private AdministrableLocationService administrableLocationService;
	
	@Autowired
	private EntityManager mainEntityManager;
	
	final String DEFAULT_ADMINISTRABLE_LOCATION_NAME;
	final Long DEFAULT_ADMINISTRABLE_LOCATION_TYPE_ID;
	
	GameService(
			@Value("${jgame.game.defaultAdministrableLocation.name}") String defaultAdministrableLocationName,
			@Value("${jgame.game.defaultAdministrableLocation.typeId}") Long defaultAdministrableLocationTypeId
	) {
		DEFAULT_ADMINISTRABLE_LOCATION_NAME = defaultAdministrableLocationName;
		DEFAULT_ADMINISTRABLE_LOCATION_TYPE_ID = defaultAdministrableLocationTypeId;
		
		log.info(
			"game service started with parameters {} {}", 
			DEFAULT_ADMINISTRABLE_LOCATION_NAME, 
			DEFAULT_ADMINISTRABLE_LOCATION_TYPE_ID
		);
	}
	
	public Game get(Long id) {
		return repository.preloadGame(id).orElse(null);
	}
	
	@Transactional
	public Game save(Game input) {
		Game game = new Game();
		game.setPlayer(input.getPlayer());
		repository.saveAndFlush(game);
		
		AdministrableLocationType defaultLocationType = administrableLocationTypeService
			.get(DEFAULT_ADMINISTRABLE_LOCATION_TYPE_ID);

		if (null == defaultLocationType) {
			throw new EntityNotFoundException("Default location type not found");
		}
		
		AdministrableLocation defaultLocation = administrableLocationService.save(
			game,
			defaultLocationType,
			new AdministrableLocation(DEFAULT_ADMINISTRABLE_LOCATION_NAME)
		);
		
		if (null == defaultLocation) {
			throw new EntityNotFoundException("Default location not found");
		}
		
		game.getAdministrableLocations().add(defaultLocation);
		
		return game;
	}

	public Game update(Long id, Game input)
	{
		Game game = repository.findById(id).get();
		
		if (game == null) {
			throw new EntityNotFoundException(String.format("game not found : %d", id));
		}
		
		game.setPlayer(input.getPlayer());
		
		repository.saveAndFlush(game);
		
		return repository.preloadGame(id).get();
	}
}
