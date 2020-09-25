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
	
	@GetMapping(path = "/game/{gameId}/at/{path}")
	public AdministrableLocation get(
		@PathVariable("gameId") Long gameId,
		@PathVariable("path") String path
	) {
		return service.preloadByGameIdAndPath(gameId, path);
	}
	
	@PostMapping(path = "/game/{gameId}/at/{path}/build")
	@ResponseBody
	public AdministrableLocation pushToProd(
		@PathVariable("gameId") Long gameId,
		@PathVariable("path") String path,
		@RequestBody ProductionRequest productionRequest
	) {
		AdministrableLocation location = service.getByGameIdAndPath(gameId, path);
		if (location == null) {
			throw new EntityNotFoundException("Location not found");
		}
		
		service.pushToProd(location, productionRequest);
		
		return service.preloadById(location.getId());
	}
	
	@PutMapping(
		path = "/game/{gameId}/at/{path}", 
		consumes = {MediaType.APPLICATION_JSON_VALUE}
	)
	@ResponseBody
	public AdministrableLocation put(
		@PathVariable("gameId") Long gameId,
		@PathVariable("path") String path,
		@RequestBody AdministrableLocation input
	) {
		return service.update(gameId, path, input);
	}
}
