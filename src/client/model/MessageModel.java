package model;

import java.sql.SQLException;
import java.sql.Date;

import framework.Model;

public class MessageModel extends Model {
	
	
	protected String type; // er  
	protected Date date;
	protected String message;
	protected MeetingModel meeting;
	protected boolean isSeen;
	protected ParticipantModel userInQestion; 
	
	
	protected String inviteMessage = "Du har blitt invitert til" + meeting.getName() + "kl" + meeting.getTimeAsString() + meeting.getDateAsString();
	protected String changeOfTimeMessage = meeting.getName() + "Har blitt endret. Ny tid er: " + meeting.getStartTimeAsString() + meeting.getStartDateAsString() + "til" + meeting.getEndTimeAsString() + meeting.getEndDateAsString() ; 
	protected String changeOfPlaceMessage = meeting.getName() + "har blitt flyttet til" meeting.getPlaceOrRoom;  
	protected String meetingCancledMessage = meeting.getName() + "har blitt avlyst"; 
	protected String userHasConfirmedMessage = "Har bekreftet møteinkallingen til" meeting.getName();   
	protected String userHasDeclinedMessage = "Har medlt avbud til" meeting.getName();
	
	
	public MessageModel(String type, ParticipantModel messageOwner, ParticipantModel userInQestion){
		this.type = type; 	
		this.userInQestion = userInQestion; 
		setMessage(); 
	}
	
	
	public String setMessage(){
		if (type.equals("meetingCreated"))){
			message = inviteMessage;  
		}
		if (type.equals("meetingTimeChanged"))){
			message = changeOfTimeMessage;  
		}
		if ((type.equals("placeChanged"))){
			message = changeOfPlaceMessage; 
		}
		if ((type.equals("meetingCancled"))){
			message = meetingCancledMessage; 
		}
		if ((type.equals("partConfrimed"))){ 
			message = userInQestion.getName() + userHasConfirmedMessage; 
		}
		if ((type.equals("partDeclined"))){ 
			message = userInQestion.getName() + userHasDeclinedMessage; 
		}
	
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getMessage() {
		return message;
	}
	
	//public void setMessage(String message) {
		//this.message = message;
	//}
	
	public MeetingModel getMeeting() {
		return meeting;
	}
	
	public void setMeeting(MeetingModel meeting) {
		this.meeting = meeting;
	}
	
	public boolean isSeen() {
		return isSeen;
	}
	
	public void setSeen(boolean isSeen) {
		this.isSeen = isSeen;
	}
	
	
	
	@Override
	public void create() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() throws ClassNotFoundException, SQLException {
		// Unødvendig
		
	}

	@Override
	public void delete() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetch(String o) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

}
