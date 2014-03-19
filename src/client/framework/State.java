package framework;

/**
 * Has methods which is mandatory for States. Implement if class is a State.
 */
public interface State {
	
	/**
	 * Called when a state is beeing set.
	 */
	public abstract void show();
	
	/**
	 * Called when a state is beeing unset.
	 */
	public abstract void hide();
	
}