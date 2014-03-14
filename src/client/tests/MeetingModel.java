package tests;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.sql.Date;
import database.ClientObjectFactory;

public class MeetingModel  {
	
	protected int meetid;
	protected Timestamp startTime;
	protected Timestamp endTime;
	protected String description;
	protected RoomModel room;
	protected String place;
	protected EmployeeModel responsible;
	protected ArrayList<ParticipantModel> participants = new ArrayList<ParticipantModel>();
	protected boolean isAppointment;
	protected String name;
	
	public MeetingModel() {

	}
	
	public MeetingModel(int meetid, String description, Timestamp startTime,
			Timestamp endTime, RoomModel room, String place, EmployeeModel responsible,
			ArrayList<ParticipantModel> participants, boolean isAppointment,
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
	
//	public void sendAdminMessage(String typeOfMessage, ParticipantModel personOfInterest) throws ClassNotFoundException, SQLException{  
//		MessageModel message = new MessageModel(this,typeOfMessage, (ParticipantModel) responsible, personOfInterest);  
//		message.setMeeting(this);  
//		message.create(); 
//	}
		
//	public void sendUserMessages(String typeOfMessage) throws ClassNotFoundException, SQLException{
//		for(int i = 0; i < participants.size(); i++){
//			if(!(participants.get(i).getUsername().equals(responsible.getUsername()))){			// sjekker at responsible ikke er i participants 
//				MessageModel message = new MessageModel(this, typeOfMessage, participants.get(i), null) ;  
//				message.setMeeting(this); 
//				message.create(); 
//			}
//		}
//	}
//	
//
//	public void create() throws ClassNotFoundException, SQLException {
//		ClientFactory.addMeeting(this);
//		
//		sendUserMessages("meetingCreated"); // sender melding om oprettet mote til alle participants. 
//		
//	}
//
//	public void save() throws ClassNotFoundException, SQLException {
//		ClientFactory.updateMeeting(this);
//		
//		sendUserMessages("time or place changed"); // not complete	
//	}
//
//	public void delete() throws ClassNotFoundException, SQLException {
//		ClientFactory.deleteMeeting(meetid);
//
//		sendUserMessages("meetingCancled");
//	}
//	
	
	public void fetch() throws ClassNotFoundException, SQLException {
		ClientObjectFactory.getMeetingByID(meetid);
	}
	
	public static List<MeetingModel> fetchMeetingsByWeek(int weekNumber) throws ClassNotFoundException, SQLException {
		
		return ClientObjectFactory.getMeetingByWeek(weekNumber);
	}
	
	// TODO: functionality for adding people 
	
	
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

	public int getMeetID() {
		return meetid;
	}

	public void setMeetID(int meetID) {
		this.meetid = meetID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	public void removeParticipants(ParticipantModel emp) {
		participants.remove(emp);
	}
	
	public Timestamp getStartTime() {
		return startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
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
	
	@Override
	public String toString() {
		return getName() + ": " + getPlace();
	}

	public int getMeetid() {
		return meetid;
	}

	public void setMeetid(int meetid) {
		this.meetid = meetid;
	}
	
}
