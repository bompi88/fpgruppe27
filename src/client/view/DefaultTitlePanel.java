package view;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;

@SuppressWarnings("serial")
public class DefaultTitlePanel extends JPanel {
		
	private JLabel title;
	
	public DefaultTitlePanel() {
		
		setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
		
		title = new JLabel(AppConstants.APPOINTMENT_HEADER_TEXT);
		title.setFont(new Font("Arial", Font.PLAIN, 28));
		
		add(title);
		
		setBackground(AppConstants.HEADER_BG_COLOR);
		setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
	}
	
	public void setTitle(String title) {
		this.title.setText(title);
		this.title.repaint();
	}
	
	public String getTitle() {
		return title.getText();
	}
}
