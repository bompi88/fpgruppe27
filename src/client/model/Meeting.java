package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import framework.Model;


public class Meeting extends Model {
	
	private int meetid;
	private String description;
	private Timestamp startTime;
	private Timestamp endTime;
	private Room room;
	private String place;
	private Employee responsible;
	private List<Participant> participants = new ArrayList<Participant>();
	private boolean isAppointment;
	private String name;
	
	
	public Meeting() {
		super();
	}
	
	
	public Meeting(int meetid, String description, Timestamp startTime,
			Timestamp endTime, Room room, String place, Employee responsible,
			ArrayList<Participant> participants, boolean isAppointment,
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
	public int getMeetid() {
		return meetid;
	}
	public void setMeetid(int meetid) {
		int oldValue = this.meetid;
		this.meetid = meetid;
		firePropertyChange("meetid", oldValue, this.meetid);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		String oldValue = this.description;
		this.description = description;
		firePropertyChange("description", oldValue, this.description);
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
		Room oldValue = this.room;
		this.room = room;
		firePropertyChange("room", oldValue, this.room);
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		String oldValue = this.place;
		this.place = place;
		firePropertyChange("place", oldValue, this.place);
	}
	public Employee getResponsible() {
		return responsible;
	}
	public void setResponsible(Employee responsible) {
		Employee oldValue = this.responsible;
		this.responsible = responsible;
		firePropertyChange("responsible", oldValue, this.responsible);
	}
	public List<Participant> getParticipants() {
		return participants;
	}
	public void setParticipants(List<Participant> participants) {
		List<Participant> oldValue = participants;
		this.participants = participants;
		firePropertyChange("participants", oldValue, this.participants);
	}
	public boolean isAppointment() {
		return isAppointment;
	}
	public void setAppointment(boolean isAppointment) {
		boolean oldValue = this.isAppointment;
		this.isAppointment = isAppointment;
		firePropertyChange("isAppointment", oldValue, this.isAppointment);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		firePropertyChange("name", oldValue, this.name);
	}
	public Date getStartDate() {
		return new Date (startTime.getTime()); // maybe multiply by 1000?
	}
	
	public String getStartDateAsString() {
		return new Date (startTime.getTime()).toString();
	}

	public void setStartTime(Timestamp startTime) {
		Timestamp oldValue = this.startTime;
		this.startTime = startTime;
		firePropertyChange("startTime", oldValue, this.startTime);
	}

	public Date getEndDate() {
		return new Date (endTime.getTime());
	}
	
	public String getEndDateAsString() {
		return new Date (endTime.getTime()).toString();
	}


	public void setEndTime(Timestamp endTime) {
		Timestamp oldValue = this.endTime;
		this.endTime = endTime;
		firePropertyChange("endTime", oldValue, this.endTime);
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
