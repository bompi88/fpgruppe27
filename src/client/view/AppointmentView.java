package view;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.CalendarCtrl;

import database.ClientObjectFactory;

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
	private JButton saveButton, cancelButton;
	
	public AppointmentView(Controller ctrl) {
		
		this.ctrl = ctrl;
		//ctrl.setModel(new Meeting());
		app = new AppointmentPanel(ctrl);
		part = new ParticipantPanel();
		model = ctrl.getModel();
		titlePanel = new TitlePanel();
		saveButton = new JButton("Lagre");
		mainWrapper = new JPanel();
		mainWrapper.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 0));
		mainWrapper.add(app);
		mainWrapper.add(part);
		model.addPropertyChangeListener(app);

				
		addUIElements();
		
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(app.checkInput()) {
					model = app.createMeeting(model);
					model.setParticipants(part.getParticipantList());
					System.out.println(model);
					ClientObjectFactory.addMeeting(model);
					getCtrl().setState(CalendarCtrl.class);
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(),"Vennligst fyll inn alle feltene.", "Mangler informasjon", JOptionPane.ERROR_MESSAGE);
				}
				
				
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
	
	public Controller getCtrl() {
		return ctrl;
	}

	private void addUIElements() {
		
		RelativeLayout rl1 = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl1.setAlignment(RelativeLayout.LEADING);
		setLayout(rl1);
		add(titlePanel);
		add(mainWrapper);
		add(saveButton);
	
		
		
	}
	class TitlePanel extends JPanel {
		private JLabel title;
		public TitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			title = new JLabel("Opprett avtale");
			title.setFont(new Font("Arial", Font.PLAIN, 28));
			add(title);
			setBackground(AppConstants.HEADER_BG_COLOR);
			setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		}
	}
}
