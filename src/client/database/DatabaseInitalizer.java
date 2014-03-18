package database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
	
	public void initDatabase() throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		// init some test users
		Employee eivingj = new Employee();
		Employee bjorbrat = new Employee();
		Employee einaree = new Employee();
		Employee eivhav = new Employee();
		Employee andybb = new Employee();
		Employee nicholat = new Employee();
		
		eivingj.setUsername("eivingj");
		eivingj.setName("Eivind G");
		eivingj.setEmail("eivingj@stud.ntnu.no");
		eivingj.setPassword(PasswordHash.createHash("eivingj"));
		
		eivhav.setUsername("eivhav");
		eivhav.setName("Eivind H");
		eivhav.setEmail("eivhav@stud.ntnu.no");
		eivhav.setPassword(PasswordHash.createHash("eivhav"));
		
		bjorbrat.setUsername("bjorbrat");
		bjorbrat.setName("Bj�rn");
		bjorbrat.setEmail("bjorbrat@stud.ntnu.no");
		bjorbrat.setPassword(PasswordHash.createHash("bjorbrat"));
		
		einaree.setUsername("einaree");
		einaree.setName("Einar");
		einaree.setEmail("einaree@stud.ntnu.no");
		einaree.setPassword(PasswordHash.createHash("einaree"));
		
		andybb.setUsername("andybb");
		andybb.setName("Andreas");
		andybb.setEmail("andybb@stud.ntnu.no");
		andybb.setPassword(PasswordHash.createHash("andybb"));
		
		nicholat.setUsername("nicholat");
		nicholat.setName("Nicholas");
		nicholat.setEmail("nicholat@stud.ntnu.no");
		nicholat.setPassword(PasswordHash.createHash("nicholat"));
		
		ClientObjectFactory.addEmployee(eivingj);
		ClientObjectFactory.addEmployee(eivhav);
		ClientObjectFactory.addEmployee(einaree);
		ClientObjectFactory.addEmployee(bjorbrat);
		ClientObjectFactory.addEmployee(andybb);
		ClientObjectFactory.addEmployee(nicholat);
		
		
//		for (int j = 0; j < numberOfMeetings; j++) {
//			// init some meetings
//			List<Participant> participants = new ArrayList<Participant>();
//			
//			//init our meeting participants
//			for(int i = 0; i < numberOfUsers; i++) {
//				if (Math.random() > 0.2) {
//					if (Math.random() < 0.33) {
//						participants.add(new Participant(name + i, username + i, username + i + "@test.com", password + i, Status.INVITED));
//					} else if (Math.random() > 0.66) {
//						participants.add(new Participant(name + i, username + i, username + i + "@test.com", password + i, Status.DECLINED));
//					} else {
//						participants.add(new Participant(name + i, username + i, username + i + "@test.com", password + i, Status.ATTENDING));
//					}
//				}
//			}
//			
//			// set time for meeting
//			Timestamp startTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * ((int)Math.random() * 10)));
//			Timestamp endTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 2 * ((int)Math.random() * 10)));
//			
//			Room room = new Room(1,(int)(Math.random() * 100),"" + Character.toChars((int)(Math.random() * 20)).toString() + (int)(Math.random() * 400));
//			
//			// create a meeting
//			Meeting meeting = new Meeting();
//			meeting.setName(meetingName + j);
//			meeting.setDescription(meetingDescription + j);
//			meeting.setStartTime(startTime);
//			meeting.setEndTime(endTime);
//			meeting.setPlace(meetingPlace + j);
//			meeting.setResponsible((Employee)participants.get(0));
//			meeting.setRoom(room);
//			meeting.setParticipants(participants);
//			meeting.setAppointment(Math.random() > 0.5);
//			
//			// add the meetinh to database
//			ClientObjectFactory.addMeeting(meeting);
//		}
	}
}
