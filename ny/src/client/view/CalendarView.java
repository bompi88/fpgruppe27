package view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import framework.Observer;

import model.Employee;
import model.Meeting;

import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class CalendarView extends JPanel {

	private JPanel topPanelWrapper;
	private JPanel weeklyCalendarWrapper;
	private SubscribeToCalendarPanel addCalendarPanel;
	private WeeklyCalendarPanel weeklyCalendarPanel;
	private CalendarCtrlPanel calendarCtrlPanel;
	private CalendarTitlePanel calendarTitlePanel;
	private JScrollPane scrollPane;
	private RelativeLayout rl1;
	private RelativeLayout rl2;
	
	private Dimension topPanelWrapperSize;
	private Dimension weeklyCalendarWrapperSize;
	private Dimension calendarViewSize;
	private Dimension scrollPaneSize;
	
	public CalendarView() {
		
		this.topPanelWrapper = new JPanel();
		this.weeklyCalendarWrapper = new JPanel();
		this.addCalendarPanel = new SubscribeToCalendarPanel();
		this.calendarCtrlPanel = new CalendarCtrlPanel();
		this.calendarTitlePanel = new CalendarTitlePanel();
		this.weeklyCalendarPanel = new WeeklyCalendarPanel();
		
		// init some values
		this.topPanelWrapperSize = new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT);
		this.weeklyCalendarWrapperSize = new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.MAIN_FRAME_HEIGHT-AppConstants.HEADER_PANEL_HEIGHT);
		this.calendarViewSize = new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.MAIN_FRAME_HEIGHT);
		this.scrollPaneSize = new Dimension(weeklyCalendarWrapperSize.width, weeklyCalendarWrapperSize.height - 20);
		
		// use realative layout
		this.rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		this.rl1.setAlignment(RelativeLayout.LEADING);
		this.rl2 = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		this.rl2.setAlignment(RelativeLayout.LEADING);
		

		// initialize panels
		topPanelWrapper.setLayout(rl2);
		topPanelWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		topPanelWrapper.setPreferredSize(topPanelWrapperSize);
		
		weeklyCalendarWrapper.setLayout(rl1);
		weeklyCalendarWrapper.setBackground(AppConstants.CALENDAR_BG_COLOR);
		weeklyCalendarWrapper.setPreferredSize(weeklyCalendarWrapperSize);
		weeklyCalendarWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(AppConstants.CALENDAR_BG_COLOR);
		scrollPane.setPreferredSize(scrollPaneSize);
		scrollPane.setViewportView(weeklyCalendarWrapper);

		// set configuration for this view
		setLayout(rl1);
		setPreferredSize(calendarViewSize);
		
		// add views
		weeklyCalendarWrapper.add(weeklyCalendarPanel);
		topPanelWrapper.add(addCalendarPanel);
		topPanelWrapper.add(calendarTitlePanel);
		topPanelWrapper.add(calendarCtrlPanel);
		
		add(topPanelWrapper);
		add(scrollPane);

		// resize components in title panel
		addCalendarPanel.fillSizeOfParent();
		calendarCtrlPanel.fillSizeOfParent();
		calendarTitlePanel.fillSizeOfParent();
	}
	
	public void addNextWeekButtonListener(ActionListener listener) {
		calendarCtrlPanel.addNextWeekButtonListener(listener);
	}
	
	public void addPreviousWeekButtonListener(ActionListener listener) {
		calendarCtrlPanel.addPreviousWeekButtonListener(listener);
	}
	
	public void addSubscribeCalendarButtonListener(ActionListener listener) {
		addCalendarPanel.addSubscribeCalendarButtonListener(listener);
	}
	
	public void addSubscribeCalendarTextFieldListener(KeyListener listener) {
		addCalendarPanel.addSubscribeCalendarTextFieldListener(listener);
	}
	
	public void addAsObserverToAllMeetings(Observer ob) {
		weeklyCalendarPanel.addAsObserverToAllMeetings(ob);
	}
	
	public void setWeek(int week) {
		calendarCtrlPanel.setWeek(week);
		weeklyCalendarPanel.setWeek(week);
	}
	
	public void setCalendarModel(List<Meeting> calendarModel) {
		weeklyCalendarPanel.setCalendarModel(calendarModel);
	}

	public List<Employee> getAllSubscriptions() {
		return addCalendarPanel.getAllSubscriptions();
	}
	
	public void addSubscribedCalendar() {
		addCalendarPanel.addCalendar();
	}	
}
