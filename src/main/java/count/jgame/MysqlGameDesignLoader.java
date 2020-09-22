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
import count.jgame.models.ResourceType;
import count.jgame.models.ShipType;
import count.jgame.repositories.AdministrableLocationTypeRepository;
import count.jgame.repositories.ConstructionTypeLevelRepository;
import count.jgame.repositories.ConstructionTypeRepository;
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
		
		this.initAdministrableLocationType(
			(AdministrableLocationTypeRepository)
			this.repositories.getRepositoryFor(AdministrableLocationType.class).get()
		);
	}
	
	public void initResourceType(ResourceTypeRepository repository)
	{	
		repository.save(new ResourceType(1l, "Metal"));
		repository.save(new ResourceType(2l, "Silicium"));
		repository.save(new ResourceType(3l, "Deuterium"));
		
		repository.flush();
	}
	
	public void initConstructionType(ConstructionTypeRepository repository)
	{	
		repository.save(new ConstructionType(1l, "MetalMine"));
		repository.save(new ConstructionType(2l, "CrystalMine"));
		repository.save(new ConstructionType(3l, "DeuteriumMine"));
		repository.save(new ConstructionType(4l, "MetalWarehouse"));
		repository.save(new ConstructionType(5l, "CrystalWarehouse"));
		repository.save(new ConstructionType(6l, "DeuteriumWarehouse"));
		repository.save(new ConstructionType(7l, "Shipyard"));
		repository.save(new ConstructionType(8l, "Laboratory"));
		repository.save(new ConstructionType(9l, "DataCenter"));
		
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
		
		repository.flush();
	}
	
	public void initAdministrableLocationType(AdministrableLocationTypeRepository repository)
	{	
		AdministrableLocationType planet = new AdministrableLocationType(1l, "Planet");
		planet.getResources().add(mainEntityManager.getReference(ResourceType.class, 1l));
		planet.getResources().add(mainEntityManager.getReference(ResourceType.class, 2l));
		planet.getResources().add(mainEntityManager.getReference(ResourceType.class, 3l));
		planet.getConstructions().add(mainEntityManager.getReference(ConstructionType.class, 1l));
		
		repository.save(planet);
		
		repository.flush();
	}
}
