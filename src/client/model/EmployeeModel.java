package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import framework.Model;

public class EmployeeModel extends Model{
	
	protected String username = "";
	protected String password;
	protected String name;
	protected String email;
	protected Calendar calendar;
	protected ArrayList<MessageModel> inbox;
	protected int noOfUnreadMessages; 
	
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
	public void fetch() throws ClassNotFoundException, SQLException {
		
		String query = String.format("Select username, password from employee where username='%s'", username);
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);

		while(rs.next())
		{
			this.username = rs.getString("username");
			this.password = rs.getString("password");
		}
		
		rs.close();
		db.close();
		
	}
	
	public EmployeeModel fetch(String id) throws ClassNotFoundException, SQLException {
		
		String query = String.format("Select username, password from employee where username='%s'", id);
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);

		while(rs.next())
		{
			this.username = rs.getString("username");
			this.password = rs.getString("password");
		}
		
		rs.close();
		db.close();
		return this;
	}
	
	public void fetchByUserName(String id) throws ClassNotFoundException, SQLException {
		
		String query = String.format("Select username, password from employee where username='%s'",id);
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);
		
		while(rs.next())
		{
			this.username = rs.getString("username");
			this.password = rs.getString("password");
		}
		
		rs.close();
		db.close();
	}	
	
	public boolean authenticate() throws ClassNotFoundException, SQLException {
		
		if (username != null && password != null && !password.equals("") && !username.equals("")) {
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
	
	
	public void fetchMessages() throws ClassNotFoundException, SQLException{  //kjores ved oppstart av programmet. Opretter f���rste MessageModelobjekt , sletter gamle meldinger, og lager nye objekter til inbox basert p��� DB.   
		MessageModel firstMessage = new MessageModel("none", null, null);  
		Timestamp starttime = new Timestamp(0); 
		
		firstMessage.deleteOldMess(); 
		firstMessage.fetchMessData(username, starttime);
		inbox.add(firstMessage); 
		
		int noOfMessages = firstMessage.countMessages(username, firstMessage.getTime()); 
		
		for(int i = 1; i < noOfMessages; i++){
			MessageModel Message = new MessageModel("none", null, null);
			Message.fetchMessData(username, inbox.get(0).getTime()); 
			inbox.add(0, Message); 				
		}	
		
	} 
	
	public void updateInbox() throws ClassNotFoundException, SQLException{ // kj���rer kontinuerlig. Henter TimeStamp fra siste melding, og ser etter nye meldinger ��� lage objekter av, 
		MessageModel lastMessage = inbox.get(0); 
		int noOfNewMessages = lastMessage.countMessages(username, lastMessage.getTime()); 
		
		if(noOfNewMessages > 0){
			for(int i = 0; i < noOfNewMessages; i++){
				MessageModel newMessage = new MessageModel("none", null, null);
				newMessage.fetchMessData(username, inbox.get(0).getTime()); 
				inbox.add(0, newMessage);
			}
		}
		
	}	
	
	public int getNoOfUnseenMess(){ 	
		int returnvalue = 0; 
		for (int i = 0; i < inbox.size(); i ++){
			if (inbox.get(i).isSeen() == false){
				returnvalue++; 
			}
		}
		return returnvalue; 
	}
	
	
	public void setAllMessagesSeen() throws ClassNotFoundException, SQLException{
		for (int i = 0; i < inbox.size(); i ++){
			inbox.get(i).setSeen(true); 
		}
	}
	

	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof EmployeeModel)) {
			return false;
		}
		return (username.equals(((EmployeeModel)obj).username));
	}
	
	public int hashCode() {
		return username.hashCode();
	}
}
