package view;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import controller.CalendeerClient;
import model.Meeting;
import resources.AppConstants;
import utils.RelativeLayout;
import framework.Observer;

@SuppressWarnings("serial")
public class CalendarColumn extends JPanel {

	private JLabel dayLabel;
	
	private List<Meeting> model;
	private List<CalendarElement> meetings = new ArrayList<CalendarElement>();
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	private int dayViewWidth = ((AppConstants.MAIN_FRAME_WIDTH - AppConstants.SIDEBAR_WIDTH - 50) / 7) + 2;
	private int dayViewHeight = AppConstants.MAIN_FRAME_HEIGHT - AppConstants.HEADER_PANEL_HEIGHT;
	private Dimension columnSize = new Dimension(dayViewWidth, dayViewHeight);
	private Date thisDate;

	
	public CalendarColumn() {

		this.dayLabel = new JLabel("", SwingConstants.CENTER);
		
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);
		
		setLayout(rl);
		
		setPreferredSize(columnSize);
		setBackground(AppConstants.CALENDAR_BG_COLOR);
		
		add(dayLabel);
	}
	
	public void setDate(Date date) {
		this.thisDate = date;
	}
	
	public void addAsObserverToAllMeetings(Observer ob) {
		observers.add(ob);
		updateView();
	}

	public List<Meeting> getModel() {
		return model;
	}

	public void setModel(List<Meeting> meetingModel) {
		model = meetingModel;	
		updateView();
	}
	
	private void updateView() {
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
			for (Observer ob : observers)
				e.addObserver(ob);
		}
		
		revalidate();
        repaint();
	}
	
	public static Color choiceColor(Meeting meeting, boolean hover){
		Color tempColor; 
		String currentUser = CalendeerClient.getCurrentEmployee().getUsername(); 
		
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
	
	public void setHeaderText(String week) {
		dayLabel.setText(week);
	}
}
