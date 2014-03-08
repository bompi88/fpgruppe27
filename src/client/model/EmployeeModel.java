package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import framework.Model;

public class EmployeeModel extends Model {
	
	protected String username;
	protected String password;
	protected String name;
	
	public EmployeeModel() {}

	@Override
	public void create() throws ClassNotFoundException, SQLException {
		String query=String.format("insert into employee " +
				"(username, password) values ('%s','%s')",username, password); 
		db.initialize();
		db.makeSingleUpdate(query);
		db.close();
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
	public void fetch(String id) throws ClassNotFoundException, SQLException {
		
		String query = String.format("Select username from employee where id=%s",id);
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);

		while(rs.next())
		{
			this.username = rs.getString(1);
		}
		
		db.initialize();
		rs.close();
		db.close();
	}
	
	public boolean authenticate() throws ClassNotFoundException, SQLException {
		
		if (username != null && password != null && !password.equals("") && !username.equals("")) {
			System.out.println(":" + username + ":");
			String query = String.format("Select password from employee where username='%s'", username);
			db.initialize();
			ResultSet rs = db.makeSingleQuery(query);
			
			String tmpPasswd = "";
			
			while(rs.next())
			{
				tmpPasswd = rs.getString(1);
			}
			
			rs.close();
			db.close();
			
			return password.equals(tmpPasswd);
		}
		
		return false;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		String tmp = this.username;
		this.username = username;
		propertyChangeSupport.firePropertyChange("username", tmp, username);
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		String tmp = this.password;
		this.password = password;
		propertyChangeSupport.firePropertyChange("password", tmp, password);
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		String tmp = this.name;
		this.name = name;
		propertyChangeSupport.firePropertyChange("name", tmp, name);
	}
}
