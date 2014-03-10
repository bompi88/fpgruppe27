package model;

import java.sql.SQLException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import framework.Model;

public class MessageModel extends Model {
	
	
	protected String type; // er  
	protected Date date = new Date(System.currentTimeMillis());
	protected String message;
	protected MeetingModel meeting;
	protected boolean isSeen;
	protected ParticipantModel userInQestion; 
	protected ParticipantModel messageOwner; 
	protected int messid;  
	
	DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss"); 
	Calendar cal = Calendar.getInstance(); 
	DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd"); 
	
	
	
	protected String inviteMessage = "Du har blitt invitert til" + meeting.getMeetingName() + "kl" + meeting.getStartDateAsString() + meeting.getStartDateAsString();
	protected String changeOfTimeMessage = meeting.getMeetingName() + "Har blitt endret. Ny tid er: " + meeting.getStartTimeAsString() + meeting.getStartDateAsString() + "til" + meeting.getEndTimeAsString() + meeting.getEndDateAsString() ; 
	protected String changeOfPlaceMessage = meeting.getMeetingName() + "har blitt flyttet til" + meeting.getPlaceOrRoom();  
	protected String meetingCancledMessage = meeting.getMeetingName() + "har blitt avlyst"; 
	protected String userHasConfirmedMessage = "Har bekreftet møteinkallingen til" + meeting.getMeetingName();   
	protected String userHasDeclinedMessage = "Har medlt avbud til" + meeting.getMeetingName();
	
	
	public MessageModel(String type, ParticipantModel messageOwner, ParticipantModel userInQestion) throws ClassNotFoundException, SQLException{
		this.type = type; 	
		this.userInQestion = userInQestion; 
		setMessage(); 
		create(); 
	}
	
	
	public String setMessage(){
		if (type.equals("meetingCreated")){
			message = inviteMessage;  
		}
		if (type.equals("meetingTimeChanged")){
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
		String query=String.format("insert into message " + "(message, date, timestamp, owner, isSeen) values ('%s','%s','%s','%s', '%s')", message, dateformat.format(cal.getTime()), timeFormat.format(cal.getTime()), messageOwner.getUsername()); 
	}

	@Override
	public void save() throws ClassNotFoundException, SQLException {
		// Unødvendig
		
	}

	@Override
	public void delete() throws ClassNotFoundException, SQLException {
		//skal skje automatisk når lest og etter 5 dager(ish)
		
	}

	@Override
	public void fetch(String userOwner) throws ClassNotFoundException, SQLException { // henter ut meldinger til brukeren. 
		// TODO Auto-generated method stub
		String query=String.format("SELECT message, date, timestamp, isSeen; FROM Message; WHERE owner = userOwner AND this.date < date");    
		
	}

}
