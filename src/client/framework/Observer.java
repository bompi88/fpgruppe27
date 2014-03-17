package framework;

/**
 * Implement this to make a class observe an class which implements the observable interface.
 */
public interface Observer {
	
	/**
	 * Implement this to take care of observer events.
	 * @param event
	 * @param obj
	 */
	public void changeEvent(String event, Object obj);

}
