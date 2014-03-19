package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Employee;
import model.Meeting;
import model.Participant;
import model.Status;
import resources.AppConstants;
import utils.RelativeLayout;
import controller.AppointmentCtrl;
import controller.CalendarCtrl;
import controller.MainCtrl;
import database.ClientObjectFactory;
import framework.Observable;
import framework.Observer;

@SuppressWarnings("serial")
public class CalendarColumn extends JPanel implements Observer, Observable {
	
	private JLabel dayLabel;
	
	private List<Meeting> model;
	private List<CalendarElement> meetings = new ArrayList<CalendarElement>();
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	private int dayViewWidth = ((AppConstants.MAIN_FRAME_WIDTH - AppConstants.SIDEBAR_WIDTH - 30) / 7) + 2;
	private int dayViewHeight = AppConstants.MAIN_FRAME_HEIGHT - AppConstants.HEADER_PANEL_HEIGHT;
	private Dimension columnSize = new Dimension(dayViewWidth, dayViewHeight);
	private Date thisDate;
	Employee userEmp; 
	
	
	
	public CalendarColumn(List<Meeting> model, Date thisDate) {
		this.model = model;
		this.thisDate = thisDate;
		this.userEmp = MainCtrl.getCurrentEmployee();
		
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);
		
		setLayout(rl);
		dayLabel = new JLabel("", SwingConstants.CENTER);
		
		setPreferredSize(columnSize);
		setBackground(AppConstants.CALENDAR_BG_COLOR);
		
		add(dayLabel);
		
		for(Meeting meeting : model) {
			
			Color color = choiceColor(meeting, false); 
			CalendarElement e = new CalendarElement(meeting, thisDate, color);
			meetings.add(e);
			add(e);
			e.setPreferredSize(new Dimension(dayViewWidth, 50));
			e.addObserver(this);
		}
		
	}
	
	public static Color choiceColor(Meeting meeting, boolean hover){
		Color tempColor; 
		String currentUser = MainCtrl.getCurrentEmployee().getUsername(); 
		
		if(meeting.getResponsible().getUsername().equals(currentUser)){
			if(!hover)
				tempColor = AppConstants.MEETING_BOX_COLOR_ADMIN;  
			else 
				tempColor = AppConstants.MEETING_BOX_COLOR_ADMIN_HOVER; 
		}
		else if(meeting.areUserpartInMeeting(currentUser)){
			if(!hover)
				tempColor = AppConstants.MEETING_BOX_COLOR_PART;  
			else 
				tempColor = AppConstants.MEETING_BOX_COLOR_PART_HOVER;  
		}
		else{
			if(!hover)
				tempColor = AppConstants.MEETING_BOX_COLOR_STALKER;  
			else 
				tempColor = AppConstants.MEETING_BOX_COLOR_STALKER_HOVER;  
		}
		return tempColor; 
	}
	
	public void updateView() {
		
		//removeAll();

		for(CalendarElement el : meetings) {
			remove(el);
		}
		
		meetings.removeAll(meetings);
		
		for(Meeting meeting : model) {
			
			Color color = choiceColor(meeting, false); 
			CalendarElement e = new CalendarElement(meeting, thisDate, color);
			meetings.add(e);
			add(e);
			e.setPreferredSize(new Dimension(dayViewWidth, 50));
			e.addObserver(this);
		}
		
		revalidate();
        repaint();
	}

	public List<Meeting> getModel() {
		return model;
	}

	public void setModel(List<Meeting> model) {
		this.model = model;
	}
	
	public void setHeaderText(String week) {
		dayLabel.setText(week);
	}

	
	
	
	@Override
	public void changeEvent(String event, Object obj) {
		Meeting meet = ((CalendarElement)obj).getModel();
		
		if(event.equals("delete")) {
			
			Employee emp = MainCtrl.getCurrentEmployee();
			
			Participant p = new Participant(emp.getName(), emp.getUsername(), emp.getEmail(), emp.getPassword(), Status.DECLINED);
			
			meet.removeParticipants(p);
			if((model.remove(meet))){
				if(p.getUsername().equals(meet.getResponsible().getUsername())){
					ClientObjectFactory.deleteMeeting(meet.getMeetid()); 
					 
				} else{
					ClientObjectFactory.setNegAttandenceAndRemove(meet, emp); 
					
				}
			}
			deleteMeeting(obj, true);
			fireObserverEvent("delete", obj);
			
		} else if(event.equals("edit")) {
			// hmmm.... :P
			MainCtrl mainCtrl = (MainCtrl)((CalendarCtrl)((CalendarView)((JPanel)((WeeklyCalendarPanel)getParent()).getParent()).getParent().getParent().getParent()).getCtrl()).getMainCtrl();
			mainCtrl.setMeetingModel(meet);
			mainCtrl.setState(AppointmentCtrl.class);
			
		} else if(event.equals("view_appointment")) {
			
			MainCtrl mainCtrl = (MainCtrl)((CalendarCtrl)((CalendarView)((JPanel)((WeeklyCalendarPanel)getParent()).getParent()).getParent().getParent().getParent()).getCtrl()).getMainCtrl();
			mainCtrl.setMeetingModel(meet);
			mainCtrl.setState(AppointmentCtrl.class);
		}
	}
	
	public void deleteMeeting(Object obj, boolean hasAlreadyDeletedFromModel) {
		if(!hasAlreadyDeletedFromModel) {
			model.remove(((CalendarElement)obj).getModel());
		}
		remove((CalendarElement)obj);
		revalidate();
		repaint();
	}

	@Override
	public void addObserver(Observer ob) {
		observers.add(ob);
		
	}

	@Override
	public void fireObserverEvent(String event, Object obj) {
		for (Observer o : observers) {
			o.changeEvent(event, obj);
		}
	}
}
