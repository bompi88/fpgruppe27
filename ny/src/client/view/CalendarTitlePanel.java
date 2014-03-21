package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;

@SuppressWarnings("serial")
public class CalendarTitlePanel extends JPanel {
	
	private JLabel title;
	private Font titleFont;

	public CalendarTitlePanel() {
		this.titleFont = new Font("Arial", Font.PLAIN, 28);
		this.title = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
		
		title.setFont(titleFont);
		
		setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
		setBackground(AppConstants.HEADER_BG_COLOR);
		
		add(title);
	}

	@Override
	public Container getParent() {
		return super.getParent();
	}

	public void fillSizeOfParent() {
		setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * AppConstants.HEADER_TITLE_PANEL_SCALE_WIDTH)+1, getParent().getPreferredSize().height));
	}
}
