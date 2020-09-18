package count.jgame.exceptions;

import javax.persistence.EntityNotFoundException;

public class ControllerEntityNotFoundException extends ControllerException {
	private static final long serialVersionUID = 1954212443828327518L;

	public ControllerEntityNotFoundException(EntityNotFoundException ex) {
		super("entityNotFound", ex.getMessage(), ex);
	}
}
