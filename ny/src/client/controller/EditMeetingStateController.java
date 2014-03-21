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

import view.EditMeetingView;
import model.Meeting;
import model.Participant;
import model.Status;
import framework.State;

public class EditMeetingStateController implements State {
	
	private CalendeerClient context;
	private Meeting meetingModel;
	private EditMeetingView editMeetingView;
	
	private Calendar cal = Calendar.getInstance();
	
	public EditMeetingStateController(CalendeerClient context, Meeting meetingModel, EditMeetingView editMeetingView) {
		this.context = context;
		this.meetingModel = meetingModel;
		this.editMeetingView = editMeetingView;
		
		editMeetingView.setModel(meetingModel);
		
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
		if(!editMeetingView.getDescription().equals("") && !editMeetingView.getPlace().equals("") && !editMeetingView.getMeetingName().equals("")) {
			if(editMeetingView.getToDate() != null && editMeetingView.getFromDate() != null && editMeetingView.getFromTime() != null && editMeetingView.getToTime() != null) {
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
		
		DateModel date = editMeetingView.getFromDate();
		Date time = editMeetingView.getFromTime();
		
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
		
		DateModel date = editMeetingView.getToDate();
		Date time = editMeetingView.getToTime();
		
		cal.set(Calendar.YEAR, date.getYear());
		cal.set(Calendar.MONTH, date.getMonth());
		cal.set(Calendar.DATE, date.getDay());
		cal.set(Calendar.HOUR_OF_DAY, time.getHours());
		cal.set(Calendar.MINUTE, time.getMinutes());
		
		System.out.print("s:" + cal.getTime());
		
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
		meetingModel.put();
	}
	
	public void updateModel() {
		// set textual fields
		meetingModel.setName(editMeetingView.getMeetingName());
		meetingModel.setPlace(editMeetingView.getPlace());
		meetingModel.setDescription(editMeetingView.getDescription());
		
		// set time
		meetingModel.setStartTime(getFromTimeAsTimestamp());
		meetingModel.setEndTime(getToTimeAsTimestamp());
		
		// set the responsible of meeting
		meetingModel.setResponsible(CalendeerClient.getCurrentEmployee());
		meetingModel.setParticipants(editMeetingView.getParticipants());
	}
	
	public void setActionListeners() {
		
		editMeetingView.addSaveButtonListener(new ActionListener() {

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
					System.out.println("was here");
					meeting.put();
					
					getContext().updateAll();
					getContext().setState(getContext().getCalendarState());
				}
				else {
					JOptionPane.showMessageDialog(new JFrame(),"Vennligst fyll inn alle feltene.", "Mangler informasjon", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		editMeetingView.addCancelButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// set a new empty meeting and go to CalendarView
				getModel().setModel(new Meeting());
				getContext().setState(getContext().getCalendarState());	
			}
		});
		editMeetingView.addAddParticipantButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Participant m = editMeetingView.getParticipant();
				m.setStatus(Status.INVITED);
				editMeetingView.addParticipant(m);
			}
		});
		
		editMeetingView.addRemoveParticipantButtonListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Participant m = editMeetingView.getParticipant();
				ClientObjectFactory.deleteParticipant(m.getUsername(),m.getMeetid());
				editMeetingView.removeParticipant(m);
			}
		});
		
		editMeetingView.addAddExternalParticipantButtonListener(new ActionListener() {

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
		editMeetingView.setModel(meeting);
	}
	
	public CalendeerClient getContext() {
		return context;
	}
	
	@Override
	public void hideState() {
		editMeetingView.setVisible(false);

	}

	@Override
	public void showState() {
		editMeetingView.setVisible(true);

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
