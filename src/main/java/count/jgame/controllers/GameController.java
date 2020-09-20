package count.jgame.controllers;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import count.jgame.exceptions.UnknownProductionRequestException;
import count.jgame.models.ConstructionRequest;
import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.Game;
import count.jgame.models.ProductionList;
import count.jgame.models.ProductionRequest;
import count.jgame.models.ProductionRequestObserver;
import count.jgame.models.ShipRequest;
import count.jgame.models.ShipRequestObserver;
import count.jgame.repositories.GameRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class GameController {
	
	@Autowired
	private GameRepository repository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@GetMapping(path = "/game/{id}")
	@ResponseBody
	public Game get(@PathVariable(name = "id") Long id) {
		Game game = repository.findById(id).orElse(null);
		log.debug("Game: {}", game.toString());		
		
		return game;
	}
	
	@PostMapping(path = "/game", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Game post(@RequestBody Game input) {
		Game game = new Game();
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
		
		if (productionRequest instanceof ShipRequest) {
			Double unitLeadTime = 5.0 * (((ShipRequest) productionRequest).getType().ordinal() + 1);
			
			observer = new ShipRequestObserver(
				(ShipRequest) productionRequest,
				unitLeadTime
			);
		} else if (productionRequest instanceof ConstructionRequest) {
			Double unitLeadTime = 12.0 * (((ConstructionRequest) productionRequest).getType().ordinal() + 1);
			observer = new ConstructionRequestObserver(
				(ConstructionRequest) productionRequest,
				unitLeadTime
			);
		} else {
			throw new UnknownProductionRequestException(productionRequest.getClass().getSimpleName());
		}
		
		log.info("pushing production request of type {} to queue {}", observer.getClass().getSimpleName(), listName.toString());
		jmsTemplate.convertAndSend(listName.toString(), observer);
		
		return game;
	}
}
