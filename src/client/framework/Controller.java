package framework;

import javax.swing.JFrame;

import framework.Model;

/**
 * A template for a controller. The controller is responsible for a
 * state.
 */
public abstract class Controller {

	private Controller parentCtrl;
	protected Model model;
	protected boolean isHidden = true;

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	public Controller(Controller ctrl) {
		this.parentCtrl = ctrl;
	}
	
	public Controller() {}

	/**
	 * Gets the main frame (top window).
	 */
	public JFrame getMainFrame() {
		return parentCtrl.getMainFrame();
	}
	
	/**
	 * Sets a controller to become the controller in charge. Override this in
	 * main controller.
	 */
	public void setState(Class<? extends State> c) {
		parentCtrl.setState(c);
	}
	
	public <T extends Model> void setModel(T model) {
		this.model = model;
	}

	@SuppressWarnings("unchecked")
	public <T extends Model> T getModel() {
		return (T) model;
	}
	
	public <T extends Controller> void setParentCtrl(T ctrl) {
		this.parentCtrl = ctrl;
	}

	@SuppressWarnings("unchecked")
	public <T extends Controller> T getParentCtrl() {
		return (T) this.parentCtrl;
	}
	
	/**
	 * Gets the master controller, which take care of a number of
	 * other controllers.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Controller> T getMainCtrl() {
		if (this.parentCtrl != null)
			return (T) this.parentCtrl;
		else
			return (T) this;
	}
}