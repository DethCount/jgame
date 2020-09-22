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

import count.jgame.models.Game;
import count.jgame.services.GameService;

@RestController
public class GameController {
	
	@Autowired
	private GameService gameService;
	
	@GetMapping(path = "/game/{id}")
	@ResponseBody
	public Game get(@PathVariable(name = "id") Long id)
	{
		Game game = gameService.get(id);
		if (game == null) {
			throw new EntityNotFoundException(String.format("game not found : %d", id));
		}
		
		return game;
	}
	
	@PostMapping(path = "/game", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Game post(@RequestBody Game input)
	{
		return gameService.save(input);
	}
	
	@PutMapping(path = "/game/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public Game put(
		@PathVariable("id") Long id,
		@RequestBody Game input
	) {
		return gameService.update(id, input);
	}
}
