package count.jgame.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import count.jgame.exceptions.UnknownProductionRequestException;
import count.jgame.models.ConstructionRequest;
import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.ConstructionType;
import count.jgame.models.Game;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GameService {
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
	
	GameService(
			@Value("${jgame.jms.shipyard.destination:Shipyard}") String shipyardQueueName,
			@Value("${jgame.jms.constructions.destination:Shipyard}") String constructionsQueueName
	) {
		SHIPYARD_QUEUE_NAME = shipyardQueueName;
		CONSTRUCTIONS_QUEUE_NAME = constructionsQueueName;
	}
	
	public Game get(Long id) {
		return repository.preloadGame(id).orElse(null);
	}
	
	public Game save(Game input) {
		Game game = new Game();
		game.setPlayer(input.getPlayer());
		
		for (ConstructionType type : constructionTypeRepository.findAll()) {
			game.getConstructions().put(type, 0);
		}
		
		for (ShipType type : shipTypeRepository.findAll()) {
			game.getShips().put(type, 0);
		}
		
		// @todo add default administrable location
		
		return repository.save(game);
	}

	public Game update(Long id, Game input)
	{
		Game game = repository.findById(id).get();
		
		if (game == null) {
			throw new EntityNotFoundException(String.format("game not found : %d", id));
		}
		
		game.setPlayer(input.getPlayer());
		
		this.refresh(game, true);
		
		return repository.preloadGame(id).get();
	}
	
	public Game refresh(Game game, Boolean flush)
	{	
		// @todo calculate resource productions
		
		// @todo move constructions to administrable locations
		
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
		
		return flush != false 
			? repository.saveAndFlush(game) 
			: repository.save(game);
	}
	
	public void pushToProd(Game game, ProductionRequest productionRequest)
	{
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
		
		log.info("pushing production request of type {} to queue {}", observer.getClass().getSimpleName(), queueName);
		jmsTemplate.convertAndSend(queueName, observer);
	}
	
	// public void updateResources(AdministrableLocation location) {}
}
