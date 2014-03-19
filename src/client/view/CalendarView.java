package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import framework.Controller;

import model.Employee;
import resources.AppConstants;
import utils.RelativeLayout;

/**
 * CalendarView class shows weekly calendars which the user has subscribed to.
 */
@SuppressWarnings("serial")
public class CalendarView extends JPanel{

	public static float titleBarScaleWidth = 0.33f;

	private JPanel topPanelWrapper = new JPanel();
	private JPanel weeklyCalendarWrapper = new JPanel();
	private AddCalendarPanel addCalendarPanel;
	private WeeklyCalendarPanel weeklyCalendarPanel;
	private JScrollPane scrollPane;

	private Controller ctrl;

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
		// construct top bar panel
		addCalendarPanel = new AddCalendarPanel();
		CalendarCtrlPanel calendarCtrlPanel = new CalendarCtrlPanel();
		CalendarTitlePanel calendarTitlePanel = new CalendarTitlePanel();

		weeklyCalendarPanel = new WeeklyCalendarPanel();

		weeklyCalendarWrapper.add(weeklyCalendarPanel);

		scrollPane = new JScrollPane(weeklyCalendarWrapper,
		        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
		        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		scrollPane.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
				AppConstants.MAIN_FRAME_HEIGHT-AppConstants.HEADER_PANEL_HEIGHT - 20));

		scrollPane.revalidate();
		scrollPane.setBackground(AppConstants.CALENDAR_BG_COLOR);

		add(scrollPane);

		topPanelWrapper.add(addCalendarPanel);
		topPanelWrapper.add(calendarTitlePanel);
		topPanelWrapper.add(calendarCtrlPanel);

		addCalendarPanel.fillSizeOfParent();
		calendarCtrlPanel.fillSizeOfParent();
		calendarTitlePanel.fillSizeOfParent();

		weeklyCalendarPanel.getMeetingsFromDB();

	}

	public void update() {
		weeklyCalendarPanel.getMeetingsFromDB();
	}

	public List<Employee> getAllSubscriptions() {
		return addCalendarPanel.getAllSubscriptions();
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

	public Controller getCtrl() {
		return ctrl;
	}
}