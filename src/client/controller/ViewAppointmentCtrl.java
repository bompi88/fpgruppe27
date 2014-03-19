package controller;

import model.Meeting;
import view.AppointmentView;
import view.CalendarElement;
import view.ViewAppointmentView;
import framework.Controller;
import framework.Observer;
import framework.State;

public class ViewAppointmentCtrl extends Controller implements State, Observer {
	
	protected ViewAppointmentView appointmentPanel;
	
	public ViewAppointmentCtrl(Controller ctrl) {
		super(ctrl);
		
		setModel(new Meeting());
		appointmentPanel = new ViewAppointmentView((Meeting)model);
		appointmentPanel.addObserver(this);
		
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
	
	public void init() {
		appointmentPanel.setVisible(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void changeEvent(String event, Object obj) {
		if (event.equals("change_state")) {
			((MainCtrl)getMainCtrl()).setState((Class<State>)obj);
		}else if (event.equals("view_appointment")) {
			((MainCtrl)getMainCtrl()).setMeetingModel(((CalendarElement)obj).getModel());
			((MainCtrl)getMainCtrl()).setState(ViewAppointmentCtrl.class);
		}
	}

}
