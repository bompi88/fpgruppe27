package framework;

public interface Observable {

	public void addObserver(Observer ob);

	abstract void fireObserverEvent(String event);
}
