package framework;

/**
 * Implement this to make class observable to classes which implements Observer
 */
public interface Observable {

	/**
	 * Add observer to this class
	 * @param ob
	 */
	public void addObserver(Observer ob);

	/**
	 * fire an observer event. All observers which is added, will
	 * get the event message.
	 * @param event
	 * @param obj
	 */
	abstract void fireObserverEvent(String event, Object obj);
}