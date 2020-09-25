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
import count.jgame.models.Research;
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
	
	public AdministrableLocation getByGameIdAndPath(Long gameId, String path)
	{
		return repository.findByGameIdAndPath(gameId, path).orElse(null);
	}
	
	public AdministrableLocation preloadByGameIdAndPath(Long gameId, String path)
	{
		return repository.preloadByGameIdAndPath(gameId, path).orElse(null);
	}
	
	public AdministrableLocation preloadById(Long id)
	{
		return repository.preloadById(id).orElse(null);
	}
	
	protected void updateSlugAndPath(Long gameId, AdministrableLocation location, String name, Long parentId)
	{
		location.setSlug(slugService.get(name));
		
		String basePath = "";
		if (null != parentId) {
			AdministrableLocation parentLocation = repository.findOneByGameIdAndId(gameId, parentId).orElse(null);
			if (null == parentLocation) {
				throw new EntityNotFoundException("Parent location not found");
			}
			
			location.setParent(parentLocation);
			basePath = parentLocation.getPath() + ":";
		}
		
		location.setPath(basePath + location.getSlug());
	}
	
	public AdministrableLocation save(Game game, AdministrableLocationType type, AdministrableLocation input, Long parentId)
	{
		AdministrableLocation location = new AdministrableLocation(null, input.getName(), type, game);

		this.updateSlugAndPath(game.getId(), location, input.getName(), parentId);
		
		this.refresh(location, true);
		
		return repository.preloadById(location.getId()).orElse(null);
	}
	
	public AdministrableLocation save(Game game, AdministrableLocationType type, AdministrableLocation input)
	{
		return this.save(game, type, input, null);
	}
	
	public AdministrableLocation update(Long gameId, String path, AdministrableLocation input)
	{
		AdministrableLocation location = repository.findByGameIdAndPath(gameId, path).orElse(null);
		if (null == location) {
			throw new EntityNotFoundException("Location not found");
		}
		
		String oldPath = location.getPath();
		
		this.updateSlugAndPath(
			gameId,
			location, 
			input.getName(), 
			null == location.getParent() ? null : location.getParent().getId()
		);
		
		repository.updatePath(gameId, oldPath, location.getPath());
		
		this.refresh(location, true);
		
		return repository.preloadById(location.getId()).orElse(null);
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
		
		log.debug("level: {}, constructionType: {}, administrableLocationType: {}", level, type, type.getAdministrableLocationType());
		
		if (level == 1 && null != type.getAdministrableLocationType()) {
			this.save(
				location.getGame(), 
				type.getAdministrableLocationType(), 
				new AdministrableLocation(type.getName()),
				location.getId()
			);
		}

		log.info("built construction {} at level {}", type.getName(), level);
		
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

	public void produceResearch(AdministrableLocation location, Research type, Integer level)
	{
		if (!location.getResearches().containsKey(type)) {
			level = 1;
		}
		
		location.getResearches().put(type, level);

		log.info("researched {} at level {}", type.getName(), level);
		
		repository.saveAndFlush(location);
	}

	public boolean fulfillsConstructionTypeDependencies(Long id, Long constructionTypeId) {
		return repository.fulfillsConstructionTypeDependencies(id, constructionTypeId);
	}
	
	public boolean fulfillsShipTypeDependencies(Long id, Long shipTypeId) {
		return repository.fulfillsShipTypeDependencies(id, shipTypeId);
	}
	
	public boolean fulfillsResearchDependencies(Long id, Long researchId) {
		return repository.fulfillsResearchDependencies(id, researchId);
	}
	
	// @todo
	// public void updateResources(AdministrableLocation location) {}
}
