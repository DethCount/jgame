package count.jgame.controllers;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import count.jgame.exceptions.UnknownProductionRequestException;
import count.jgame.exceptions.UnknownProductionListException;
import count.jgame.models.ConstructionRequest;
import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.ConstructionType;
import count.jgame.models.Game;
import count.jgame.models.ProductionList;
import count.jgame.models.ProductionRequest;
import count.jgame.models.ProductionRequestObserver;
import count.jgame.models.ShipRequest;
import count.jgame.models.ShipRequestObserver;
import count.jgame.models.ShipType;
import count.jgame.repositories.ConstructionRequestObserverRepository;
import count.jgame.repositories.ConstructionTypeRepository;
import count.jgame.repositories.GameRepository;
import count.jgame.repositories.ShipRequestObserverRepository;
import count.jgame.repositories.ShipTypeRepository;
import count.jgame.services.GameService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private GameRepository repository;
	
	@Autowired
	private ConstructionTypeRepository constructionTypeRepository;
	
	@Autowired
	private ShipTypeRepository shipTypeRepository;
	
	@Autowired
	private ConstructionRequestObserverRepository constructionRequestObserverRepository;
	
	@Autowired
	private ShipRequestObserverRepository shipRequestObserverRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	final String SHIPYARD_QUEUE_NAME;
	final String CONSTRUCTIONS_QUEUE_NAME;
	
	GameController(
		@Value("${jgame.jms.shipyard.destination:Shipyard}") String shipyardQueueName,
		@Value("${jgame.jms.constructions.destination:Shipyard}") String constructionsQueueName
	) {
		SHIPYARD_QUEUE_NAME = shipyardQueueName;
		CONSTRUCTIONS_QUEUE_NAME = constructionsQueueName;
	}
	
	@GetMapping(path = "/game/{id}")
	@ResponseBody
	public Game get(@PathVariable(name = "id") Long id) {
		Game game = repository.findById(id).orElse(null);
		if (game == null) {
			throw new EntityNotFoundException(String.format("game not found : %d", id));
		}
		
		return game;
	}
	
	@PostMapping(path = "/game", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Game post(@RequestBody Game input)
	{
		Game game = new Game();
		game.setPlayer(input.getPlayer());
		
		for (ConstructionType type : constructionTypeRepository.findAll()) {
			game.getConstructions().put(type, 0);
		}
		
		for (ShipType type : shipTypeRepository.findAll()) {
			game.getShips().put(type, 0);
		}
		
		return repository.save(game);
	}
	
	@PutMapping(path = "/game/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Game put(
		@PathVariable("id") Long id,
		@RequestBody Game input
	) {
		Game game = repository.preloadGame(id).orElse(null);
		if (game == null) {
			throw new EntityNotFoundException(String.format("game not found : %d", id));
		}
		
		gameService.updateResources(game);
		
		for (ShipType t : shipTypeRepository.findAll()) {
			if (!game.getShips().containsKey(t)) {
				game.getShips().put(t, 0);
			}
		}

		for (ConstructionType t : constructionTypeRepository.findAll()) {
			if (!game.getConstructions().containsKey(t)) {
				game.getConstructions().put(t, 0);
			}
		}
		
		game.setPlayer(input.getPlayer());
		
		return repository.save(game);
	}
	
	@PostMapping(path = "/game/{id}/production/{listName}")
	@ResponseBody
	public Game pushToProd(
		@PathVariable("id") Long id,
		@PathVariable("listName") ProductionList listName,
		@RequestBody ProductionRequest productionRequest
	) {
		Game game = repository.findById(id).orElse(null);
		if (game == null) {
			throw new EntityNotFoundException(String.format("game not found : %d", id));
		}
		
		ProductionRequestObserver observer = null;
		
		productionRequest.setGame(game);
		
		String queueName = null;
		
		if (productionRequest instanceof ShipRequest) {
			Double unitLeadTime = 5.0;
			
			observer = new ShipRequestObserver(
				(ShipRequest) productionRequest,
				unitLeadTime
			);
			
			shipRequestObserverRepository.saveAndFlush((ShipRequestObserver) observer);
			
			queueName = this.SHIPYARD_QUEUE_NAME;
		} else if (productionRequest instanceof ConstructionRequest) {
			Double unitLeadTime = 10.0;
			observer = new ConstructionRequestObserver(
				(ConstructionRequest) productionRequest,
				unitLeadTime
			);
			
			constructionRequestObserverRepository.saveAndFlush((ConstructionRequestObserver) observer);
			
			queueName = this.CONSTRUCTIONS_QUEUE_NAME;
		} else {
			throw new UnknownProductionRequestException(productionRequest.getClass().getSimpleName());
		}
		
		if (queueName.length() <= 0) {
			throw new UnknownProductionListException(listName.name());
		}
		
		log.info("pushing production request of type {} to queue {}", observer.getClass().getSimpleName(), queueName);
		jmsTemplate.convertAndSend(queueName, observer);
		
		return game;
	}
}
