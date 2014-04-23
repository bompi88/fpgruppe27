package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import database.ClientObjectFactory;
import view.ViewMeetingView;
import model.Meeting;
import model.Participant;
import model.Status;
import framework.State;

public class ViewMeetingStateController implements State {

	private CalendeerClient context;
	private Meeting meetingModel;
	private ViewMeetingView meetingView;
	
	public ViewMeetingStateController(CalendeerClient context, Meeting meetingModel, ViewMeetingView meetingView) {
		this.context = context;
		this.meetingModel = meetingModel;
		this.meetingView = meetingView;
	
		meetingView.addGoBackButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// set a new empty meeting and go to CalendarView
				getContext().setMeetingModel(new Meeting());
				getContext().setState(getContext().getCalendarState());		
			}
		});
		
		meetingView.addAcceptButtonListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String ourUser = CalendeerClient.getCurrentEmployee().getUsername();
				List<Participant> participants = getModel().getParticipants();
				for(int i = 0; i<participants.size(); i++) {
					Participant part = participants.get(i);
					if(part.getUsername().equals(ourUser)) {
						participants.remove(i);
						part.setStatus(Status.ATTENDING);
						participants.add(part);
						getModel().setParticipants(participants);
						ClientObjectFactory.setAttandence(getModel(), CalendeerClient.getCurrentEmployee(), "ATTENDING");
						setMeetingModel(getModel());
					}
				}
			}
		});
		
		meetingView.addDeclineButtonListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//fireObserverEvent("declined", meetingModel);
				String ourUser = CalendeerClient.getCurrentEmployee().getUsername();
				List<Participant> participants = getModel().getParticipants();
				for(int i = 0; i<participants.size(); i++) {
					Participant part = participants.get(i);
					if(part.getUsername().equals(ourUser)) {
						participants.remove(i);
						part.setStatus(Status.DECLINED);
						participants.add(part);
						getModel().setParticipants(participants);
						ClientObjectFactory.setAttandence(getModel(), CalendeerClient.getCurrentEmployee(), "DECLINED");	
						setMeetingModel(getModel());
					}
				}
				
			}
		});
	}
	
	public CalendeerClient getContext() {
		return context;
	}
	
	public Meeting getModel() {
		return meetingModel;
	}
	
	public void setMeetingModel(Meeting meeting) {
		meetingView.setMeetingModel(meeting);
	}
	
	@Override
	public void hideState() {
		meetingView.setVisible(false);
	}

	@Override
	public void showState() {
		meetingView.setVisible(true);
	}

	@Override
	public void initState() {
	}

	@Override
	public void updateState() {
	}

}
