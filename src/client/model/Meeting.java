package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import database.ClientObjectFactory;

public class Meeting {

	private int meetid;
	private String description;
	private Timestamp startTime;
	private Timestamp endTime;
	private Room room;
	private String place;
	private Employee responsible;
	private List<Participant> participants;
	private boolean isAppointment;
	private String name;
	
	public Meeting() {
		this.meetid = 0;
		this.description = "";
		this.startTime = new Timestamp(System.currentTimeMillis());
		this.endTime = new Timestamp(System.currentTimeMillis());
		this.room = new Room();
		this.place = "";
		this.responsible = new Employee();
		this.participants = new ArrayList<Participant>();
		this.isAppointment = false;
		this.name = "";
	}

	public Meeting(int meetid, String description, Timestamp startTime,
			Timestamp endTime, Room room, String place, Employee responsible,
			List<Participant> participants, boolean isAppointment,
			String name) {
		super();
		this.meetid = meetid;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.room = room;
		this.place = place;
		this.responsible = responsible;
		this.participants = participants;
		this.isAppointment = isAppointment;
		this.name = name;
	}
	
	public Meeting(String description, Timestamp startTime, Timestamp endTime,
			Room room, String place, Employee responsible,
			List<Participant> participants, boolean isAppointment,
			String name) {
		super();
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.room = room;
		this.place = place;
		this.responsible = responsible;
		this.participants = participants;
		this.isAppointment = isAppointment;
		this.name = name;
	}

	public void post() {
		ClientObjectFactory.addMeeting(this);
	}
	
	public void put() {
		ClientObjectFactory.updateMeeting(this);
	}
	
	public void delete() {
		ClientObjectFactory.deleteMeeting(meetid);
	}
	
	public void setNegativeAttendenceAndRemove(Employee emp) {
		ClientObjectFactory.setNegAttandenceAndRemove(this, emp);
	}
	
	public int getMeetid() {
		return meetid;
	}

	public void setMeetid(int meetid) {
		this.meetid = meetid;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public Employee getResponsible() {
		return responsible;
	}

	public void setResponsible(Employee responsible) {
		this.responsible = responsible;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	public boolean isAppointment() {
		return isAppointment;
	}

	public void setAppointment(boolean isAppointment) {
		this.isAppointment = isAppointment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setModel(Meeting m) {
			meetid = m.getMeetid();
			description = m.getDescription();
			startTime = m.getStartTime();
			endTime = m.getEndTime();
			room = m.getRoom();
			place = m.getPlace();
			responsible = m.getResponsible();
			participants = m.getParticipants();
			isAppointment = m.isAppointment();
			name = m.getName();
	}
	
	public void removeParticipants(Participant emp) {
		participants.remove(emp);
	}
	
	public boolean areUserpartInMeeting(String username){
		boolean tempreturn = false; 
		for(Participant p : getParticipants()){  
			if(p.getUsername().equals(username)){
				tempreturn = true; 
			}
		}
		return tempreturn;
	}
}
