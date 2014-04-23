package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import database.ClientObjectFactory;

import framework.Observer;
import framework.State;

import view.CalendarElement;
import view.CalendarView;
import model.Employee;
import model.Meeting;
import model.Participant;
import model.Status;
import model.Week;


public class CalendarStateController implements State, Observer {

	private Calendar calendar = new GregorianCalendar();
	
	private CalendeerClient context;
	private List<Meeting> calendarModel;
	private CalendarView calendarView;
	
	private Week selectedWeek;
	
	public CalendarStateController(CalendeerClient context, List<Meeting> calendarModel, CalendarView calendarView) {
		this.context = context;
		this.calendarModel = calendarModel;
		this.calendarView = calendarView;
		this.selectedWeek = new Week(calendar.get(Calendar.WEEK_OF_YEAR));
		
		calendarView.addPreviousWeekButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setPreviousWeek();
			}
		});

		calendarView.addNextWeekButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setNextWeek();
			}
		});
		
		calendarView.addSubscribeCalendarButtonListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addSubscribedCalendar();
				updateState();
			}
		});
		
		calendarView.addSubscribeCalendarTextFieldListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					addSubscribedCalendar();
					updateState();
				}
			}
		});
	}
	
	public void setCalendarModel() {
		List<Employee> subscribers = getAllSubscriptions();
		
		String[] subscribersUsernames = new String[subscribers.size() + 1];  
		
		for (int i = 0; i < subscribers.size(); i++) {
			subscribersUsernames[i] = subscribers.get(i).getUsername(); 
		}
		
		subscribersUsernames[subscribers.size()] = CalendeerClient.getCurrentEmployee().getUsername();
		
		calendarModel = ClientObjectFactory.getMeetingByWeek(selectedWeek.getWeekNumber(), subscribersUsernames);
		
		calendarView.setWeek(selectedWeek.getWeekNumber());
		calendarView.setCalendarModel(calendarModel);
		calendarView.addAsObserverToAllMeetings(this);
	}
	
	public CalendeerClient getContext() {
		return context;
	}
	
	public void setNextWeek() {			
		selectedWeek.setWeek(getNextWeek());
		setCalendarModel();
	}

	public int getNextWeek() {
		if(selectedWeek.getWeekNumber() + 1 > calendar.getWeeksInWeekYear())
			selectedWeek.setWeek(0);

		return selectedWeek.getWeekNumber() + 1;
	}

	public void setPreviousWeek() {
		selectedWeek.setWeek(getPreviousWeek());
		setCalendarModel();
	}

	public int getPreviousWeek() {
		if(selectedWeek.getWeekNumber() - 1 < 1)
			selectedWeek.setWeek(calendar.getWeeksInWeekYear() + 1);

		return selectedWeek.getWeekNumber() - 1;
	}
	
	public void addSubscribedCalendar() {
		calendarView.addSubscribedCalendar();
	}
	
	public List<Employee> getAllSubscriptions() {
		return calendarView.getAllSubscriptions();
	}

	@Override
	public void changeEvent(String event, Object obj) {
		
		Meeting meet = ((CalendarElement)obj).getModel();
		if(event.equals("delete")) {
			
			Employee emp = CalendeerClient.getCurrentEmployee();
			
			Participant p = new Participant(emp.getUsername(), emp.getName(), emp.getEmail(), emp.getPassword(), meet.getMeetid(), Status.DECLINED);
			
			meet.removeParticipants(p);
			if(calendarModel.remove(meet)){
				if(emp.getUsername().equals(meet.getResponsible().getUsername())){
					meet.delete();
				} else{
					meet.setNegativeAttendenceAndRemove(emp);
				}
				calendarView.setCalendarModel(calendarModel);
			}
		} else if(event.equals("edit")) {
			context.setMeetingModel(meet);
			
			if(CalendeerClient.getCurrentEmployee().getUsername().equals(meet.getResponsible().getUsername())) {
				context.setState(context.getEditMeetingState());
			} else {
				context.setState(context.getViewMeetingState());
			}	
		}
	}
	
	@Override
	public void hideState() {
		calendarView.setVisible(false);
	}

	@Override
	public void showState() {
		calendarView.setVisible(true);
	}

	@Override
	public void updateState() {
		setCalendarModel();
	}

	@Override
	public void initState() {
		setCalendarModel();
	}
}
