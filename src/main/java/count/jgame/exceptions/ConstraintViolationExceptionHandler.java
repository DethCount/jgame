package count.jgame.exceptions;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = {"count.spring.projet1.controllers"})
public class ConstraintViolationExceptionHandler {
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ControllerConstraintViolationException> handleConstraintViolation(ConstraintViolationException ex) {
	    return new ResponseEntity<>(
    		new ControllerConstraintViolationException(ex), 
    		HttpStatus.BAD_REQUEST
		);
	}
}