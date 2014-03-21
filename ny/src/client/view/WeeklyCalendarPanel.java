package view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JPanel;

import framework.Observer;

import resources.AppConstants;
import utils.RelativeLayout;

import model.Meeting;

@SuppressWarnings("serial")
public class WeeklyCalendarPanel extends JPanel {

	private Calendar cal = Calendar.getInstance();

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
		mondayView = new CalendarColumn();
		thuesdayView = new CalendarColumn();
		wednesdayView = new CalendarColumn();
		thursdayView = new CalendarColumn();
		fridayView = new CalendarColumn();
		saturdayView = new CalendarColumn();
		sundayView = new CalendarColumn();
		
		mondayView.setDate(mondayDate);
		thuesdayView.setDate(thuesdayDate);
		wednesdayView.setDate(wednesdayDate);
		thursdayView.setDate(thursdayDate);
		fridayView.setDate(fridayDate);
		saturdayView.setDate(saturdayDate);
		sundayView.setDate(sundayDate);
		
		mondayView.setModel(monday);
		thuesdayView.setModel(thuesday);
		wednesdayView.setModel(wednesday);
		thursdayView.setModel(thursday);
		fridayView.setModel(friday);
		saturdayView.setModel(saturday);
		sundayView.setModel(sunday);

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
		setVisible(true);
	}

	public void setWeek(int week) {
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
	
	public void addAsObserverToAllMeetings(Observer ob) {
		mondayView.addAsObserverToAllMeetings(ob);
		thuesdayView.addAsObserverToAllMeetings(ob);
		wednesdayView.addAsObserverToAllMeetings(ob);
		thursdayView.addAsObserverToAllMeetings(ob);
		fridayView.addAsObserverToAllMeetings(ob);
		saturdayView.addAsObserverToAllMeetings(ob);
		sundayView.addAsObserverToAllMeetings(ob);
	}
	
	public void setCalendarModel(List<Meeting> meetings) {

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
	}
}
