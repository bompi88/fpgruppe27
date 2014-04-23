package view;

import java.awt.Dimension;
import javax.swing.JFrame;

import resources.AppConstants;

@SuppressWarnings("serial")
public class MainView extends JFrame {
	
	private Dimension windowSize = new Dimension(AppConstants.MAIN_FRAME_WIDTH, AppConstants.MAIN_FRAME_HEIGHT);
	
	public MainView() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(windowSize);
		setSize(windowSize);
	
		setVisible(true);
		
		// Center frame
		setLocationRelativeTo(null);
	}
}
