package model;

import model.Status;
import model.Employee;

public class Participant extends Employee {
	
	private int meetid;
	private Status status;
	
	public Participant(String name, String username, String email,
			String password, Status declined) {
		super();
		setName(name);
		setUsername(username);
		setEmail(email);
		setPassword(password);
		
		this.status = declined;
	}
	
	public Participant() {}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return getName();
	}

	public int getMeetid() {
		return meetid;
	}

	public void setMeetid(int meetid) {
		this.meetid = meetid;
	}
}
