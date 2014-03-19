package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import resources.AppConstants;
import resources.ImageManager;
import utils.RelativeLayout;

import model.Employee;

import controller.AppointmentCtrl;
import controller.CalendarCtrl;
import controller.InboxCtrl;
import controller.MainCtrl;
import framework.Controller;
import framework.Observer;

@SuppressWarnings("serial")
public class SideBarView extends JPanel implements Observer {

	private Dimension sidebarSize = new Dimension(150,800);
	
	private JButton calendarButton = new JButton(AppConstants.SIDE_BAR_CALENDAR_TEXT);
	private JButton createEventButton = new JButton(AppConstants.SIDE_BAR_CREATE_EVENT_TEXT);
	private JButton inboxButton = new JButton(AppConstants.SIDE_BAR_INBOX_TEXT);
	private JButton logoutButton = new JButton(AppConstants.SIDE_BAR_LOGOUT_TEXT);
	private JLabel appIcon = new JLabel("");
	private JLabel loggedInUser = new JLabel("");
	
	private Controller ctrl;
	
	public SideBarView (Controller ctrl) {
		this.ctrl = ctrl;
		
		ImageManager.getInstance();
		appIcon.setIcon(ImageManager.getAppIcon());
		loggedInUser.setPreferredSize(new Dimension(120,90));
		loggedInUser.setFont(new Font("Ariel", Font.PLAIN, 12));

		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.CENTER);
		
		appIcon.setPreferredSize(new Dimension(120,90));
		
		add(appIcon);
		add(loggedInUser);
		add(calendarButton);
		add(createEventButton);
		add(inboxButton);
		add(logoutButton);
		
		setLayout(rl);
		setPreferredSize(sidebarSize);
		setBorder(new EmptyBorder(10,0,0,0));
		setBackground(AppConstants.SIDE_BAR_BG_COLOR);
		
		setVisible(true);
		
		
		calendarButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((MainCtrl)getCtrl()).setState(CalendarCtrl.class);
			}
			
		});
		
		createEventButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((MainCtrl)getCtrl()).setState(AppointmentCtrl.class);
			}
			
		});
		
		inboxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((MainCtrl)getCtrl()).setState(InboxCtrl.class);
			}
			
		});
		
		logoutButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((MainCtrl)getCtrl()).logout();
			}
			
		});
	}
	
	public Controller getCtrl() {
		return ctrl;
	}
	
	public void setNumberOfUnseenMessages(int count) {
		inboxButton.setText(AppConstants.SIDE_BAR_INBOX_TEXT + " (" + count + ")");
	}

	public void init() {
		loggedInUser.setText("<html><b>user:</b> " + ((Employee)((MainCtrl)ctrl.getMainCtrl()).getModel()).getUsername() + "</html>");
	}

	@Override
	public void changeEvent(String event, Object obj) {
		if (event.equals("inboxSeen")) {
			setNumberOfUnseenMessages(0);
		}
	}
}