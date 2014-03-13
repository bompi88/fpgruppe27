package test;

public class Participant extends Employee {
	
	private Status status;
	
	

	public Participant(String name, String username, String email,
			String password, Status status) {
		super(name, username, email, password);
		this.status = status;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	

}
