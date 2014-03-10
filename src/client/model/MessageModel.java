package model;

import java.sql.SQLException;
import java.sql.Date;

import framework.Model;

public class MessageModel extends Model {
	
	protected Date date;
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	protected String message;
	protected MeetingModel meeting;
	protected boolean isSeen;

	@Override
	public void create() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
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
