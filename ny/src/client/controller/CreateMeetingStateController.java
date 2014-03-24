package controller;


import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import net.sourceforge.jdatepicker.DateModel;

import database.ClientObjectFactory;

import view.CreateMeetingView;
import model.Meeting;
import model.Participant;
import model.Status;
import framework.State;

public class CreateMeetingStateController implements State {
	
	private CalendeerClient context;
	private Meeting meetingModel;
	private CreateMeetingView createMeetingView;
	
	private Calendar cal = Calendar.getInstance();
	
	public CreateMeetingStateController(CalendeerClient context, Meeting meetingModel, CreateMeetingView createMeetingView) {
		this.context = context;
		this.meetingModel = meetingModel;
		this.createMeetingView = createMeetingView;
		
		
		setActionListeners();
	}
	
	/**
	 * Checks if input fields have the right values
	 * @return
	 */
	public boolean checkInput() {
		
		Date from = getFromTimeAsDate();
		Date to = getToTimeAsDate();
		
		// check if fields is filled in
		if(!createMeetingView.getDescription().equals("") && (!createMeetingView.getPlace().equals("") || createMeetingView.getRoom().getRoomID() > 0) && !createMeetingView.getMeetingName().equals("")) {
			if(createMeetingView.getToDate() != null && createMeetingView.getFromDate() != null && createMeetingView.getFromTime() != null && createMeetingView.getToTime() != null) {
				if(to.compareTo(from) >= 0) {
					return true;
				}	
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the time as a Date, which is selected in the "From time and date pickers".
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getFromTimeAsDate() {
		
		DateModel date = createMeetingView.getFromDate();
		Date time = createMeetingView.getFromTime();
		
		cal.set(Calendar.YEAR, date.getYear());
		cal.set(Calendar.MONTH, date.getMonth());
		cal.set(Calendar.DATE, date.getDay());
		cal.set(Calendar.HOUR_OF_DAY, time.getHours());
		cal.set(Calendar.MINUTE, time.getMinutes());
		
		return cal.getTime();
	}
	
	/**
	 * Gets the time as a Date, which is selected in the "To time and date pickers".
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getToTimeAsDate() {
		
		DateModel date = createMeetingView.getToDate();
		Date time = createMeetingView.getToTime();
		
		cal.set(Calendar.YEAR, date.getYear());
		cal.set(Calendar.MONTH, date.getMonth());
		cal.set(Calendar.DATE, date.getDay());
		cal.set(Calendar.HOUR_OF_DAY, time.getHours());
		cal.set(Calendar.MINUTE, time.getMinutes());
		
		return cal.getTime();
	}
	
	/**
	 * Gets the time as a SQL Timestamp, which is selected in the "To time and date pickers".
	 * @return
	 */
	public Timestamp getToTimeAsTimestamp() {
		return new Timestamp(getToTimeAsDate().getTime());
	}
	
	/**
	 * Gets the time as a SQL Timestamp, which is selected in the "From time and date pickers".
	 * @return
	 */
	public Timestamp getFromTimeAsTimestamp() {
		return new Timestamp(getFromTimeAsDate().getTime());
	}
	
	public void saveModel() {
		meetingModel.post();
	}
	
	public void updateModel() {
		// set textual fields
		meetingModel.setName(createMeetingView.getMeetingName());
		meetingModel.setPlace(createMeetingView.getPlace());
		meetingModel.setDescription(createMeetingView.getDescription());
		meetingModel.setRoom(createMeetingView.getRoom());
		
		// set time
		meetingModel.setStartTime(getFromTimeAsTimestamp());
		meetingModel.setEndTime(getToTimeAsTimestamp());
		
		// set the responsible of meeting
		meetingModel.setResponsible(CalendeerClient.getCurrentEmployee());
		meetingModel.setParticipants(createMeetingView.getParticipants());
	}
	
	public void setActionListeners() {
		
		createMeetingView.addSaveButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				updateModel();
				
				Meeting meeting = getModel();
				
				if(checkInput()) {
					
					// if we have participants, then set meeting as an appointment
					if(meeting.getParticipants().size() > 0) {
						meeting.setAppointment(true);
					} else {
						meeting.setAppointment(false);
					}
					
					// create meeting in database and go to CalendarView
					meeting.post();
					
					getContext().updateAll();
					getContext().setState(getContext().getCalendarState());
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(),"Vennligst fyll inn alle feltene.", "Mangler informasjon", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		createMeetingView.addCancelButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// set a new empty meeting and go to CalendarView
				getModel().setModel(new Meeting());
				getContext().setState(getContext().getCalendarState());	
			}
		});
		createMeetingView.addAddParticipantButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Participant m = createMeetingView.getParticipant();
				if (!m.getUsername().equals(CalendeerClient.getCurrentEmployee().getUsername())) {
					m.setStatus(Status.INVITED);
					createMeetingView.addParticipant(m);
				}
			}
		});
		
		createMeetingView.addRemoveParticipantButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Participant m = createMeetingView.getSelectedParticipant();
				createMeetingView.removeParticipant(m);
				ClientObjectFactory.deleteParticipant(m.getUsername(),m.getMeetid());
			}
		});
		
		createMeetingView.addAddExternalParticipantButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop;
				if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
				
					URI mailto;
					
					try {
						mailto = new URI("mailto:ola@nordmann.no?subject=Moteinvitasjon&body=Du%20er%20invitert%20til%20mote");
						desktop.mail(mailto);
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}
	
	public Meeting getModel() {
		return meetingModel;
	}
	
	public void setMeetingModel(Meeting meeting) {
		createMeetingView.setModel(meeting);
	}
	
	public CalendeerClient getContext() {
		return context;
	}
	
	@Override
	public void hideState() {
		createMeetingView.setVisible(false);

	}

	@Override
	public void showState() {
		createMeetingView.setVisible(true);

	}

	@Override
	public void updateState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initState() {
		// TODO Auto-generated method stub

	}

}
