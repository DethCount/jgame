package count.jgame.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import count.jgame.models.ConstructionType;
import count.jgame.models.Game;
import count.jgame.models.ResourceType;

@Service
public class GameService {
	public void updateResources(Game game) {/*
		Map<ResourceType, Long> resources = new HashMap<>();
		
		for (ResourceType type : ResourceType.values()) {
			Long value = 0l;
			
			//ConstructionType mineType = ConstructionType.valueOf(ConstructionType.getResourceMine(type.name()));
			
			Integer level = 0;
			
			if (!game.getConstructions().containsKey(mineType)) {
				continue;
			}
			
			level = game.getConstructions().get(mineType);
			
			if (game.getLastResourceUpdate() != null 
				&& level > 0
			) {
				
			}
			
			resources.put(type, value);
		}
		*/
	}
}
