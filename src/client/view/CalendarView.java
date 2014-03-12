package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import controller.AppointmentCtrl;
import controller.CalendarCtrl;
import controller.MainCtrl;

import framework.Model;
import framework.Observable;
import framework.Observer;
import framework.Controller;

import model.EmployeeModel;
import model.MeetingModel;

import resources.AppConstants;
import resources.ImageManager;
import utils.RelativeLayout;
import utils.RoundedPanel;

/**
 * CalendarView class shows weekly calendars which the user has subscribed to.
 */
@SuppressWarnings("serial")
public class CalendarView extends JPanel{
	
	float titleBarScaleWidth = 0.33f;
	
	Calendar calendar = new GregorianCalendar();
	
	private JPanel topPanelWrapper = new JPanel();
	private JPanel weeklyCalendarWrapper = new JPanel();
	
	Controller ctrl;
	
	public CalendarView(Controller ctrl) {
		this.ctrl = ctrl;
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		
		RelativeLayout rl2 = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl2.setAlignment(RelativeLayout.LEADING);
		
		topPanelWrapper.setLayout(rl2);
		topPanelWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		topPanelWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		weeklyCalendarWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
																AppConstants.MAIN_FRAME_HEIGHT-AppConstants.HEADER_PANEL_HEIGHT));
		weeklyCalendarWrapper.setLayout(rl2);
		weeklyCalendarWrapper.setBackground(AppConstants.CALENDAR_BG_COLOR);
		setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
				AppConstants.MAIN_FRAME_HEIGHT));
		weeklyCalendarWrapper.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		add(topPanelWrapper);
		add(weeklyCalendarWrapper);
		
		// construct top bar panel
		AddCalendarPanel addCalendarPanel = new AddCalendarPanel();
		CalendarCtrlPanel calendarCtrlPanel = new CalendarCtrlPanel();
		CalendarTitlePanel calendarTitlePanel = new CalendarTitlePanel();
		WeeklyCalendarPanel weeklyCalendarPanel = new WeeklyCalendarPanel();
		weeklyCalendarWrapper.add(weeklyCalendarPanel);
		topPanelWrapper.add(addCalendarPanel);
		topPanelWrapper.add(calendarTitlePanel);
		topPanelWrapper.add(calendarCtrlPanel);
		
		addCalendarPanel.fillSizeOfParent();
		calendarCtrlPanel.fillSizeOfParent();
		calendarTitlePanel.fillSizeOfParent();
		
	}

	public class CalendarElement extends RoundedPanel implements PropertyChangeListener, Observable {
		
		private List<Observer> observers = new ArrayList<Observer>();
		
		private JLabel meetingTitle;
		
		private MeetingModel model;
		private JLabel deleteButton;
		private ImageIcon normalDeleteIcon;
		private ImageIcon hoverDeleteIcon;
		
		public CalendarElement(MeetingModel model) {
			this.model = model;
			
			deleteButton = new JLabel();
			
			normalDeleteIcon = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon"), 15, 15));
			hoverDeleteIcon = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon_hover"), 15, 15));
			deleteButton.setIcon(normalDeleteIcon);
			
			setLayout(new GridBagLayout());
			
			Font meetTitleFont = new Font("Arial", Font.BOLD, 12);
			
			setBackground(new Color(10,250,250,150));
			meetingTitle = new JLabel(model.getName());
			meetingTitle.setFont(meetTitleFont);
			
			add(meetingTitle, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,0,new Insets(5,5,0,0),0,0));
			add(deleteButton, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.NORTHEAST,0,new Insets(5,0,0,7),0,0));
			
			deleteButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					fireObserverEvent("delete");
					
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					deleteButton.setIcon(hoverDeleteIcon);
					setBackground(new Color(250,20,20,150));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					deleteButton.setIcon(normalDeleteIcon);
					setBackground(new Color(10,250,250,150));
				}
				
			});
			
			this.addMouseListener(new MouseListener() {
				
				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					setBackground(new Color(10,250,250,150));
					
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					setBackground(new Color(10,250,250,100));
					
				}
				
				@Override
				public void mouseClicked(MouseEvent e) {
					fireObserverEvent("edit");
				}
			});
		}

		public MeetingModel getModel() {
			return model;
		}

		public void setModel(MeetingModel model) {
			this.model = model;
			model.addPropertyChangeListener(this);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("name")) {
				meetingTitle.setText(model.getName());
			}
		}

		@Override
		public void addObserver(Observer ob) {
			observers.add(ob);
			
		}

		@Override
		public void fireObserverEvent(String event) {
			for (Observer ob : observers) {
				ob.changeEvent(event, this);
			}
		}
	}
	
	public class CalendarColumn extends JPanel implements Observer {
		
		private JLabel dayLabel;
		
		private List<MeetingModel> model;
		
		private int dayViewWidth = ((AppConstants.MAIN_FRAME_WIDTH - AppConstants.SIDEBAR_WIDTH - 30) / 7) + 2;
		private int dayViewHeight = AppConstants.MAIN_FRAME_HEIGHT - AppConstants.HEADER_PANEL_HEIGHT;
		private Dimension columnSize = new Dimension(dayViewWidth, dayViewHeight);
		
		public CalendarColumn(List<MeetingModel> model) {
			this.model = model;
			
			RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
			rl.setAlignment(RelativeLayout.LEADING);
			
			setLayout(rl);
			dayLabel = new JLabel("", SwingConstants.CENTER);
			
			setPreferredSize(columnSize);
			setBackground(AppConstants.CALENDAR_BG_COLOR);
			
			add(dayLabel);
			
			for(MeetingModel meeting : model) {
				CalendarElement e = new CalendarElement(meeting);
				add(e);
				e.setPreferredSize(new Dimension(dayViewWidth, 50));
				e.addObserver(this);
			}
			
		}

		public List<MeetingModel> getModel() {
			return model;
		}

		public void setModel(List<MeetingModel> model) {
			this.model = model;
		}
		
		public void setHeaderText(String week) {
			dayLabel.setText(week);
		}

		@Override
		public void changeEvent(String event, Object obj) {
			
			if(event.equals("delete")) {
				model.remove(((CalendarElement)obj).model);
				remove((CalendarElement)obj);
				revalidate();
				repaint();
			} else if(event.equals("edit")) {
				MainCtrl mainCtrl = (MainCtrl)((CalendarCtrl)((CalendarView)((JPanel)((WeeklyCalendarPanel)getParent()).getParent()).getParent()).getCtrl()).getMainCtrl();
				mainCtrl.setMeetingModel(((CalendarElement)obj).model);
				mainCtrl.setState(AppointmentCtrl.class);
			}
		}
	}
	
	public class WeeklyCalendarPanel extends JPanel {
		
		private List<MeetingModel> monday = new ArrayList<MeetingModel>();
		private List<MeetingModel> thuesday = new ArrayList<MeetingModel>();
		private List<MeetingModel> wednesday = new ArrayList<MeetingModel>();
		private List<MeetingModel> thursday = new ArrayList<MeetingModel>();
		private List<MeetingModel> friday = new ArrayList<MeetingModel>();
		private List<MeetingModel> saturday = new ArrayList<MeetingModel>();
		private List<MeetingModel> sunday = new ArrayList<MeetingModel>();
		
		private CalendarColumn mondayView;
		private CalendarColumn thuesdayView;
		private CalendarColumn wednesdayView;
		private CalendarColumn thursdayView;
		private CalendarColumn fridayView;
		private CalendarColumn saturdayView;
		private CalendarColumn sundayView;
		
		public WeeklyCalendarPanel() {
			
//			for (int i = 1; i <= 7; i++) {
//				int rand = 1 + (int)(Math.random() * ((10 - 1) + 1));
//				for (int j = 0; j < rand; j++) {
//					MeetingModel m = new MeetingModel();
//					m.setMeetID((i+1)*(i+1)*(j+1));
//					m.setName("meewID" + (i+1)*(i+1)*(j+1));
//					m.setPlace("this is a meeting");
//					
//					switch(i) {
//					case 1:
//						monday.add(monday.size(), m);
//						break;
//					case 2:
//						thuesday.add(thuesday.size(), m);
//						break;
//					case 3:
//						wednesday.add(wednesday.size(), m);
//						break;
//					case 4:
//						thursday.add(thursday.size(), m);
//						break;
//					case 5:
//						friday.add(friday.size(), m);
//						break;
//					case 6:
//						saturday.add(saturday.size(), m);
//						break;
//					case 7:
//						sunday.add(sunday.size(), m);
//						break;
//					}
//				}
//			}
			
			// For testing
			try {
				monday = MeetingModel.fetchMeetingsByWeek(calendar.get(Calendar.WEEK_OF_YEAR));
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			mondayView = new CalendarColumn(monday);
			thuesdayView = new CalendarColumn(thuesday);
			wednesdayView = new CalendarColumn(wednesday);
			thursdayView = new CalendarColumn(thursday);
			fridayView = new CalendarColumn(thursday);
			saturdayView = new CalendarColumn(saturday);
			sundayView = new CalendarColumn(sunday);
			
			RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
			rl.setAlignment(RelativeLayout.LEADING);
			
			setLayout(rl);
			
			mondayView.setModel(monday);
			thuesdayView.setModel(thuesday);
			wednesdayView.setModel(wednesday);
			thursdayView.setModel(thursday);
			fridayView.setModel(friday);
			saturdayView.setModel(saturday);
			sundayView.setModel(sunday);
			
			mondayView.setHeaderText(AppConstants.MONDAY_TEXT);
			thuesdayView.setHeaderText(AppConstants.THUESDAY_TEXT);
			wednesdayView.setHeaderText(AppConstants.WEDNESDAY_TEXT);
			thursdayView.setHeaderText(AppConstants.THURSDAY_TEXT);
			fridayView.setHeaderText(AppConstants.FRIDAY_TEXT);
			saturdayView.setHeaderText(AppConstants.SATURDAY_TEXT);
			sundayView.setHeaderText(AppConstants.SUNDAY_TEXT);
			
			add(mondayView);
			add(thuesdayView);
			add(wednesdayView);
			add(thursdayView);
			add(fridayView);
			add(saturdayView);
			add(sundayView);
			
			setBackground(AppConstants.CALENDAR_BG_COLOR);
			
		}
		
	}
	
	
	public class CalendarTitlePanel extends JPanel {
		
		public CalendarTitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			JLabel title = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
			title.setFont(new Font("Arial", Font.PLAIN, 28));
			setBackground(AppConstants.HEADER_BG_COLOR);
			add(title);
		}
		
		@Override
		public Container getParent() {
			// TODO Auto-generated method stub
			return super.getParent();
		}
		
		public void fillSizeOfParent() {
			setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * titleBarScaleWidth)+1, getParent().getPreferredSize().height));
		}
	}
	
	
	/**
	 * This is the control panel which handles Week traversal in calendar view.
	 */
	public class CalendarCtrlPanel extends JPanel {
		
		JLabel l;
		Week selectedWeek;
		
		public CalendarCtrlPanel() {
			
			JButton nextButton = new JButton(">>");
			PrevButton previousButton = new PrevButton("<<");
			
			JComboBox<Week> weekComboBox = new JComboBox<Week>();
			List<Week> weeks = getWeeks(8);
			
			Week[] arrWeeks = new Week[weeks.size()];
			weeks.toArray(arrWeeks);
			weekComboBox.setModel(new DefaultComboBoxModel<Week>(arrWeeks));
			setBackground(AppConstants.HEADER_BG_COLOR);
			selectedWeek = arrWeeks[0];
			
			l = new JLabel(Integer.toString(selectedWeek.getWeekNumber()));
			
			RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
			rl.setAlignment(RelativeLayout.CENTER);
			setLayout(rl);
			add(l);
			add(weekComboBox);
			add(previousButton);
			add(nextButton);
			
			addPropertyChangeListener(previousButton);
			
			// So the previous week button can set itself to a disabled state. Quick fix.
			firePropertyChange("weekChange", 0, selectedWeek.getWeekNumber());
			
			previousButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setPreviousWeek();
				}
				
			});
			
			
			nextButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					setNextWeek();
				}
				
			});
		}
		
		public void setNextWeek() {			
			int tmp = selectedWeek.getWeekNumber();
			selectedWeek.setWeek(getNextWeek());
			firePropertyChange("weekChange", tmp, selectedWeek.getWeekNumber());
			
			updateWeekLabel();
		}
		
		public int getNextWeek() {
			if(selectedWeek.getWeekNumber() + 1 > calendar.getWeeksInWeekYear())
				selectedWeek.setWeek(0);
			
			return selectedWeek.getWeekNumber() + 1;
		}
		
		public void setPreviousWeek() {
			int tmp = selectedWeek.getWeekNumber();
			selectedWeek.setWeek(getPreviousWeek());
			firePropertyChange("weekChange", tmp, selectedWeek.getWeekNumber());
			
			updateWeekLabel();
		}
		
		public int getPreviousWeek() {
			if(selectedWeek.getWeekNumber() - 1 < 1)
				selectedWeek.setWeek(calendar.getWeeksInWeekYear() + 1);
			
			return selectedWeek.getWeekNumber() - 1;
		}
		
		public void updateWeekLabel() {
			l.setText(Integer.toString(selectedWeek.getWeekNumber()));
		}
		

		@Override
		public Container getParent() {
			// TODO Auto-generated method stub
			return super.getParent();
		}
		
		public void fillSizeOfParent() {
			setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - titleBarScaleWidth)/2)+1, getParent().getPreferredSize().height));
		}
		
		public List<Week> getWeeks(int numberOfWeeks) {
			
			List<Week> weeks = new ArrayList<Week>();
			
			int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
			int numberOfWeeksInYear = calendar.getWeeksInWeekYear();
			
			int numberOfWeeksAddedThisYear = 0;
			
			for(int i = 0; i < numberOfWeeks; i++) {
				
				if (currentWeek + i <= numberOfWeeksInYear) {
					weeks.add(new Week(currentWeek + i));
					numberOfWeeksAddedThisYear++;
				} else {
					weeks.add(new Week(i - numberOfWeeksAddedThisYear));
				}
			}
			
			return weeks;
		}
		
		public class Week {
			private int weekNumber;
			
			public Week(int weekNumber) {
				this.weekNumber = weekNumber;
			}
			
			public void setWeek(int week) {
				weekNumber = week;
			}
			
			public int getWeekNumber() {
				return weekNumber;
			}
			
			@Override
			public String toString() {
				return Integer.toString(weekNumber);
			}
				
		}
		
		public class PrevButton extends JButton implements PropertyChangeListener {

			public PrevButton() {}
			
			public PrevButton(String label) {
				super(label);
			}
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("weekChange")) {
					if (getPreviousWeek() < calendar.get(Calendar.WEEK_OF_YEAR)) {
						getSelf().setEnabled(false);
					} else {
						getSelf().setEnabled(true);
					}
				}
			}
			
			public JButton getSelf() {
				return this;
			}
			
		}
	}


	public Controller getCtrl() {
		return ctrl;
	}
}
