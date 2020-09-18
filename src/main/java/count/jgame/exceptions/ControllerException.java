package count.jgame.exceptions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"cause","stackTrace","suppressed","message"})
public class ControllerException extends RuntimeException {
	private static final long serialVersionUID = 2249942278694336342L;
	
	String type;

	public ControllerException(String type, String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		
		this.type = type;
	}

	public ControllerException(String type, String message, Throwable cause) {
		super(message, cause);
		
		this.type = type;
	}

	public ControllerException(String type, Throwable cause) {
		super(cause);

		this.type = type;
	}

	public ControllerException(String type) {
		super();
		
		this.type = type;
	}
}
