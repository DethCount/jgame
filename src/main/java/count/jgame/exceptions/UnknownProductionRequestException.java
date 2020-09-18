package count.jgame.exceptions;

public class UnknownProductionRequestException extends ControllerException {
	
	private static final long serialVersionUID = 2936902187165158330L;

	private final static String TYPE = "unknwonProductionRequest";
	
	String requestClassName;
	
	
	public String getRequestClassName() {
		return requestClassName;
	}

	public void setRequestClassName(String requestClassName) {
		this.requestClassName = requestClassName;
	}

	public UnknownProductionRequestException(
		String requestClassName, 
		String message, 
		Throwable cause, 
		boolean enableSuppression,
		boolean writableStackTrace
	) {
		super(TYPE, message, cause, enableSuppression, writableStackTrace);
		
		this.requestClassName = requestClassName;
	}

	public UnknownProductionRequestException(String requestClassName, String message, Throwable cause) {
		super(TYPE, message, cause);
		
		this.requestClassName = requestClassName;
	}

	public UnknownProductionRequestException(String requestClassName, Throwable cause) {
		super(TYPE, cause);
		
		this.requestClassName = requestClassName;
	}

	public UnknownProductionRequestException(String requestClassName) {
		super(TYPE);
		
		this.requestClassName = requestClassName;
	}
}
