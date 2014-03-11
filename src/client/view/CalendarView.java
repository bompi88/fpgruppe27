package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;
import utils.RelativeLayout;

/**
 * CalendarView class shows weekly calendars which the user has subscribed to.
 */
@SuppressWarnings("serial")
public class CalendarView extends JPanel{
	
	float titleBarScaleWidth = 0.33f;
	
	Calendar calendar = new GregorianCalendar();
	
	private JPanel topPanelWrapper = new JPanel();
	private JPanel weeklyCalendarWrapper = new JPanel();
	
	public CalendarView() {
		
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		
		RelativeLayout rl2 = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl2.setAlignment(RelativeLayout.LEADING);
		
		topPanelWrapper.setLayout(rl2);
		topPanelWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		topPanelWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		weeklyCalendarWrapper.setBackground(Color.white);
		weeklyCalendarWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
																AppConstants.MAIN_FRAME_HEIGHT-AppConstants.HEADER_PANEL_HEIGHT));
		setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
				AppConstants.MAIN_FRAME_HEIGHT));
		
		add(topPanelWrapper);
		add(weeklyCalendarWrapper);
		
		// construct top bar panel
		AddCalendarPanel addCalendarPanel = new AddCalendarPanel();
		CalendarCtrlPanel calendarCtrlPanel = new CalendarCtrlPanel();
		CalendarTitlePanel calendarTitlePanel = new CalendarTitlePanel();
		
		topPanelWrapper.add(addCalendarPanel);
		topPanelWrapper.add(calendarTitlePanel);
		topPanelWrapper.add(calendarCtrlPanel);
		
		addCalendarPanel.fillSizeOfParent();
		calendarCtrlPanel.fillSizeOfParent();
		calendarTitlePanel.fillSizeOfParent();
		
	}
	
	public class WeeklyCalendarPanel extends JPanel {
		
	}
	
	
	public class CalendarTitlePanel extends JPanel {
		
		public CalendarTitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			JLabel title = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
			title.setFont(new Font("Arial", Font.PLAIN, 28));
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
}
