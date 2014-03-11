package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;


import framework.Model;

public class MeetingModel extends Model {
	
	protected int meetID;
	protected Date startDate;
	protected Date endDate;	
	protected String description;
	protected Time startTime;
	protected Time endTime;
	protected RoomModel room;
	protected String place;
	protected EmployeeModel responsible;
	protected ArrayList<ParticipantModel> participants;
	protected boolean isAppointment;
	protected String name;
	
	public MeetingModel() {

	}

	public void create() throws ClassNotFoundException, SQLException {
		// add to DB meetID, date description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),      
		// add to DB  meetID, participants, StatusModel to TABLE MeetingParticipants
		
		String query1=String.format( "insert into Meeting" +"(meetid, name, description, startDate, endDate, startTime, endTime, place, roomid, username, isAppointment) values ('%d','%d')",meetID, name, description, startDate, endDate, startTime, endTime, place, room.getRoomID(), responsible.getUsername(), this.isAppiontmentString()); 
		
		ArrayList<String> peopleList  = new ArrayList<String>();
		peopleList.add(query1); 
		
		for (int i = 0; i < participants.size(); i++){
			String tempQuery = String.format("insert into MeetingParticipants" + "(meetid, username, status) values ('%d','%d,'%d')",meetID, participants.get(i).getUsername(), "INVITED"); 
			peopleList.add(tempQuery); 		
		}
		
		db.initialize();	
		for(int i = 0; i < peopleList.size(); i++){
			db.makeSingleUpdate(peopleList.get(i));
		}		
		db.close();
	}

	public void save() throws ClassNotFoundException, SQLException {
		// overwrite to DB date, description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),  
		// remove all old entries in TABLE MeetingParticipants associated with this meeting (meetid = meetID)
		// add to DB meetID, participants, StatusModel to TABLE MeetingParticipants
		
		String query1 = "update Meeting set name = " + name + " , description = " + description + " , startDate = " + startDate + " , endDate = " + endDate + " , startTime = " + startTime + " , endTime = " + endTime + " , place = " + place + " , roomid = " + room.getRoomID() + " , username = " + responsible.getUsername() + " , isAppointment = " + this.isAppiontmentString() + " where meetid = " + meetID;
		
		String query2 = "delete from MeetingParticipants where meetid = " + meetID;
		
		ArrayList<String> queryList = new ArrayList<String>();
		queryList.add(query1);
		queryList.add(query2);
		
		for (int i = 0; i < participants.size(); i++){
			String tempQuery = String.format("insert into MeetingParticipants" + "(meetid, username, status) values ('%d','%d,'%d')",meetID, participants.get(i).getUsername(), participants.get(i).getStatusAsString()); 
			queryList.add(tempQuery); 		
		}
		
		db.initialize();
		for (int i = 0; i < queryList.size(); i++) {
			db.makeSingleUpdate(queryList.get(i));
		}
		db.close();
		
	}

	public void delete() throws ClassNotFoundException, SQLException {
		// remove entry with meetid = meetID in TABLE Meeting      
		// associated entries in TABLE MeetingParticipants will be removed auotomaticly by database
		
		String query1 = "delete from Meeting where meetid = " + meetID;
		
		db.initialize();
		db.makeSingleUpdate(query1);
		db.close();
		
	}

	public void fetch(String meetId) throws ClassNotFoundException, SQLException {
		// fetch and overwrite meetID, date description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),      
		// fetch and overwrite meetID, participants, StatusModel to TABLE MeetingParticipants
		
		String query1 = "select name, description, startDate, endDate, startTime, endTime, place, roomid, username, isAppointment from Meeting where meetid =" + meetId;
		
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query1);
		db.close();
		
		rs.next();
		name = rs.getString("name");
		description = rs.getString("description");
		startDate = rs.getDate("startDate");
		endDate = rs.getDate("endDate");
		startTime = rs.getTime("getTime");
		endTime = rs.getTime("endTime");
		place = rs.getString("place");
		//room og responsible venter paa spesielle metoder
		isAppointment = rs.getBoolean("isAppointment");
	}
	
	// TODO: functionality for adding people 

	public int getMeetID() {
		return meetID;
	}

	public void setMeetID(int meetID) {
		this.meetID = meetID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Time getStartTime() {
		return startTime;
	}
	
	public String getStartTimeAsString() {
		return startTime.toString();
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}
	
	public String getEndTimeAsString() {
		return endTime.toString();
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public RoomModel getRoom() {
		return room;
	}

	public void setRoom(RoomModel room) {
		this.room = room;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public EmployeeModel getResponsible() {
		return responsible;
	}

	public void setResponsible(EmployeeModel responsible) {
		this.responsible = responsible;
	}

	public ArrayList<ParticipantModel> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<ParticipantModel> participants) {
		this.participants = participants;
	}

	public String isAppiontmentString() {
		if (isAppointment) {
			return "true";
		} else {
			return "false";
		}
	}

	public void setAppiontment(boolean isAppiontment) {
		this.isAppointment = isAppiontment;
	}

	public String getMeetingName() {
		return name;
	}

	public void setMeetingName(String meetingName) {
		this.name = meetingName;
	}

	public Date getStartDate() {
		return startDate;
	}
	
	public String getStartDateAsString() {
		return startDate.toString();
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}
	
	public String getEndDateAsString() {
		return endDate.toString();
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public String getPlaceOrRoom() {
		if (room != null) {
			return room.getName(); 
		}
		else if (place != null) {
			return place;
		}
		else {
			return null;
		}
		
	}
	
	

}
