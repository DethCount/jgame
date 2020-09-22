package count.jgame.services;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import count.jgame.exceptions.UnknownProductionRequestException;
import count.jgame.models.AdministrableLocation;
import count.jgame.models.AdministrableLocationType;
import count.jgame.models.ConstructionRequest;
import count.jgame.models.ConstructionRequestObserver;
import count.jgame.models.ConstructionType;
import count.jgame.models.Game;
import count.jgame.models.ProductionRequest;
import count.jgame.models.ProductionRequestObserver;
import count.jgame.models.ShipRequest;
import count.jgame.models.ShipRequestObserver;
import count.jgame.models.ShipType;
import count.jgame.repositories.AdministrableLocationRepository;
import count.jgame.repositories.ConstructionRequestObserverRepository;
import count.jgame.repositories.ShipRequestObserverRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdministrableLocationService
{
	@Autowired
	private ConstructionTypeService constructionTypeService;

	@Autowired
	private ShipTypeService shipTypeService;
	
	@Autowired
	private SlugService slugService;
	
	@Autowired
	private ConstructionRequestObserverRepository constructionRequestObserverRepository;
	
	@Autowired
	private ShipRequestObserverRepository shipRequestObserverRepository;
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	final String SHIPYARD_QUEUE_NAME;
	final String CONSTRUCTIONS_QUEUE_NAME;
	
	AdministrableLocationService(
			@Value("${jgame.jms.shipyard.destination:Shipyard}") String shipyardQueueName,
			@Value("${jgame.jms.constructions.destination:Constructions}") String constructionsQueueName
	) {
		SHIPYARD_QUEUE_NAME = shipyardQueueName;
		CONSTRUCTIONS_QUEUE_NAME = constructionsQueueName;
	}
	
	@Autowired
	private AdministrableLocationRepository repository;
	
	public AdministrableLocation getByGameIdAndSlug(Long gameId, String slug)
	{
		return repository.findByGameIdAndSlug(gameId, slug).get();
	}
	
	public AdministrableLocation preloadByGameIdAndSlug(Long gameId, String slug)
	{
		return repository.preloadByGameIdAndSlug(gameId, slug).get();
	}
	
	public AdministrableLocation preloadById(Long id)
	{
		return repository.preloadById(id).get();
	}
	
	public AdministrableLocation save(Game game, AdministrableLocationType type, AdministrableLocation input)
	{
		AdministrableLocation location = new AdministrableLocation(null, input.getName(), type, game);
		location.setSlug(slugService.get(input.getName()));
		
		this.refresh(location, true);
		
		return repository.preloadById(location.getId()).get();
	}
	
	public AdministrableLocation update(Long gameId, String slug, AdministrableLocation input)
	{
		AdministrableLocation location = repository.findByGameIdAndSlug(gameId, slug).get();
		if (null == location) {
			throw new EntityNotFoundException("Location not found");
		}
		
		location.setName(input.getName());
		location.setSlug(slugService.get(input.getName()));
		
		this.refresh(location, true);
		
		return repository.preloadById(location.getId()).get();
	}
	
	public AdministrableLocation refresh(AdministrableLocation location, Boolean flush)
	{	
		// @todo calculate resource productions
		
		// @todo get ship types by administrable location type
		for (ShipType t : shipTypeService.getAll()) {
			if (!location.getShips().containsKey(t)) {
				location.getShips().put(t, 0);
			}
		}

		// @todo get construction types by administrable location type
		for (ConstructionType t : constructionTypeService.getAll()) {
			if (!location.getConstructions().containsKey(t)) {
				location.getConstructions().put(t, 0);
			}
		}
		
		// @todo calculate stats
		
		return flush != false 
			? repository.saveAndFlush(location) 
			: repository.save(location);
	}

	public void pushToProd(AdministrableLocation administrableLocation, ProductionRequest productionRequest)
	{
		ProductionRequestObserver observer = null;
		
		productionRequest.setAdministrableLocation(administrableLocation);
		
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
	
	public void produceConstruction(AdministrableLocation location, ConstructionType type, Integer level)
	{
		if (!location.getConstructions().containsKey(type)) {
			level = 1;
		}

		log.info("building construction {} at level {}", type.getName(), level);
		
		location.getConstructions().put(type, level);
		
		repository.saveAndFlush(location);
	}
	
	public void produceShip(AdministrableLocation location, ShipType type, Integer nb)
	{
		if (location.getShips().containsKey(type)) {
			nb += location.getShips().get(type);
		}

		log.info("now owns {} {} ship(s)", nb, type.getName());
		
		location.getShips().put(type, nb);
		
		repository.saveAndFlush(location);
	}
	
	// @todo
	// public void updateResources(AdministrableLocation location) {}
}
