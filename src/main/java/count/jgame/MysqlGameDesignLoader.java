package count.jgame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.util.Pair;
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

		log.info("Mysql game design finished loading");
	}

	interface LevelGenerator {
		public Integer getValueFromLevel(Integer level, Integer prev);
	}
	
	interface LevelMapAccess<K,V> {
		public Map<K,V> getMap(ConstructionTypeLevel level);
	}
	
	class LevelPropertyGeneratorsMap 
		extends HashMap<LevelMapAccess<ResourceType,Integer>,Pair<Long,LevelGenerator>>
	{
		private static final long serialVersionUID = 7589537186161115093L;
		
		public LevelPropertyGeneratorsMap putThis(
			LevelMapAccess<ResourceType, Integer> key, 
			Pair<Long,LevelGenerator> value
		) {
			super.put(key, value);
			
			return this;
		}
	}
	
	protected void createLevel(
		ConstructionTypeLevelRepository repository,
		LevelPropertyGeneratorsMap propertyGenerators,
		Long typeId
	) {
		List<Integer> currentValues = new ArrayList<>();
		Integer idx;
		
		for (int lvl = 1; lvl <= 100; lvl++) {
			ConstructionTypeLevel typeLevel = new ConstructionTypeLevel(
				null,
				lvl,
				mainEntityManager.getReference(ConstructionType.class, typeId)
			);
			idx = 0;
			
			for (Map.Entry<LevelMapAccess<ResourceType, Integer>, Pair<Long, LevelGenerator>> entry 
				: propertyGenerators.entrySet()
			) {
				if (lvl == 1) {
					currentValues.add(idx, 0);
				}
				
				currentValues.set(idx, entry.getValue().getSecond().getValueFromLevel(lvl, currentValues.get(idx)));
				
				entry.getKey().getMap(typeLevel)
					.put(
						mainEntityManager.getReference(ResourceType.class, entry.getValue().getFirst()), 
						currentValues.get(idx)
					);
				
				repository.save(typeLevel);
				idx++;
			}
		}
		
		repository.flush();
	}

	public void initResourceType(ResourceTypeRepository repository)
	{	
		repository.save(new ResourceType(1l, "Metal")); // MetalMine + RecyclingPlant
		repository.save(new ResourceType(2l, "Silicium")); // SiliciumMine + RecyclingPlant
		repository.save(new ResourceType(3l, "Deuterium")); // DeuteriumMine + RecyclingPlant
		repository.save(new ResourceType(4l, "Flux")); // FluxInducer
		repository.save(new ResourceType(5l, "Water")); // WaterPlant
		repository.save(new ResourceType(6l, "Coin")); // Bank + ...
		
		// Factory
		repository.save(new ResourceType(7l, "Nanite"));
		repository.save(new ResourceType(8l, "ElectronicCircuit"));
		repository.save(new ResourceType(9l, "Motor"));
		repository.save(new ResourceType(10l, "Wheel"));
		repository.save(new ResourceType(11l, "Turbine"));
		repository.save(new ResourceType(12l, "Hull"));
		repository.save(new ResourceType(13l, "Shield"));
		repository.save(new ResourceType(14l, "SolarCell"));
		repository.save(new ResourceType(15l, "Battery"));
		repository.save(new ResourceType(16l, "Thruster"));
		repository.save(new ResourceType(17l, "Laser"));
		repository.save(new ResourceType(18l, "Wire"));
		repository.save(new ResourceType(19l, "Missile"));
		repository.save(new ResourceType(20l, "Satellite"));
		repository.save(new ResourceType(21l, "Rocket"));
		repository.save(new ResourceType(22l, "Gun"));
		repository.save(new ResourceType(23l, "LandVehicule"));
		repository.save(new ResourceType(24l, "Concrete"));
		repository.save(new ResourceType(25l, "Glass"));
		repository.save(new ResourceType(26l, "Medicalwares"));
		
		repository.save(new ResourceType(27l, "Seeds")); // Fields
		repository.save(new ResourceType(28l, "Food")); // Fields
		repository.save(new ResourceType(29l, "Drinks")); // Brewerie
		
		repository.save(new ResourceType(30l, "SpyReport")); // InformationAgency + Embassy + Radar
		repository.save(new ResourceType(31l, "DataBlock")); // DataCenter + Laboratory + Radar + ADministration + Police + ArchiveModule
		
		repository.save(new ResourceType(32l, "Insight", false)); // Embassy + Racetrack
		repository.save(new ResourceType(33l, "Fun", false)); // Casino + NightClub + SwimmingPool + Paintball + Racetrack
		repository.save(new ResourceType(34l, "PeaceOfMind", false)); // Administration + Police + Theater + DormitoryModule + BotanicModule + ParkModule - Casino - Paintball - EmploymentAgency - Racetrack - LaboratoryModule - SportsModule
		repository.save(new ResourceType(35l, "Sex", false)); // NightClub + Theater + Restaurant + Kitchen + Park + Archive
		repository.save(new ResourceType(36l, "Health", false)); // SwimmingPool + Hospital + WaterPlant + SportsModule + DormitoryModule - NightClub - RecyclingPlant - Factory - Restaurant
		repository.save(new ResourceType(37l, "MoneyFactor", false)); // TVModule + Theater
		repository.save(new ResourceType(37l, "Employee", false)); // Colony + EmploymentAgency
		
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
		repository.save(new ConstructionType(7l, "DeuteriumTank"));
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
		repository.save(new ConstructionType(38l, "Racetrack"));
		repository.save(new ConstructionType(39l, "Theater"));
		repository.save(new ConstructionType(40l, "Fields"));
		repository.save(new ConstructionType(41l, "Brewerie"));
		
		repository.save(new ConstructionType(42l, "SpaceStation"));
		repository.save(new ConstructionType(43l, "LabModule"));
		repository.save(new ConstructionType(44l, "SportsModule")); // la santé de la station spatiale joue sur les temps de recherche
		repository.save(new ConstructionType(45l, "DormitoryModule"));
		repository.save(new ConstructionType(46l, "BotanicModule"));
		repository.save(new ConstructionType(47l, "CornFieldModule"));
		repository.save(new ConstructionType(48l, "KitchenModule"));
		repository.save(new ConstructionType(49l, "TVModule"));
		repository.save(new ConstructionType(50l, "WeaponsModule"));
		repository.save(new ConstructionType(51l, "BoosterModule"));
		repository.save(new ConstructionType(52l, "DockerModule"));
		repository.save(new ConstructionType(53l, "StorageModule"));
		repository.save(new ConstructionType(54l, "ArchiveModule"));
		repository.save(new ConstructionType(55l, "ParkModule"));
		
		repository.flush();
	}
	
	public void initConstructionTypeLevel(ConstructionTypeLevelRepository repository)
	{
		LevelMapAccess<ResourceType,Integer> consumptions = (mine) -> mine.getConsumptions();
		LevelMapAccess<ResourceType,Integer> productions = (mine) -> mine.getProductions();
		LevelMapAccess<ResourceType,Integer> storage = (warehouse) -> warehouse.getStorage();
		
		// metal mine consummes nanites to produce metal
		this.createLevel(
			repository,
			new LevelPropertyGeneratorsMap()
				.putThis(
					consumptions, 
					Pair.of(7l, (lvl, prev) -> lvl)
				)
				.putThis(
					productions, 
					Pair.of(1l, (lvl, prev) -> lvl == 1 ? 10 : prev + (int) Math.round(Math.sqrt(lvl)))
				)
			,
			1l
		);
		
		// silicium mine consumes nanites to produce silicium
		this.createLevel(
			repository,
			new LevelPropertyGeneratorsMap()
				.putThis(
					consumptions, 
					Pair.of(7l, (lvl, prev) -> lvl)
				)
				.putThis(
					productions, 
					Pair.of(2l, (lvl, prev) -> lvl == 1 ? 5 : prev + (int) Math.round(Math.sqrt(lvl)))
				)
			,
			2l
		);
		
		// deuterium mine consumes nanites to produce deuterium
		this.createLevel(
			repository, 
			new LevelPropertyGeneratorsMap()
				.putThis(
					consumptions, 
					Pair.of(7l, (lvl, prev) -> lvl)
				)
				.putThis(
					productions, 
					Pair.of(3l, (lvl, prev) -> lvl == 1 ? 1 : prev + lvl)
				)
			,
			3l
		);
		
		// metal warehouse stores metal
		this.createLevel(
			repository, 
			new LevelPropertyGeneratorsMap()
				.putThis(
					storage, 
					Pair.of(1l, (lvl, prev) -> lvl == 1 ? 1000 : (int) Math.round(prev * 1.25))
				)
			,
			4l
		);
		
		// silicium warehouse stores silicium
		this.createLevel(
			repository, 
			new LevelPropertyGeneratorsMap()
				.putThis(
					storage, 
					Pair.of(2l, (lvl, prev) -> lvl == 1 ? 500 : (int) Math.round(prev * 1.7))
				)
			,
			5l
		);
		
		// deuterium tank stores deuterium
		this.createLevel(
			repository,
			new LevelPropertyGeneratorsMap()
				.putThis(
					storage, 
					Pair.of(3l, (lvl, prev) -> lvl == 1 ? 200 : (int) Math.round(prev * 1.5))
				)
			,
			6l
		);
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
		repository.save(new Research(1l, "RocketScience")); // Datacenter
		repository.save(new Research(2l, "Shielding")); // Shipyard
		repository.save(new Research(3l, "Weapons")); // MilitaryBase
		repository.save(new Research(41l, "CarsTrucksAndBikes")); // Racetrack
		repository.save(new Research(5l, "Aeronautics")); // Laboratory
		repository.save(new Research(6l, "Robotics")); // IndustrialCenter
		repository.save(new Research(7l, "Logistics")); // Supermarket + MilitaryBase + IndustrialCenter
		repository.save(new Research(8l, "Lasers")); // Laboratory
		repository.save(new Research(9l, "Electronics")); // IndustrialCenter
		repository.save(new Research(10l, "Datamining")); // Datacenter
		repository.save(new Research(11l, "ArtificialIntelligence")); // Datacenter
		repository.save(new Research(12l, "Architecture")); // 
		repository.save(new Research(13l, "Mechanic")); // 
		repository.save(new Research(14l, "Hydroponics")); // SpaceStation
		repository.save(new Research(15l, "Alienistry")); // SpaceStation
		repository.save(new Research(16l, "Geology")); // StrategicComplex
		repository.save(new Research(17l, "Agronomics")); // CornFieldModule + Supermarket
		repository.save(new Research(18l, "Medecine")); // Hospital
		repository.save(new Research(19l, "Communications")); // Radar
		repository.save(new Research(20l, "Espionage")); // EspionNage en FR
		repository.save(new Research(21l, "ArtsAndCulture")); // TVModule + Theater
		
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

		type = new AdministrableLocationType(3l, "IndustrialCenter");
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
		repository.addConstruction(type.getId(), 12l);
		repository.addConstruction(type.getId(), 13l);
		repository.addConstruction(type.getId(), 14l);
		repository.addConstruction(type.getId(), 15l);
		repository.addConstruction(type.getId(), 16l);
		repository.addConstruction(type.getId(), 17l);
		repository.addConstruction(type.getId(), 18l);
		
		type = new AdministrableLocationType(4l, "MilitaryBase");
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
		repository.addConstruction(type.getId(), 20l);
		repository.addConstruction(type.getId(), 21l);
		repository.addConstruction(type.getId(), 22l);
		repository.addConstruction(type.getId(), 23l);
		repository.addConstruction(type.getId(), 24l);
		
		type = new AdministrableLocationType(5l, "Colony");
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
		repository.addConstruction(type.getId(), 26l);
		repository.addConstruction(type.getId(), 27l);
		repository.addConstruction(type.getId(), 28l);
		repository.addConstruction(type.getId(), 29l);
		repository.addConstruction(type.getId(), 30l);
		repository.addConstruction(type.getId(), 31l);
		repository.addConstruction(type.getId(), 32l);
		repository.addConstruction(type.getId(), 33l);
		repository.addConstruction(type.getId(), 34l);
		repository.addConstruction(type.getId(), 35l);
		repository.addConstruction(type.getId(), 36l);
		repository.addConstruction(type.getId(), 37l);
		repository.addConstruction(type.getId(), 38l);
		repository.addConstruction(type.getId(), 39l);
		repository.addConstruction(type.getId(), 40l);
		repository.addConstruction(type.getId(), 41l);
		

		type = new AdministrableLocationType(6l, "SpaceStation");
		type.setCanDoResearch(true);
		type.setCanConstructBuildings(true);
		repository.saveAndFlush(type);
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
		repository.addConstruction(type.getId(), 52l);
		repository.addConstruction(type.getId(), 53l);
		repository.addConstruction(type.getId(), 54l);
		repository.addConstruction(type.getId(), 55l);
		repository.addResearch(type.getId(), 12l);
		
		type = new AdministrableLocationType(7l, "Shipyard");
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
		
		type = new AdministrableLocationType(8l, "Laboratory");
		type.setCanDoResearch(true);
		repository.saveAndFlush(type);
		repository.addResearch(type.getId(), 1l);
		repository.addResearch(type.getId(), 2l);
		repository.addResearch(type.getId(), 3l);
		repository.addResearch(type.getId(), 4l);
		repository.addResearch(type.getId(), 5l);
		repository.addResearch(type.getId(), 6l);
		repository.addResearch(type.getId(), 7l);
		repository.addResearch(type.getId(), 8l);
		repository.addResearch(type.getId(), 9l);
		repository.addResearch(type.getId(), 10l);
		repository.addResearch(type.getId(), 11l);
		repository.addResearch(type.getId(), 12l);
		repository.addResearch(type.getId(), 13l);
		repository.addResearch(type.getId(), 14l);
		repository.addResearch(type.getId(), 15l);
		repository.addResearch(type.getId(), 16l);
		repository.addResearch(type.getId(), 17l);
		repository.addResearch(type.getId(), 18l);
		repository.addResearch(type.getId(), 19l);
		repository.addResearch(type.getId(), 20l);
		repository.addResearch(type.getId(), 21l);
		
		repository.flush();
	}
	
	public void initConstructionTypeAdministrableLocationType(ConstructionTypeRepository repository)
	{
		repository.updateAdministrableLocationType(1l, 2l);
		repository.updateAdministrableLocationType(11l, 3l);
		repository.updateAdministrableLocationType(19l, 4l);
		repository.updateAdministrableLocationType(25l, 5l);
		repository.updateAdministrableLocationType(38l, 6l);
		repository.updateAdministrableLocationType(12l, 7l);
		repository.updateAdministrableLocationType(13l, 8l);
		
		repository.flush();
	}
}
