package model;

import database.ClientObjectFactory;

public class Employee {

	private String username;
	private String name;
	private String email;
	private String password;
	
	public Employee() {
		this.username = "";
		this.name = "";
		this.email = "";
		this.password = "";
	}

	public Employee(String name, String username, String email, String password) {
		super();
		this.username = username;
		this.name = name;
		this.email = email;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean authenticate() {
		return ClientObjectFactory.authenticateWithHash(this);
	}
	
	public void setModel(Employee emp) {
		name = emp.getName();
		email = emp.getEmail();
		password = emp.getPassword();
		username = emp.getUsername();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Employee)) {
			return false;
		}
		return (getUsername().equals(((Employee)obj).getUsername()));
	}
	
	public int hashCode() {
		return getUsername().hashCode();
	}
}
