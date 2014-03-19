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

import resources.AppConstants;

import controller.AppointmentCtrl;
import controller.CalendarCtrl;
import controller.MainCtrl;
import database.ClientObjectFactory;

import model.Meeting;
import model.Participant;
import model.Status;
import utils.RelativeLayout;
import view.AppointmentView.TitlePanel;
import framework.Controller;
import framework.Observable;
import framework.Observer;

public class ViewAppointmentView extends JPanel implements Observable {
	
	private Controller ctrl;
	private List<Observer> observers = new ArrayList<Observer>();
	private ViewAppointmentPanel app;
	private ViewParticipantPanel part;
	private TitlePanel titlePanel;
	private JPanel mainWrapper;
	private JPanel buttonWrapper;
	private Meeting meetingModel;
	private HashSet<Participant> participantModel = new HashSet<Participant>();
	protected JButton goBackButton;
	protected static JButton acceptButton;
	protected static JButton declineButton;
	
	public ViewAppointmentView(Meeting model) {
		this.meetingModel = model;
		
		app = new ViewAppointmentPanel(meetingModel);
		part = new ViewParticipantPanel(participantModel);
		
		meetingModel.addPropertyChangeListener(app);
		meetingModel.addPropertyChangeListener(part);
		
		titlePanel = new TitlePanel();
		goBackButton = new JButton("Tilbake");
		acceptButton = new JButton("Godta invitasjon");
		declineButton = new JButton("Avvis invitasjon");
		
		mainWrapper = new JPanel();
		buttonWrapper = new JPanel();
		buttonWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.add(app);
		mainWrapper.add(part);
				
		addUIElements();
		
		goBackButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// set a new empty meeting and go to CalendarView
				meetingModel.setModel(new Meeting());
				fireObserverEvent("change_state", CalendarCtrl.class);	
				
			}
			
		});
		
		acceptButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//fireObserverEvent("accepted", meetingModel);
				

				String ourUser = MainCtrl.getCurrentEmployee().getName();
				List<Participant> participants = meetingModel.getParticipants();
				for(Participant part : participants) {
					if(part.getName().equals(ourUser)) {
						part.setStatus(Status.ATTENDING);
						meetingModel.setParticipants(participants);
						ClientObjectFactory.setAttandence(meetingModel, MainCtrl.getCurrentEmployee(), "ATTENDING");
						
						
					}
				}
				
			}
		});
		
		declineButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//fireObserverEvent("declined", meetingModel);
				String ourUser = MainCtrl.getCurrentEmployee().getName();
				List<Participant> participants = meetingModel.getParticipants();
				for(Participant part : participants) {
					if(part.getName().equals(ourUser)) {
						part.setStatus(Status.DECLINED);
						meetingModel.setParticipants(participants);
						ClientObjectFactory.setAttandence(meetingModel, MainCtrl.getCurrentEmployee(), "DECLINED");
						
						
					}
				}
				
			}
		});
	}
	
	public void setModel(Meeting m) {
		meetingModel.setName(m.getName());
		meetingModel.setDescription(m.getDescription());
		meetingModel.setStartTime(m.getStartTime());
		meetingModel.setEndTime(m.getEndTime());
		meetingModel.setPlace(m.getPlace());
		meetingModel.setParticipants(m.getParticipants());
		meetingModel.setMeetid(m.getMeetid());
		meetingModel.setResponsible(m.getResponsible());
		meetingModel.setRoom(m.getRoom());
		titlePanel.title.setText(meetingModel.getName());
		titlePanel.title.repaint();

		
	}
	
	public Meeting getModel() {
		return meetingModel;
	}
	
	public void setMeetingModel(Meeting m) {
		meetingModel.setName(m.getName());
		meetingModel.setDescription(m.getDescription());
		meetingModel.setStartTime(m.getStartTime());
		meetingModel.setEndTime(m.getEndTime());
		meetingModel.setPlace(m.getPlace());
		meetingModel.setParticipants(m.getParticipants());
		meetingModel.setMeetid(m.getMeetid());
		meetingModel.setResponsible(m.getResponsible());
		meetingModel.setRoom(m.getRoom());
		titlePanel.title.setText(meetingModel.getName());
		titlePanel.title.repaint();
		
	}
	
	public Meeting getMeetingModel() {
		return meetingModel;
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
	class TitlePanel extends JPanel {
		protected JLabel title;
		public TitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			title = new JLabel(meetingModel.getName());
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
	
	public Controller getCtrl() {
		return ctrl;
	}

}
