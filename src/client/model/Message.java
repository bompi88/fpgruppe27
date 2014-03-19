package model;

import java.sql.Date;
import java.sql.Timestamp;

import framework.Model;

import model.Meeting;
import model.Participant;

public class Message extends Model {
	
	private String type;
	private Timestamp time; 
	private String message;
	private Meeting meeting;
	private boolean isSeen;
	private Participant userInQuestion; 
	private Participant messageOwner; 
	private int messid;  

//	protected String userHasConfirmedMessage = "Har bekreftet m??????teinkallingen til" + meeting.getMeetingName();   
//	protected String userHasDeclinedMessage = "Har medlt avbud til" + meeting.getMeetingName();
	
	
	public Message(Meeting meeting, String type, Participant messageOwner, Participant userInQuestion) {
		super();
		this.meeting = meeting;
		this.type = type; 	
		this.userInQuestion = userInQuestion; 
		this.messageOwner = messageOwner; 
	}
	
	public int getMessID(){
		return messid; 
	}
	
	public Date getDate() {
		Date date = new Date(time.getTime());
		return date;
	}
	
	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}
	
	public Meeting getMeeting() {
		return meeting;
	}
	
	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}
	
	public boolean isSeen() {
		return isSeen;
	}
	
	public void setSeen(boolean isSeen){
		this.isSeen = isSeen;
	}
}