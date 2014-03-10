package model;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;


import framework.Model;

public class MeetingModel extends Model {
	
	protected int meetID;
	protected Date date;
	protected String description;
	protected Time startTime;
	protected Time endTime;
	protected RoomModel room;
	protected String place;
	protected EmployeeModel responsible;
	protected ArrayList<ParticipantModel> participants;
	protected boolean isAppiontment;
	
	public MeetingModel() {

	}

	@Override
	public void create() throws ClassNotFoundException, SQLException {
		// add to DB meetID, date description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),      
		// add to DB  meetID, participants, StatusModel to TABLE MeetingParticipants
		
		String query1=String.format( "insert into Meeting" +"(meetid, description, meetDate, START, END, place, roomid, username,) values ('%d','%d')",meetID, description, date, startTime, endTime, place, room.getRoomID(), responsible.getUsername()); 
		
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

	@Override
	public void save() throws ClassNotFoundException, SQLException {
		// write to DB meetID, date description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),      
		// write to DB meetID, participants, StatusModel to TABLE MeetingParticipants
		
	}

	@Override
	public void delete() throws ClassNotFoundException, SQLException {
		// remove from DB meetID, date description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),      
		// remove from DB meetID, participants, StatusModel to TABLE MeetingParticipants
		
	}

	@Override
	public void fetch(String meetId) throws ClassNotFoundException, SQLException {
		// fetch and overwrite meetID, date description starttime, endtime, roomid or place, responsible(as username TABLE Meeting),      
		// fetch and overwrite meetID, participants, StatusModel to TABLE MeetingParticipants
	}
	
	// TODO: functionality for adding people 

	public int getMeetID() {
		return meetID;
	}

	public void setMeetID(int meetID) {
		this.meetID = meetID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
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

	public boolean isAppiontment() {
		return isAppiontment;
	}

	public void setAppiontment(boolean isAppiontment) {
		this.isAppiontment = isAppiontment;
	}
	
	
	

}
