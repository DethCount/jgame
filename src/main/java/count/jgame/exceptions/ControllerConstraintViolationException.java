package count.jgame.exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

class ControllerConstraintViolationException extends ControllerException {
	private static final long serialVersionUID = 3477791751345599911L;
	public String type = "constraintViolation";
	public Map<String,List<String>> errors;
	
	public ControllerConstraintViolationException(ConstraintViolationException ex) {
		super("constraintViolations", "Constraint violations", ex);
		
		this.errors = new HashMap<>();
		for (ConstraintViolation<?> cv : ex.getConstraintViolations()) {
			String key = cv.getPropertyPath().toString();
			if (this.errors.containsKey(key)) {
				this.errors.get(key).add(cv.getMessage());
				continue;
			}
			
			List<String> messages = new ArrayList<>();
			messages.add(cv.getMessage());
			this.errors.put(key, messages);
		}
	}
}