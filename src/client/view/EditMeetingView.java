package view;

import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.sourceforge.jdatepicker.DateModel;

import model.Meeting;
import model.Participant;
import model.Room;

import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class EditMeetingView extends JPanel {

	private EditMeetingPanel meetingPanel;
	private EditParticipantPanel participantPanel;
	private DefaultTitlePanel titlePanel;
	private JPanel mainWrapper;
	private JButton saveButton, cancelButton;
	
	public EditMeetingView() {
		
		// set views
		this.meetingPanel = new EditMeetingPanel();
		this.participantPanel = new EditParticipantPanel();
		this.titlePanel = new DefaultTitlePanel();
		this.mainWrapper = new JPanel();
		this.saveButton = new JButton(AppConstants.SAVE_BUTTON_TEXT);
		this.cancelButton = new JButton(AppConstants.CANCEL_BUTTON_TEXT);
		
		// holds both appointmentPanel and participantPanel
		this.mainWrapper = new JPanel();
		this.mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		
		// set layout
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		setTitle(AppConstants.EDIT_APPOINTMENT_HEADER_TEXT);
		// add elements
		add(titlePanel);
		mainWrapper.add(meetingPanel);
		mainWrapper.add(participantPanel);
		add(mainWrapper);
		add(saveButton);
		add(cancelButton);
	}
	
	public void addSaveButtonListener(ActionListener saveButtonListener) {
		saveButton.addActionListener(saveButtonListener);
	}
	
	public void addCancelButtonListener(ActionListener CancelButtonListener) {
		cancelButton.addActionListener(CancelButtonListener);
	}
	
	public void addAddExternalParticipantButtonListener(ActionListener listener) {
		participantPanel.addAddExternalParticipantButtonListener(listener);
	}
	
	public void addRemoveParticipantButtonListener(ActionListener listener) {
		participantPanel.addRemoveParticipantButtonListener(listener);
	}
	
	public void addAddParticipantButtonListener(ActionListener listener) {
		participantPanel.addAddParticipantButtonListener(listener);
	}
	
	public void setTitle(String title) {
		titlePanel.setTitle(title);
	}
	
	public String getTitle() {
		return titlePanel.getTitle();
	}
	
	public void setModel(Meeting meeting) {
		meetingPanel.setModel(meeting);
		participantPanel.setModel(meeting.getParticipants());
	}
	
	public String getMeetingName() {
		return meetingPanel.getMeetingName();
	}
	
	public String getPlace() {
		return meetingPanel.getPlace();
	}
	
	public String getDescription() {
		return meetingPanel.getDescription();
	}
	
	public DateModel getFromDate() {
		return meetingPanel.getFromDate();
	}
	
	public DateModel getToDate() {
		return meetingPanel.getToDate();
	}
	
	public Date getFromTime() {
		return meetingPanel.getFromTime();
	}
	
	public Date getToTime() {
		return meetingPanel.getToTime();
	}
	
	public Room getRoom() {
		return meetingPanel.getRoom();
	}
	
	public List<Participant> getParticipants() {
		return participantPanel.getParticipants();
	}
	
	public Participant getParticipant() {
		return participantPanel.getParticipant();
	}
	
	public Participant getSelectedParticipant() {
		return participantPanel.getSelectedParticipant();
	}
	
	public void addParticipant(Participant p) {
		participantPanel.addParticipant(p);
	}
	
	public void removeParticipant(Participant p) {
		participantPanel.removeParticipant(p);
	}
	
	public String getEmailList() {
		return participantPanel.getEmailField();
	}
}
