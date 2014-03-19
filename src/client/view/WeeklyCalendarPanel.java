package view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import model.Meeting;
import resources.AppConstants;
import utils.RelativeLayout;
import controller.MainCtrl;
import database.ClientObjectFactory;
import framework.Observer;

@SuppressWarnings("serial")
public class WeeklyCalendarPanel extends JPanel implements Observer {

	private Calendar cal = Calendar.getInstance();

	private List<Meeting> meetings = new ArrayList<Meeting>();

	private Date mondayDate;
	private Date thuesdayDate;
	private Date wednesdayDate;
	private Date thursdayDate;
	private Date fridayDate;
	private Date saturdayDate;
	private Date sundayDate;

	private Date nextMondayDate;

	private List<Meeting> monday = new ArrayList<Meeting>();
	private List<Meeting> thuesday = new ArrayList<Meeting>();
	private List<Meeting> wednesday = new ArrayList<Meeting>();
	private List<Meeting> thursday = new ArrayList<Meeting>();
	private List<Meeting> friday = new ArrayList<Meeting>();
	private List<Meeting> saturday = new ArrayList<Meeting>();
	private List<Meeting> sunday = new ArrayList<Meeting>();

	private CalendarColumn mondayView;
	private CalendarColumn thuesdayView;
	private CalendarColumn wednesdayView;
	private CalendarColumn thursdayView;
	private CalendarColumn fridayView;
	private CalendarColumn saturdayView;
	private CalendarColumn sundayView;

	@SuppressWarnings("deprecation")
	public WeeklyCalendarPanel() {

		setWeek(cal.get(Calendar.WEEK_OF_YEAR));


		// initialize Day Column Views
		mondayView = new CalendarColumn(monday, mondayDate);
		thuesdayView = new CalendarColumn(thuesday, thuesdayDate);
		wednesdayView = new CalendarColumn(wednesday, wednesdayDate);
		thursdayView = new CalendarColumn(thursday, thursdayDate);
		fridayView = new CalendarColumn(thursday, fridayDate);
		saturdayView = new CalendarColumn(saturday, saturdayDate);
		sundayView = new CalendarColumn(sunday, sundayDate);

		mondayView.addObserver(this);
		thuesdayView.addObserver(this);
		wednesdayView.addObserver(this);
		thursdayView.addObserver(this);
		fridayView.addObserver(this);
		saturdayView.addObserver(this);
		sundayView.addObserver(this);

		RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);

		setLayout(rl);

		mondayView.setHeaderText(AppConstants.MONDAY_TEXT + " " + mondayDate.getDate() + "." + mondayDate.getMonth());
		thuesdayView.setHeaderText(AppConstants.THUESDAY_TEXT + " " + thuesdayDate.getDate() + "." + thuesdayDate.getMonth());
		wednesdayView.setHeaderText(AppConstants.WEDNESDAY_TEXT + " " + wednesdayDate.getDate() + "." + wednesdayDate.getMonth());
		thursdayView.setHeaderText(AppConstants.THURSDAY_TEXT + " " + thursdayDate.getDate() + "." + thursdayDate.getMonth());
		fridayView.setHeaderText(AppConstants.FRIDAY_TEXT + " " + fridayDate.getDate() + "." + fridayDate.getMonth());
		saturdayView.setHeaderText(AppConstants.SATURDAY_TEXT + " " + saturdayDate.getDate() + "." + saturdayDate.getMonth());
		sundayView.setHeaderText(AppConstants.SUNDAY_TEXT + " " + sundayDate.getDate() + "." + sundayDate.getMonth());

		add(mondayView);
		add(thuesdayView);
		add(wednesdayView);
		add(thursdayView);
		add(fridayView);
		add(saturdayView);
		add(sundayView);

		setBackground(AppConstants.CALENDAR_BG_COLOR);

		
	}

	public void fireWeekChange(int week) {
		setWeek(week);
		getMeetingsFromDB();

		updateAllCalendarColumnViews();
	}

	private void setWeek(int week) {
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		mondayDate = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
		thuesdayDate = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
		wednesdayDate = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		thursdayDate = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		fridayDate = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		saturdayDate = cal.getTime();
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		sundayDate = cal.getTime();
		cal.set(Calendar.WEEK_OF_YEAR, week + 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		nextMondayDate = cal.getTime();
	}

	public void getMeetingsFromDB() {

		// get all subscriptions, and create an array

//		System.out.println(((CalendarView)getParent().getParent().getParent().getParent().getParent()).calendar.getInstance().get(Calendar.DAY_OF_WEEK));
//		
//		List<Employee> subs = ((CalendarView)getParent().getParent().getParent()).getAllSubscriptions();
//		subs.add((Employee)((MainCtrl)getCtrl().getMainCtrl()).getModel());
//		
//		String[] subsUsers = new String[subs.size()];
//		
//		for (int i = 0; i < subs.size(); i++) {
//			subsUsers[i] = subs.get(i).getUsername();
//		}

		// For testing
		meetings = ClientObjectFactory.getMeetingByWeek(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR), MainCtrl.getCurrentEmployee().getUsername());

		monday = new ArrayList<Meeting>();
		thuesday = new ArrayList<Meeting>();
		wednesday = new ArrayList<Meeting>();
		thursday = new ArrayList<Meeting>();
		friday = new ArrayList<Meeting>();
		saturday = new ArrayList<Meeting>();
		sunday = new ArrayList<Meeting>();

		for(Meeting m : meetings) {
			long startTime = m.getStartTime().getTime();
			long endTime = m.getEndTime().getTime();

			if ((startTime >= mondayDate.getTime() && startTime < thuesdayDate.getTime()) || (endTime > mondayDate.getTime() && startTime < mondayDate.getTime())) {
				monday.add(m);
			} 
			if (startTime >= thuesdayDate.getTime() && startTime < wednesdayDate.getTime() || (endTime > thuesdayDate.getTime() && startTime < thuesdayDate.getTime())) {
				thuesday.add(m);
			} 
			if (startTime >= wednesdayDate.getTime() && startTime < thursdayDate.getTime() || (endTime > wednesdayDate.getTime() && startTime < wednesdayDate.getTime())) {
				wednesday.add(m);
			} 
			if (startTime >= thursdayDate.getTime() && startTime < fridayDate.getTime() || (endTime > thursdayDate.getTime() && startTime < thursdayDate.getTime())) {
				thursday.add(m);
			} 
			if (startTime >= fridayDate.getTime() && startTime < saturdayDate.getTime() || (endTime > fridayDate.getTime() && startTime < fridayDate.getTime())) {
				friday.add(m);
			} 
			if (startTime >= saturdayDate.getTime() && startTime < sundayDate.getTime() || (endTime > saturdayDate.getTime() && startTime < saturdayDate.getTime())) {
				saturday.add(m);
			}
			if (startTime >= sundayDate.getTime() && startTime < nextMondayDate.getTime() || (endTime > sundayDate.getTime() && startTime < sundayDate.getTime())) {
				sunday.add(m);
			}
		}

		mondayView.setModel(monday);
		thuesdayView.setModel(thuesday);
		wednesdayView.setModel(wednesday);
		thursdayView.setModel(thursday);
		fridayView.setModel(friday);
		saturdayView.setModel(saturday);
		sundayView.setModel(sunday);

		updateAllCalendarColumnViews();
	}

	public void updateAllCalendarColumnViews() {
		mondayView.updateView();
		thuesdayView.updateView();
		wednesdayView.updateView();
		thursdayView.updateView();
		fridayView.updateView();
		saturdayView.updateView();
		sundayView.updateView();
	}

	@Override
	public void changeEvent(String event, Object obj) {
		if (event.equals("delete")) {
			mondayView.deleteMeeting(obj, false);
			thuesdayView.deleteMeeting(obj, false);
			wednesdayView.deleteMeeting(obj, false);
			thursdayView.deleteMeeting(obj, false);
			fridayView.deleteMeeting(obj, false);
			saturdayView.deleteMeeting(obj, false);
			sundayView.deleteMeeting(obj, false);

			updateAllCalendarColumnViews();
		} else if (event.equals("weekChange")) {
			fireWeekChange((int)obj);
		}
	}
}