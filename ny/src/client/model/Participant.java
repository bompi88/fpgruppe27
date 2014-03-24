package model;

public class Participant extends Employee {

	private int meetid;
	private Status status;
	
	public Participant() {
		super();
		this.meetid = 0;
		this.status = Status.INVITED;
	}

	public Participant(String username, String name, String email,
			String password, int meetid, Status status) {
		super(username, name, email, password);
		
		this.meetid = meetid;
		this.status = status;
	}

	public Participant(String username, String name, String email,
			String password, Status status) {
		super(name, username, email, password);
		
		this.status = status;
	}

	public Participant(int meetid, Status status) {
		super();
		this.meetid = meetid;
		this.status = status;
	}
	
	public Participant(Employee emp, int meetid, Status status) {
		super();
		this.meetid = meetid;
		this.status = status;
		setName(emp.getName());
		setEmail(emp.getEmail());
		setUsername(emp.getUsername());
		setPassword(emp.getPassword());
	}

	public int getMeetid() {
		return meetid;
	}

	public void setMeetid(int meetid) {
		this.meetid = meetid;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Participant)) {
			return false;
		}
		return (getUsername().equals(((Participant)obj).getUsername()));
	}
	
	public int hashCode() {
		return getUsername().hashCode();
	}
}
