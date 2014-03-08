package controller;

import view.CalendarView;

import framework.Controller;
import framework.State;

public class CalendarCtrl extends Controller implements State {

	private CalendarView calendarPanel;
	
	public CalendarCtrl(Controller ctrl) {
		super(ctrl);
		
		calendarPanel = new CalendarView();

		ctrl.getMainFrame().getContentPane().add(calendarPanel);
	}

	@Override
	public void show() {
		calendarPanel.setVisible(true);
	}

	@Override
	public void hide() {
		calendarPanel.setVisible(false);
	}

}
