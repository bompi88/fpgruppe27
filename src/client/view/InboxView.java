package view;

import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;

@SuppressWarnings("serial")
public class InboxView extends JPanel {
	private JLabel whereLabel = new JLabel(AppConstants.INBOX_HEADER_TEXT);
	
	public InboxView() {
		add(whereLabel);
	}
}
