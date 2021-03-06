package view;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import controller.CalendeerClient;

import resources.AppConstants;

import utils.RelativeLayout;
import model.Meeting;
import model.Participant;
import model.Status;

@SuppressWarnings("serial")
public class ViewMeetingView extends JPanel {

	private ViewMeetingPanel app;
	private ViewParticipantPanel part;
	private DefaultTitlePanel titlePanel;
	private JPanel mainWrapper;
	private JPanel buttonWrapper;
	private JButton goBackButton;
	private JButton acceptButton;
	private JButton declineButton;
	
	public ViewMeetingView() {
		
		this.app = new ViewMeetingPanel();
		this.part = new ViewParticipantPanel();
		this.titlePanel = new DefaultTitlePanel();
		this.goBackButton = new JButton(AppConstants.GO_BACK_BUTTON_TEXT);
		this.acceptButton = new JButton(AppConstants.ACCEPT_BUTTON_TEXT);
		this.declineButton = new JButton(AppConstants.DECLINE_BUTTON_TEXT);
		this.mainWrapper = new JPanel();
		this.buttonWrapper = new JPanel();
		
		buttonWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.add(app);
		mainWrapper.add(part);
		
		addUIElements();
	}
	
	public void setMeetingModel(Meeting m) {
		titlePanel.setTitle(m.getName());
		
		acceptButton.setEnabled(false);
		declineButton.setEnabled(false);
		
		List<Participant> parts = m.getParticipants();
		for (Participant part : parts) {
			if (CalendeerClient.getCurrentEmployee().getUsername().equals(part.getUsername())) {
				if(part.getStatus().equals(Status.DECLINED)) {
					acceptButton.setEnabled(true);
				} else if(part.getStatus().equals(Status.ATTENDING)) {
					declineButton.setEnabled(true);
				} else {
					acceptButton.setEnabled(true);
					declineButton.setEnabled(true);
				}
			}
		}
		app.setMeetingModel(m);
		part.setParticipantModel(m.getParticipants());
	}
	
	public Meeting getMeetingModel() {
		return app.getMeetingModel();
	}
	
	private void addUIElements() {
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		
		add(titlePanel);
		add(mainWrapper);
		
		buttonWrapper.add(acceptButton);
		buttonWrapper.add(declineButton);
		buttonWrapper.add(goBackButton);
		add(buttonWrapper);
		
	}

	public void addGoBackButtonListener(ActionListener listener) {
		goBackButton.addActionListener(listener);
	}
	
	public void addAcceptButtonListener(ActionListener listener) {
		acceptButton.addActionListener(listener);
	}
	
	public void addDeclineButtonListener(ActionListener listener) {
		declineButton.addActionListener(listener);
	}
}
