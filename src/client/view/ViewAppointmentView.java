package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
import database.ClientObjectFactory;

import model.Meeting;
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
	private Meeting model;
	private JButton goBackButton, editButton, acceptButton, declineButton;
	
	public ViewAppointmentView(Controller ctrl) {
		
		this.ctrl = ctrl;
		//ctrl.setModel(new Meeting());
		app = new ViewAppointmentPanel(ctrl);
		part = new ViewParticipantPanel(ctrl);
		model = ctrl.getModel();
		titlePanel = new TitlePanel();
		goBackButton = new JButton("Tilbake");
		editButton = new JButton("Endre");
		acceptButton = new JButton("Godta");
		declineButton = new JButton("Avvis");
		mainWrapper = new JPanel();
		mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.add(app);
		mainWrapper.add(part);
		model.addPropertyChangeListener(app);
		

				
		addUIElements();
		
		if (model.getResponsible() == ctrl.getMainCtrl().getModel()) {
			acceptButton.setVisible(false);
			declineButton.setVisible(false);
			editButton.setVisible(true);
			
		} else {
			editButton.setVisible(false);
			acceptButton.setVisible(true);
			declineButton.setVisible(true);
		}
		
		goBackButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				getCtrl().setState(CalendarCtrl.class);
						
				
			}
			
		});
		
		editButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				getCtrl().setState(AppointmentCtrl.class);
				
			}
		});
		
		acceptButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		declineButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setModel(Meeting m) {
		model.setName(m.getName());
		model.setDescription(m.getDescription());
		model.setStartTime(m.getStartTime());
		model.setEndTime(m.getEndTime());
		model.setPlace(m.getPlace());
		model.setParticipants(m.getParticipants());
		model.setMeetid(m.getMeetid());
		model.setResponsible(m.getResponsible());
		model.setRoom(m.getRoom());
		
	}
	
	public Meeting getModel() {
		return model;
	}
	
	private void addUIElements() {
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		add(titlePanel);
		add(mainWrapper);
		add(goBackButton);
		add(editButton);
		add(acceptButton);
		add(declineButton);
		
	}
	class TitlePanel extends JPanel {
		private JLabel title;
		public TitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			title = new JLabel(model.getName());
			title.setFont(new Font("Arial", Font.PLAIN, 28));
			add(title);
			setBackground(AppConstants.HEADER_BG_COLOR);
			setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		}
	}
	@Override
	public void addObserver(Observer ob) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fireObserverEvent(String event, Object obj) {
		// TODO Auto-generated method stub
		
	}
	
	public Controller getCtrl() {
		return ctrl;
	}

}
