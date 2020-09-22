package count.jgame.controllers;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import count.jgame.models.AdministrableLocation;
import count.jgame.models.ProductionRequest;
import count.jgame.services.AdministrableLocationService;

@RestController
public class AdministrableLocationController {
	@Autowired
	private AdministrableLocationService service;
	
	@GetMapping(path = "/at/{slug}-{gameId}")
	public AdministrableLocation get(
		@PathVariable("gameId") Long gameId, 
		@PathVariable("slug") String slug
	) {
		return service.preloadByGameIdAndSlug(gameId, slug);
	}
	
	@PutMapping(path = "/at/{slug}-{gameId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public AdministrableLocation put(
		@PathVariable("gameId") Long gameId,
		@PathVariable("slug") String slug,
		@RequestBody AdministrableLocation input
	) {
		return service.update(gameId, slug, input);
	}
	
	@PostMapping(path = "/at/{slug}-{gameId}/build")
	@ResponseBody
	public AdministrableLocation pushToProd(
		@PathVariable("gameId") Long gameId,
		@PathVariable("slug") String slug,
		@RequestBody ProductionRequest productionRequest
	) {
		AdministrableLocation location = service.getByGameIdAndSlug(gameId, slug);
		if (location == null) {
			throw new EntityNotFoundException("Location not found");
		}
		
		service.pushToProd(location, productionRequest);
		
		return service.preloadByGameIdAndSlug(gameId, slug);
	}
}
