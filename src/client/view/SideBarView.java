package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import resources.AppConstants;

import model.EmployeeModel;

import controller.AppointmentCtrl;
import controller.CalendarCtrl;
import controller.InboxCtrl;
import controller.MainCtrl;
import framework.Controller;

public class SideBarView extends JPanel {
	
	private static final long serialVersionUID = 6009796927471975437L;

	Dimension sidebarSize = new Dimension(150,800);
	
	JLabel userLabel = new JLabel();
	
	JButton calendarButton = new JButton(AppConstants.SIDE_BAR_CALENDAR_TEXT);
	JButton createEventButton = new JButton(AppConstants.SIDE_BAR_CREATE_EVENT_TEXT);
	JButton inboxButton = new JButton(AppConstants.SIDE_BAR_INBOX_TEXT);
	JButton logoutButton = new JButton(AppConstants.SIDE_BAR_LOGOUT_TEXT);
	
	Controller ctrl;
	
	public SideBarView (Controller ctrl) {
		this.ctrl = ctrl;
		
		userLabel.setText(((EmployeeModel)ctrl.getModel()).getUsername());
		
		add(userLabel);
		add(calendarButton);
		add(createEventButton);
		add(inboxButton);
		add(logoutButton);
		setPreferredSize(sidebarSize);
		setBackground(Color.DARK_GRAY); // for testing
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

}
