package count.jgame.exceptions;

public class AbilityException extends RuntimeException
{
	private static final long serialVersionUID = 1642360376090254664L;
	private static String MESSAGE_FORMAT = "%s tried to %s but has no capabilty for that"; 

	protected Object actor;
	
	protected String action;

	public Object getActor() {
		return actor;
	}

	public void setActor(Object actor) {
		this.actor = actor;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public AbilityException() {
		super();
	}

	public AbilityException(
		Object actor, 
		String action, 
		String message, 
		Throwable cause, 
		boolean enableSuppression, 
		boolean writableStackTrace
	) {
		super(message, cause, enableSuppression, writableStackTrace);
		
		this.actor = actor;
		this.action = action;
	}

	public AbilityException(Object actor, String action, String message, Throwable cause) {
		super(message, cause);
		
		this.actor = actor;
		this.action = action;
	}

	public AbilityException(Object actor, String action, String message) {
		super(message);
		
		this.actor = actor;
		this.action = action;
	}

	public AbilityException(Object actor, String action, Throwable cause) {
		super(String.format(MESSAGE_FORMAT, actor.toString(), action), cause);
		
		this.actor = actor;
		this.action = action;
	}
	
	public AbilityException(Object actor, String action) {
		super(String.format(MESSAGE_FORMAT, actor.toString(), action));
		
		this.actor = actor;
		this.action = action;
	}
}
