package controller;

import model.Meeting;
import view.AppointmentView;
import view.CalendarElement;
import view.ViewAppointmentView;

import framework.Controller;
import framework.Observer;
import framework.State;

/**
 * 
 */
public class AppointmentCtrl extends Controller implements State, Observer {

	protected AppointmentView appointmentPanel;
	protected ViewAppointmentView viewAppointmentPanel;
	
	public AppointmentCtrl(Controller ctrl) {
		super(ctrl);
		
		setModel(new Meeting());
		appointmentPanel = new AppointmentView((Meeting)model);
		viewAppointmentPanel = new ViewAppointmentView((Meeting)model);
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
		} else if (event.equals("view_appointment")) {
			((MainCtrl)getMainCtrl()).setMeetingModel(((CalendarElement)obj).getModel());
		}		
	}
	
	public void setMeetingModel(Meeting m) {
		((Meeting)getModel()).setModel(m);
	}
	
	public void setState(Class<? extends State> c) {
		appointmentPanel.hideState();
		viewAppointmentPanel.hideState();
		
		// show a new state
		if(c.equals(appointmentPanel.getClass())) {			
			appointmentPanel.hideState();
		} else if (c.equals(viewAppointmentPanel.getClass())) {
			viewAppointmentPanel.hideState();
		}
	}
}