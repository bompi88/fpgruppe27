package model;

import java.sql.Timestamp;

public class Message {

	private Timestamp time; 
	private String message;
	private int meetid;
	private boolean isSeen;
	private int messid;
	
	public Message() {
		this.time = new Timestamp(System.currentTimeMillis());
		this.message = "";
		this.meetid = 0;
		this.isSeen = false;
		this.messid = 0;
	}
	
	public Message(Timestamp time, String message, int meetid, boolean isSeen,
			int messid) {
		super();
		this.time = time;
		this.message = message;
		this.meetid = meetid;
		this.isSeen = isSeen;
		this.messid = messid;
	}

	public Message(Timestamp time, String message, int meetid, boolean isSeen) {
		super();
		this.time = time;
		this.message = message;
		this.meetid = meetid;
		this.isSeen = isSeen;
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

	public void setMessage(String message) {
		this.message = message;
	}

	public int getMeetid() {
		return meetid;
	}

	public void setMeetid(int meetid) {
		this.meetid = meetid;
	}

	public boolean isSeen() {
		return isSeen;
	}

	public void setSeen(boolean isSeen) {
		this.isSeen = isSeen;
	}

	public int getMessid() {
		return messid;
	}

	public void setMessid(int messid) {
		this.messid = messid;
	}
	
}
