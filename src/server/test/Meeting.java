package test;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;


public class Meeting {
	
	protected int meetID;
	protected Date startDate;
	protected Date endDate;	
	protected String description;
	protected Time startTime;
	protected Time endTime;
	protected Room room;
	protected String place;
	protected Employee responsible;
	protected ArrayList<Employee> participants = new ArrayList<Employee>();
	protected boolean isAppointment;
	protected String name;
	
	
	public Meeting(Date startDate, Date endDate,
			String description, Time startTime, Time endTime, String place,
			Employee responsible, ArrayList<Employee> participants,
			boolean isAppointment, String name) {
		super();
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.place = place;
		this.responsible = responsible;
		this.participants = participants;
		this.isAppointment = isAppointment;
		this.name = name;
	}
	
	
	
	public Meeting(int meetID, Date startDate, Date endDate,
			String description, Time startTime, Time endTime, Room room,
			Employee responsible, ArrayList<Employee> participants,
			boolean isAppointment, String name) {
		super();
		this.meetID = meetID;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.room = room;
		this.responsible = responsible;
		this.participants = participants;
		this.isAppointment = isAppointment;
		this.name = name;
	}


	
	
	
	public Meeting(int meetID, Date startDate, Date endDate,
			String description, Time startTime, Time endTime, Room room,
			String place, Employee responsible,
			ArrayList<Employee> participants, boolean isAppointment, String name) {
		super();
		this.meetID = meetID;
		this.startDate = startDate;
		this.endDate = endDate;
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



	public Meeting(String name, Employee responsible, Room room) {
		this.name = name;
		this.responsible = responsible;
		this.room = room;
		// TODO Auto-generated constructor stub
	}



	public int getMeetID() {
		return meetID;
	}
	public void setMeetID(int meetID) {
		this.meetID = meetID;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public ArrayList<Employee> getParticipants() {
		return participants;
	}
	public void setParticipants(ArrayList<Employee> participants) {
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
	
	
	


}
