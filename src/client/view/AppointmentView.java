package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import controller.CalendarCtrl;
import database.ClientObjectFactory;

import resources.AppConstants;
import utils.RelativeLayout;

import model.Meeting;
import model.Participant;

import framework.Observable;
import framework.Observer;


@SuppressWarnings("serial")
public class AppointmentView extends JPanel implements Observable {
	
	private List<Observer> observers = new ArrayList<Observer>();
	private AppointmentPanel app;
	private ParticipantPanel part;
	private TitlePanel titlePanel;
	private JPanel mainWrapper;
	private Meeting meetingModel;
	private HashSet<Participant> participantModel = new HashSet<Participant>();
	private JButton saveButton, cancelButton;
	
	public AppointmentView(Meeting model) {
		
		this.meetingModel = model;
		
		meetingModel.addPropertyChangeListener(app);

		app = new AppointmentPanel(meetingModel);
		part = new ParticipantPanel(participantModel);

		titlePanel = new TitlePanel();
		saveButton = new JButton(AppConstants.SAVE_BUTTON_TEXT);
		cancelButton = new JButton(AppConstants.CANCEL_BUTTON_TEXT);
		
		// holds both appointmentPanel and participantPanel
		mainWrapper = new JPanel();
		mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.add(app);
		mainWrapper.add(part);
		
		addUIElements();
		
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				app.saveModel();
				
				if(app.checkInput()) {
					
					// if we have participants, then set meeting as an appointment
					if(participantModel.size() > 0) {
						meetingModel.setAppointment(true);
					} else {
						meetingModel.setAppointment(false);
					}
					
					// add participants to meeting model
					meetingModel.setParticipants(participantModel);
					
					// create meeting in database and go to CalendarView
					ClientObjectFactory.addMeeting(meetingModel);
					fireObserverEvent("change_state", CalendarCtrl.class);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(),"Vennligst fyll inn alle feltene.", "Mangler informasjon", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// set a new empty meeting and go to CalendarView
				meetingModel.setModel(new Meeting());
				fireObserverEvent("change_state", CalendarCtrl.class);		
			}
		});
	}
	
	public void setMeeting(int id) {
		meetingModel.setModel(ClientObjectFactory.getMeetingByID(id));
	}

	public void setMeetingModel(Meeting m) {
		meetingModel.setModel(m);
	}
	
	public void resetView() {
		meetingModel.setModel(new Meeting());
	}
	
	public Meeting getMeetingModel() {
		return meetingModel;
	}
	
	private void addUIElements() {
		
		// set layout
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		
		// add elements
		add(titlePanel);
		add(mainWrapper);
		add(saveButton);
		add(cancelButton);
	}
	
	/**
	 * Where the title of the screen is.
	 * @author bompi88
	 */
	class TitlePanel extends JPanel {
		
		private JLabel title;
		
		public TitlePanel() {
			
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			
			title = new JLabel(AppConstants.APPOINTMENT_HEADER_TEXT);
			title.setFont(new Font("Arial", Font.PLAIN, 28));
			
			add(title);
			
			setBackground(AppConstants.HEADER_BG_COLOR);
			setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		}
	}

	@Override
	public void addObserver(Observer ob) {
		observers.add(ob);
	}

	@Override
	public void fireObserverEvent(String event, Object obj) {
		for (Observer o : observers)
			o.changeEvent(event, obj);
	}
}
