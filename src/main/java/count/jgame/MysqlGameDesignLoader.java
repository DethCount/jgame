package count.jgame;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import count.jgame.models.AdministrableLocationType;
import count.jgame.models.ConstructionType;
import count.jgame.models.ConstructionTypeLevel;
import count.jgame.models.Research;
import count.jgame.models.ResourceType;
import count.jgame.models.ShipType;
import count.jgame.repositories.AdministrableLocationTypeRepository;
import count.jgame.repositories.ConstructionTypeLevelRepository;
import count.jgame.repositories.ConstructionTypeRepository;
import count.jgame.repositories.ResearchRepository;
import count.jgame.repositories.ResourceTypeRepository;
import count.jgame.repositories.ShipTypeRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MysqlGameDesignLoader implements CommandLineRunner {
	
	@Autowired EntityManager mainEntityManager;
	
	Repositories repositories;
	
	@Autowired
	public MysqlGameDesignLoader(ApplicationContext context) {
		this.repositories = new Repositories(context);
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Mysql game design loader started...");
		
		ResourceTypeRepository resourceTypeRepository 
			= (ResourceTypeRepository) this.repositories.getRepositoryFor(ResourceType.class).get();
		
		if (resourceTypeRepository.findById(1l).isPresent()) {
			log.info("Mysql game design already loaded");
			return;
		}
		
		this.initResourceType(resourceTypeRepository);
		

		this.initConstructionType(
			(ConstructionTypeRepository)
			this.repositories.getRepositoryFor(ConstructionType.class).get()
		);
		
		this.initConstructionTypeLevel(
			(ConstructionTypeLevelRepository)
			this.repositories.getRepositoryFor(ConstructionTypeLevel.class).get()
		);
		
		this.initShipType(
			(ShipTypeRepository)
			this.repositories.getRepositoryFor(ShipType.class).get()
		);
		
		this.initResearch(
			(ResearchRepository)
			this.repositories.getRepositoryFor(Research.class).get()
		);
		
		this.initAdministrableLocationType(
			(AdministrableLocationTypeRepository)
			this.repositories.getRepositoryFor(AdministrableLocationType.class).get()
		);
		
		this.initConstructionTypeAdministrableLocationType(
			(ConstructionTypeRepository)
			this.repositories.getRepositoryFor(ConstructionType.class).get()
		);

		log.info("Mysql game design finished loading");
	}

	interface LevelGenerator {
		public Integer getValueFromLevel(Integer level, Integer prev);
	}
	
	interface LevelMapAccess<K,V> {
		public Map<K,V> getMap(ConstructionTypeLevel level);
	}
	
	protected void createLevel(
		ConstructionTypeLevelRepository repository,
		LevelMapAccess<ResourceType, Integer> propertyAccess,
		LevelGenerator lvlGenerator,
		Integer initValue,
		Long typeId,
		Long resourceTypeId
	) {

		Integer value = initValue;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel metalMineLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, typeId)
			);
			
			value = lvlGenerator.getValueFromLevel(lvl, value);
			
			propertyAccess.getMap(metalMineLvl)
				.put(mainEntityManager.getReference(ResourceType.class, resourceTypeId), value);
			
			repository.save(metalMineLvl);
		}
		
		repository.flush();
	}

	public void initResourceType(ResourceTypeRepository repository)
	{	
		repository.save(new ResourceType(1l, "Metal"));
		repository.save(new ResourceType(2l, "Silicium"));
		repository.save(new ResourceType(3l, "Deuterium"));
		repository.save(new ResourceType(3l, "Money"));
		repository.save(new ResourceType(3l, "Water"));
		repository.save(new ResourceType(3l, "Ice"));
		repository.save(new ResourceType(3l, "Nanite"));
		repository.save(new ResourceType(3l, "ElectronicCircuit"));
		repository.save(new ResourceType(3l, "Motor"));
		repository.save(new ResourceType(3l, "Wheel"));
		repository.save(new ResourceType(3l, "Wings"));
		repository.save(new ResourceType(3l, "Hull"));
		repository.save(new ResourceType(3l, "Shield"));
		repository.save(new ResourceType(3l, "Seeds"));
		repository.save(new ResourceType(3l, "SolarCell"));
		repository.save(new ResourceType(3l, "Battery"));
		repository.save(new ResourceType(3l, "Thruster"));
		repository.save(new ResourceType(3l, "Glassware"));
		repository.save(new ResourceType(3l, "SpyReport"));
		repository.save(new ResourceType(3l, "Concrete"));
		repository.save(new ResourceType(3l, "Laser"));
		
		repository.flush();
	}
	
	public void initConstructionType(ConstructionTypeRepository repository)
	{	
		repository.save(new ConstructionType(1l, "StategicComplex"));
		repository.save(new ConstructionType(2l, "MetalMine")); // nanites permettent de liquefier le metal pour transport, le metal se déplace tt seul jusqu'au dépôt
		repository.save(new ConstructionType(3l, "SiciliciumMine"));
		repository.save(new ConstructionType(4l, "DeuteriumMine"));
		repository.save(new ConstructionType(5l, "MetalWarehouse"));
		repository.save(new ConstructionType(6l, "SiliciumWarehouse"));
		repository.save(new ConstructionType(7l, "DeuteriumWarehouse"));
		repository.save(new ConstructionType(8l, "SolarPanels"));
		repository.save(new ConstructionType(9l, "PowerPlant"));
		repository.save(new ConstructionType(10l, "Factory"));
		
		repository.save(new ConstructionType(11l, "IndustrialCenter"));
		repository.save(new ConstructionType(12l, "Shipyard"));
		repository.save(new ConstructionType(13l, "Laboratory"));
		repository.save(new ConstructionType(14l, "DataCenter")); 
		repository.save(new ConstructionType(15l, "CommonSpaceProgram")); // techno achetees en commun entre joueurs, négo % implication
		repository.save(new ConstructionType(16l, "Marketplace")); // ressources strategiques
		repository.save(new ConstructionType(17l, "Bank"));
		repository.save(new ConstructionType(18l, "InformationAgency")); // permet d'échanger des rapports d'espionnage
		
		repository.save(new ConstructionType(19l, "MilitaryBase"));
		repository.save(new ConstructionType(20l, "Radar")); // permet d'etre alerté avant attaque 
		repository.save(new ConstructionType(21l, "MissileLaunchers"));
		repository.save(new ConstructionType(22l, "LaserTurets"));
		repository.save(new ConstructionType(23l, "DefensiveSatelliteArray"));
		repository.save(new ConstructionType(24l, "PlanetShield"));
		
		repository.save(new ConstructionType(25l, "Colony")); // lvl = nb hab max
		repository.save(new ConstructionType(26l, "Casino")); // mini games
		repository.save(new ConstructionType(27l, "NightClub"));
		repository.save(new ConstructionType(28l, "Supermarket"));
		repository.save(new ConstructionType(29l, "SwimmingPool"));
		repository.save(new ConstructionType(30l, "PaintBall"));
		repository.save(new ConstructionType(31l, "Hospital"));
		repository.save(new ConstructionType(32l, "Administration")); // stats economie du quartier
		repository.save(new ConstructionType(33l, "Embassy")); // diplomatie entre joueurs
		repository.save(new ConstructionType(34l, "Police"));
		repository.save(new ConstructionType(35l, "WaterPlant"));
		repository.save(new ConstructionType(36l, "RecyclingPlant"));
		repository.save(new ConstructionType(37l, "EmploymentAgency"));
		
		repository.save(new ConstructionType(38l, "SpaceStation"));
		repository.save(new ConstructionType(39l, "LabModule"));
		repository.save(new ConstructionType(40l, "SportsModule")); // la santé de la station spatiale joue sur les temps de recherche
		repository.save(new ConstructionType(41l, "DormitoryModule"));
		repository.save(new ConstructionType(42l, "BotanicModule"));
		repository.save(new ConstructionType(43l, "CornFieldModule"));
		repository.save(new ConstructionType(44l, "KitchenModule"));
		repository.save(new ConstructionType(45l, "TVModule"));
		repository.save(new ConstructionType(46l, "WeaponsModule"));
		repository.save(new ConstructionType(47l, "BoosterModule"));
		repository.save(new ConstructionType(48l, "DockerModule"));
		repository.save(new ConstructionType(49l, "StorageModule"));
		repository.save(new ConstructionType(50l, "ArchiveModule"));
		repository.save(new ConstructionType(51l, "ParkModule"));
		
		repository.flush();
	}
	
	public void initConstructionTypeLevel(ConstructionTypeLevelRepository repository)
	{
		LevelMapAccess<ResourceType,Integer> mineProd = (mine) -> mine.getProductions();
		LevelMapAccess<ResourceType,Integer> storage = (warehouse) -> warehouse.getStorage();
		
		this.createLevel(repository, mineProd, (lvl, prev) -> prev + (int) Math.round(Math.sqrt(lvl)), 10, 1l, 1l);
		this.createLevel(repository, mineProd, (lvl, prev) -> prev + (int) Math.round(Math.sqrt(lvl)), 5, 2l, 2l);
		this.createLevel(repository, mineProd, (lvl, prev) -> prev + lvl, 1, 3l, 3l);
		this.createLevel(repository, storage, (lvl, prev) -> (int) Math.round(prev * 1.25), 1000, 4l, 1l);
		this.createLevel(repository, storage, (lvl, prev) -> (int) Math.round(prev * 1.7), 500, 5l, 2l);
		this.createLevel(repository, storage, (lvl, prev) -> (int) Math.round(prev * 1.5), 200, 6l, 3l);
	}
	
	public void initShipType(ShipTypeRepository repository)
	{
		repository.save(new ShipType(1l, "SpaceShuttle"));
		repository.save(new ShipType(2l, "Probe"));
		repository.save(new ShipType(3l, "LightFighter"));
		repository.save(new ShipType(4l, "LightCargo"));
		repository.save(new ShipType(5l, "HeavyFighter"));
		repository.save(new ShipType(6l, "HeavyCargo"));
		repository.save(new ShipType(7l, "SpaceStation"));
		repository.save(new ShipType(8l, "Colonial"));
		repository.save(new ShipType(9l, "Cruiser"));
		repository.save(new ShipType(10l, "Battleship"));
		repository.save(new ShipType(11l, "Destroyer"));
		repository.save(new ShipType(12l, "DeathStar"));
		repository.save(new ShipType(13l, "Halo"));
		repository.save(new ShipType(13l, "PulsarWell"));
		
		repository.flush();
	}
	
	private void initResearch(ResearchRepository repository)
	{
		repository.save(new Research(1l, "RocketScience"));
		repository.save(new Research(2l, "Electronics"));
		repository.save(new Research(3l, "Datamining"));
		repository.save(new Research(4l, "Communications"));
		repository.save(new Research(5l, "Lasers"));
		repository.save(new Research(6l, "Shielding"));
		repository.save(new Research(7l, "Alienistry"));
		repository.save(new Research(8l, "Weapons"));
		repository.save(new Research(9l, "Aeronnautics"));
		repository.save(new Research(10l, "Shielding"));
		repository.save(new Research(11l, "Cars"));
		repository.save(new Research(12l, "Hydroponics"));
		repository.save(new Research(13l, "Espionage")); // EspionNage en FR
		
		repository.flush();
	}
	
	public void initAdministrableLocationType(AdministrableLocationTypeRepository repository)
	{	
		AdministrableLocationType type;
		
		type = new AdministrableLocationType(1l, "Planet");
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
		repository.addConstruction(type.getId(), 1l);
		repository.addConstruction(type.getId(), 11l);
		repository.addConstruction(type.getId(), 19l);
		repository.addConstruction(type.getId(), 25l);
		repository.addConstruction(type.getId(), 38l);
		
		type = new AdministrableLocationType(2l, "StategicComplex");
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
		repository.addConstruction(type.getId(), 2l);
		repository.addConstruction(type.getId(), 3l);
		repository.addConstruction(type.getId(), 4l);
		repository.addConstruction(type.getId(), 5l);
		repository.addConstruction(type.getId(), 6l);
		repository.addConstruction(type.getId(), 7l);
		repository.addConstruction(type.getId(), 8l);
		repository.addConstruction(type.getId(), 9l);
		repository.addConstruction(type.getId(), 10l);
		
		type = new AdministrableLocationType(3l, "Shipyard");
		type.setCanBuildShips(true);
		repository.saveAndFlush(type);
		repository.addShip(type.getId(), 1l);
		repository.addShip(type.getId(), 2l);
		repository.addShip(type.getId(), 3l);
		repository.addShip(type.getId(), 4l);
		repository.addShip(type.getId(), 5l);
		repository.addShip(type.getId(), 6l);
		repository.addShip(type.getId(), 7l);
		repository.addShip(type.getId(), 8l);
		repository.addShip(type.getId(), 9l);
		repository.addShip(type.getId(), 10l);
		repository.addShip(type.getId(), 11l);
		repository.addShip(type.getId(), 12l);
		repository.addShip(type.getId(), 13l);
		
		type = new AdministrableLocationType(4l, "Laboratory");
		type.setCanDoResearch(true);
		repository.saveAndFlush(type);
		
		type = new AdministrableLocationType(5l, "SpaceStation");
		type.setCanDoResearch(true);
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
		repository.addConstruction(type.getId(), 39l);
		repository.addConstruction(type.getId(), 40l);
		repository.addConstruction(type.getId(), 41l);
		repository.addConstruction(type.getId(), 42l);
		repository.addConstruction(type.getId(), 43l);
		repository.addConstruction(type.getId(), 44l);
		repository.addConstruction(type.getId(), 45l);
		repository.addConstruction(type.getId(), 46l);
		repository.addConstruction(type.getId(), 47l);
		repository.addConstruction(type.getId(), 48l);
		repository.addConstruction(type.getId(), 49l);
		repository.addConstruction(type.getId(), 50l);
		repository.addConstruction(type.getId(), 51l);
		repository.addResearch(type.getId(), 12l);
		
		repository.flush();
	}
	
	public void initConstructionTypeAdministrableLocationType(ConstructionTypeRepository repository)
	{
		repository.updateAdministrableLocationType(1l, 2l);
		repository.updateAdministrableLocationType(12l, 3l);
		repository.updateAdministrableLocationType(13l, 4l);
		repository.updateAdministrableLocationType(38l, 5l);
		
		repository.flush();
	}
}
