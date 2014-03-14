package view;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

import resources.AppConstants;
import utils.RelativeLayout;

import model.Meeting;

import framework.Controller;


@SuppressWarnings("serial")
public class AppointmentView extends JPanel {
	
	private Controller ctrl;
	private AppointmentPanel app;
	private ParticipantPanel part;
	private TitlePanel titlePanel;
	private JPanel mainWrapper;
	private Meeting model;
	
	public AppointmentView(Controller ctrl) {
		
		this.ctrl = ctrl;
		ctrl.setModel(new Meeting());
		app = new AppointmentPanel(ctrl);
		part = new ParticipantPanel();
		titlePanel = new TitlePanel();
		mainWrapper = new JPanel();
		mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.add(app);
		mainWrapper.add(part);
				
		addUIElements();
		
		
	}
	
	public Controller getCtrl() {
		return ctrl;
	}

	private void addUIElements() {
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		add(titlePanel);
		//add(app);
		//add(part);
		add(mainWrapper);
		
		
	}
	
	private boolean checkInput() {
		return true;
	}
	
	class TitlePanel extends JPanel {
		protected JTextField title;
		public TitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			title = new JTextField("MØTENAVN");
			title.setFont(new Font("Arial", Font.PLAIN, 28));
			add(title);
			setBackground(AppConstants.HEADER_BG_COLOR);
			setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		}
	}
}
