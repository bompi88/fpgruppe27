package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class CalendarView extends JPanel {
	
	private JPanel topPanelWrapper = new JPanel();
	private JLabel whereLabel = new JLabel(AppConstants.CALENDAR_HEADER_TEXT);
	
	public CalendarView() {
		
		RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);
		topPanelWrapper.setLayout(rl);
		topPanelWrapper.setBackground(Color.blue);
		topPanelWrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		topPanelWrapper.setPreferredSize(new Dimension(800,200));
		
		add(topPanelWrapper);
	}
}
