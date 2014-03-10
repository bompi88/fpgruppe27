package model;

import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
	}

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
