package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class CalendarView extends JPanel {
	
	private JPanel topPanelWrapper = new JPanel();
	private JLabel whereLabel = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
	private JPanel weeklyCalendarWrapper = new JPanel();
	public CalendarView() {
		
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		setLayout(rl);
		rl.setAlignment(RelativeLayout.LEADING);
		topPanelWrapper.setLayout(rl);
		topPanelWrapper.setBackground(Color.blue);
		topPanelWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		weeklyCalendarWrapper.setBackground(Color.green);
		weeklyCalendarWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
																AppConstants.MAIN_FRAME_HEIGHT-AppConstants.HEADER_PANEL_HEIGHT));
		setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH,
				AppConstants.MAIN_FRAME_HEIGHT));
		
		add(topPanelWrapper);
		add(weeklyCalendarWrapper);
	}
}
