package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import framework.Model;


public class Meeting extends Model {
	
	private int meetid;
	private String description;
	private Timestamp startTime;
	private Timestamp endTime;
	private Room room;
	private String place;
	private Employee responsible;
	private ArrayList<Participant> participants = new ArrayList<Participant>();
	private boolean isAppointment;
	private String name;
	
	
	public Meeting() {
		
	}
	
	
	public Meeting(int meetid, String description, Timestamp startTime,
			Timestamp endTime, Room room, String place, Employee responsible,
			ArrayList<Participant> participants, boolean isAppointment,
			String name) {
		
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
	public Timestamp getEndTime() {
		return endTime;
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
	public ArrayList<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<Participant> participants) {
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
	public Date getStartDate() {
		return new Date (startTime.getTime()); // maybe multiply by 1000?
	}
	
	public String getStartDateAsString() {
		return new Date (startTime.getTime()).toString();
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Date getEndDate() {
		return new Date (endTime.getTime());
	}
	
	public String getEndDateAsString() {
		return new Date (endTime.getTime()).toString();
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	
	public void removeParticipants(Participant emp) {
		participants.remove(emp);
	}

	@Override
	public String toString() {
		return "Meeting [meetID=" + meetid + ", description=" + description
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", room=" + room + ", place=" + place + ", responsible="
				+ responsible + ", participants=" + participants
				+ ", isAppointment=" + isAppointment + ", name=" + name + "]";
	}

}
