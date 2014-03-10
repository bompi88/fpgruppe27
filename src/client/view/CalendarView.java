package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.CalendarCtrl;

import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class CalendarView extends JPanel {
	
	float titleBarScaleWidth = 0.6f;
	
	private JPanel topPanelWrapper = new JPanel();
	private JLabel whereLabel = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
	private JPanel weeklyCalendarWrapper = new JPanel();
	
	public CalendarView() {
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		
		RelativeLayout rl2 = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl2.setAlignment(RelativeLayout.LEADING);
		
		topPanelWrapper.setLayout(rl2);
		topPanelWrapper.setBackground(Color.gray);
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
	
	public class AddCalendarPanel extends JPanel {
		
		public AddCalendarPanel() {
			
		}
		
		@Override
		public Container getParent() {
			// TODO Auto-generated method stub
			return super.getParent();
		}
		
		public void fillSizeOfParent() {
			setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - titleBarScaleWidth)/2)+1, getParent().getPreferredSize().height));
		}
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
	
	public class CalendarCtrlPanel extends JPanel {
		
		public CalendarCtrlPanel() {
			
			JButton nextButton = new JButton(">>");
			
			RelativeLayout rl = new RelativeLayout(RelativeLayout.FIRST, 0);
			rl.setAlignment(RelativeLayout.TRAILING);
			setLayout(rl);
			
			add(nextButton);
		}
		
		@Override
		public Container getParent() {
			// TODO Auto-generated method stub
			return super.getParent();
		}
		
		public void fillSizeOfParent() {
			setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - titleBarScaleWidth)/2)+1, getParent().getPreferredSize().height));
		}
	}
}
