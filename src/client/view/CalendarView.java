package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;

@SuppressWarnings("serial")
public class CalendarView extends JPanel {
	private JLabel whereLabel = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
	
	public CalendarView() {
		add(whereLabel);
	}
}
