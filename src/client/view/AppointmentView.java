package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;

@SuppressWarnings("serial")
public class AppointmentView extends JPanel {
	private JLabel whereLabel = new JLabel(AppConstants.APPOINTMENT_HEADER_TEXT);
	
	public AppointmentView() {
		add(whereLabel);
	}
}
