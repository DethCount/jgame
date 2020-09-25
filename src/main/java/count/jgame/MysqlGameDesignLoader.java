package count.jgame;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;

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
		repository.save(new ConstructionType(6l, "CrystalWarehouse"));
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
		repository.save(new ConstructionType(21l, "MissileLanchers"));
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
		Double value = 10.0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel metalMineLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, 1l)
			);
			
			metalMineLvl.getProductions().put(mainEntityManager.getReference(ResourceType.class, 1l), value);
			
			repository.save(metalMineLvl);
			
			value += Math.sqrt(lvl);
		}
		
		repository.flush();
		
		value = 5.0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel siliciumMineLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, 2l)
			);
			
			siliciumMineLvl.getProductions().put(mainEntityManager.getReference(ResourceType.class, 2l), value);
			
			repository.save(siliciumMineLvl);
			
			value += Math.sqrt(lvl);
		}
		
		repository.flush();
		
		value = 1.0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel deuteriumMineLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, 3l)
			);
			
			deuteriumMineLvl.getProductions().put(mainEntityManager.getReference(ResourceType.class, 3l), value);
			
			repository.save(deuteriumMineLvl);
			
			value += lvl;
		}
		
		repository.flush();
		
		value = 1000.0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel metalWarehouseLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, 4l)
			);
			
			metalWarehouseLvl.getStorage().put(mainEntityManager.getReference(ResourceType.class, 1l), value.intValue());
			
			repository.save(metalWarehouseLvl);
			
			value *= 1.25;
		}
		
		repository.flush();
		
		value = 500.0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel siliciumWarehouseLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, 5l)
			);
			
			siliciumWarehouseLvl.getStorage().put(mainEntityManager.getReference(ResourceType.class, 2l), value.intValue());
			
			repository.save(siliciumWarehouseLvl);
			
			value *= 1.7;
		}
		
		repository.flush();
		

		value = 200.0;
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel deuteriumWarehouseLvl = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, 6l)
			);
			
			deuteriumWarehouseLvl.getStorage().put(mainEntityManager.getReference(ResourceType.class, 3l), value.intValue());
			
			repository.save(deuteriumWarehouseLvl);
			
			value *= 1.5;
		}
		
		repository.flush();
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
		AdministrableLocationType planet = new AdministrableLocationType(1l, "Planet");
		planet.setCanConstructBuildings(true);
		planet.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 1l));
		planet.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 11l));
		planet.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 19l));
		planet.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 25l));
		planet.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 38l));
		
		repository.save(planet);
		
		
		AdministrableLocationType shipyard = new AdministrableLocationType(2l, "Shipyard");
		shipyard.setCanBuildShips(true);

		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 1l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 2l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 3l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 4l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 5l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 6l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 7l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 8l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 9l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 10l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 11l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 12l));
		shipyard.getShips().add(mainEntityManager.getReference(ShipType.class, 13l));
		
		repository.save(shipyard);
		
		AdministrableLocationType laboratory = new AdministrableLocationType(3l, "Laboratory");
		laboratory.setCanDoResearch(true);
		repository.save(laboratory);
		
		AdministrableLocationType spaceStation = new AdministrableLocationType(4l, "SpaceStation");
		spaceStation.setCanDoResearch(true);
		spaceStation.setCanConstructBuildings(true);
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 39l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 40l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 41l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 42l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 43l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 44l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 45l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 46l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 47l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 48l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 49l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 50l));
		spaceStation.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 51l));
		spaceStation.getResearches().add(mainEntityManager.getReference(Research.class, 12l));
		repository.save(spaceStation);
		
		repository.flush();
	}
	
	public void initConstructionTypeAdministrableLocationType(ConstructionTypeRepository repository)
	{
		repository.updateAdministrableLocationType(10l, 2l);
		repository.updateAdministrableLocationType(11l, 3l);
		repository.updateAdministrableLocationType(38l, 4l);
		
		repository.flush();
	}
}
