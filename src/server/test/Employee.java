package test;

public class Employee {

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

	public Employee() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public String toString() {
		return "Employee [username=" + username + ", name=" + name + ", email="
				+ email + ", password=" + password + "]";
	}

}
