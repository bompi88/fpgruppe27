package controller;

import view.AppointmentView;

import framework.Controller;
import framework.State;

/**
 * 
 */
public class AppointmentCtrl extends Controller implements State {

	private AppointmentView appointmentPanel;
	
	public AppointmentCtrl(Controller ctrl) {
		super(ctrl);
		
		appointmentPanel = new AppointmentView();
		
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
