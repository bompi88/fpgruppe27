package controller;

import model.Meeting;
import view.AppointmentView;
import view.ViewAppointmentView;
import framework.Controller;
import framework.State;

public class ViewAppointmentCtrl extends Controller implements State {
	
	protected ViewAppointmentView appointmentPanel;
	
	public ViewAppointmentCtrl(Controller ctrl) {
		super(ctrl);
		
		setModel(new Meeting());
		appointmentPanel = new ViewAppointmentView(this);
		
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
