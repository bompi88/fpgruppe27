package database;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Employee;
import model.Meeting;
import model.Participant;
import model.Room;
import model.Status;

public class DatabaseInitalizer {

	private String username = "test";
	private String password = "test";
	private String name = "test user";
	
	private String meetingName = "Kaffemøte";
	private String meetingDescription = "Kaffe med gutta.";
	private String meetingPlace = "NTNU Fellesprosjekt";
	
	public DatabaseInitalizer() {
		
	}
	
	public void initDatabase() {
		
		int numberOfUsers = 20;
		int numberOfMeetings = 20;
		
		// init some test users
		Employee employee = new Employee();
		
		for(int i = 0; i < numberOfUsers; i++) {
			employee.setUsername(username + i);
			employee.setName(name + i);
			employee.setPassword(password + i);
			employee.setEmail(username  + i + "@test.com");
			
			ClientObjectFactory.addEmployee(employee);
		}
		
		
		for (int j = 0; j < numberOfMeetings; j++) {
			// init some meetings
			List<Participant> participants = new ArrayList<Participant>();
			
			//init our meeting participants
			for(int i = 0; i < numberOfUsers; i++) {
				if (Math.random() > 0.2) {
					if (Math.random() < 0.33) {
						participants.add(new Participant(name + i, username + i, username + i + "@test.com", password + i, Status.INVITED));
					} else if (Math.random() > 0.66) {
						participants.add(new Participant(name + i, username + i, username + i + "@test.com", password + i, Status.DECLINED));
					} else {
						participants.add(new Participant(name + i, username + i, username + i + "@test.com", password + i, Status.ATTENDING));
					}
				}
			}
			
			// set time for meeting
			Timestamp startTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * ((int)Math.random() * 10)));
			Timestamp endTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 2 * ((int)Math.random() * 10)));
			
			Room room = new Room(1,(int)(Math.random() * 100),"" + Character.toChars((int)(Math.random() * 20)).toString() + (int)(Math.random() * 400));
			
			// create a meeting
			Meeting meeting = new Meeting();
			meeting.setName(meetingName + j);
			meeting.setDescription(meetingDescription + j);
			meeting.setStartTime(startTime);
			meeting.setEndTime(endTime);
			meeting.setPlace(meetingPlace + j);
			meeting.setResponsible((Employee)participants.get(0));
			meeting.setRoom(room);
			meeting.setParticipants(participants);
			meeting.setAppointment(Math.random() > 0.5);
			
			// add the meetinh to database
			ClientObjectFactory.addMeeting(meeting);
		}
	}
}
