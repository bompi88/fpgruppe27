package model;

import framework.Model;

public class Employee extends Model {

	private String username;
	private String name;
	private String email;
	private String password;

	
	public Employee(String name, String username, String email, String password) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public Employee() {
		super();
	}	
	
	public Employee(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public Employee(String username) {
		super();
		this.username = username;
	}

	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Employee)) {
			return false;
		}
		return (username.equals(((Employee)obj).username));
	}
	
	public int hashCode() {
		return username.hashCode();
	}

	@Override
	public String toString() {
		return "Employee [username=" + username + ", name=" + name + ", email="
				+ email + ", password=" + password + "]";
	}
}
