package controller;

import model.Meeting;
import view.AppointmentView;

import framework.Controller;
import framework.State;

/**
 * 
 */
public class AppointmentCtrl extends Controller implements State {

	protected AppointmentView appointmentPanel;
	
	public AppointmentCtrl(Controller ctrl) {
		super(ctrl);
		
		setModel(new Meeting());
		appointmentPanel = new AppointmentView(this);
		
		ctrl.getMainFrame().getContentPane().add(appointmentPanel);
	}
	
	@Override
	public void show() {
		appointmentPanel.setVisible(true);
	}

	@Override
	public void hide() {
		appointmentPanel.setVisible(false);
	}

}
