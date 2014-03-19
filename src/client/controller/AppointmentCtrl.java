package controller;

import model.Meeting;
import view.AppointmentView;

import framework.Controller;
import framework.Observer;
import framework.State;

/**
 * 
 */
public class AppointmentCtrl extends Controller implements State, Observer {

	protected AppointmentView appointmentPanel;
	
	public AppointmentCtrl(Controller ctrl) {
		super(ctrl);
		
		setModel(new Meeting());
		appointmentPanel = new AppointmentView((Meeting)model);
		appointmentPanel.addObserver(this);
		
		ctrl.getMainFrame().getContentPane().add(appointmentPanel);
	}
	
	@Override
	public void show() {
		isHidden = false;
		appointmentPanel.setVisible(true);
	}

	@Override
	public void hide() {
		isHidden = true;
		appointmentPanel.setVisible(false);
		
	}
	
	public void init() {
		appointmentPanel.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void changeEvent(String event, Object obj) {
		if (event.equals("change_state")) {
			((MainCtrl)getMainCtrl()).setState((Class<State>)obj);
		}		
	}
}
