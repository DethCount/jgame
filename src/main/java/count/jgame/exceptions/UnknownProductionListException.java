package count.jgame.exceptions;

public class UnknownProductionListException extends ControllerException 
{
	private static final long serialVersionUID = -1871748340283078566L;

	private final static String TYPE = "unknwonProductionList";
	
	String listName;

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public UnknownProductionListException(
		String listName, 
		String message, 
		Throwable cause, 
		boolean enableSuppression,
		boolean writableStackTrace
	) {
		super(TYPE, message, cause, enableSuppression, writableStackTrace);
		
		this.listName = listName;
	}

	public UnknownProductionListException(String listName, String message, Throwable cause) {
		super(TYPE, message, cause);

		this.listName = listName;
	}

	public UnknownProductionListException(String listName, Throwable cause) {
		super(TYPE, cause);

		this.listName = listName;
	}

	public UnknownProductionListException(String listName) {
		super(TYPE);

		this.listName = listName;
	}
}
