package count.jgame.exceptions;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {"count.jgame.controllers"})
public class EntityNotFoundExceptionHandler {
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ControllerEntityNotFoundException> handle(EntityNotFoundException ex) {
		return new ResponseEntity<ControllerEntityNotFoundException>(
			new ControllerEntityNotFoundException(ex), 
			HttpStatus.NOT_FOUND
		);
	}
}
