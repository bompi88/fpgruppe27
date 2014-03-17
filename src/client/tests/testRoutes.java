package tests;

import java.sql.Timestamp;
import java.util.ArrayList;

import model.Employee;
import model.Meeting;
import model.Participant;
import model.Room;
import model.Status;

import org.junit.Test;

import database.ClientObjectFactory;
import junit.extensions.jfcunit.JFCTestCase;

public class testRoutes extends JFCTestCase {

	private String username = "test";
	private String password = "test";
	private String email = "test@test.com";
	private String name = "test user";
	
	private String meetingName = "Kaffemøte";
	private String meetingDescription = "Kaffe med gutta.";
	private String meetingPlace = "NTNU Fellesprosjekt";

	@Test
	public void testRemoveEmployee() {
		
		ClientObjectFactory.deleteEmployee(username);
		
		Employee emp = ClientObjectFactory.getEmployeeByUsername(username);
		
		assertNull(emp);
	}
	
	@Test
	public void testUpdateEmployee() {
		Employee employee = new Employee(name + "update", username, email + "update", password + "update");
		
		ClientObjectFactory.updateEmployee(employee);
		
		Employee emp = ClientObjectFactory.getEmployeeByUsername(username);
		
		assertEquals(username, emp.getUsername());
        assertEquals(password + "update", emp.getPassword());
        assertEquals(email + "update", emp.getEmail());
        assertEquals(name + "update", emp.getName());
	}
	
	@Test
    public void testAddAndGETEmployee() {
		
        Employee employee = new Employee(name, username, email, password);

        ClientObjectFactory.addEmployee(employee);
        
        Employee emp = ClientObjectFactory.getEmployeeByUsername(username);

        assertEquals(username, emp.getUsername());
        assertEquals(password, emp.getPassword());
        assertEquals(email, emp.getEmail());
        assertEquals(name, emp.getName());
    }
	
	@Test
	public void testAddMeeting() {
		
		ArrayList<Participant> participants = new ArrayList<Participant>();
		
		//init our meeting participants
		for(int i = 0; i < 5; i++) {
			participants.add(new Participant(name + i, username + i, email, password + i, Status.INVITED));
		}
		
		// set time for meeting
		Timestamp startTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60));
		Timestamp endTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 2));
		
		Room room = new Room(1,30,"G026");
		
		// create a meeting
		Meeting meeting = new Meeting();
		meeting.setName(meetingName);
		meeting.setDescription(meetingDescription);
		meeting.setStartTime(startTime);
		meeting.setEndTime(endTime);
		meeting.setPlace(meetingPlace);
		meeting.setResponsible(new Employee(username));
		meeting.setRoom(room);
		meeting.setParticipants(participants);
		meeting.setAppointment(true);
		
		// add the meetinh to database
		int meetID = ClientObjectFactory.addMeeting(meeting);
		
		// fetch the newly created meeting from database
		Meeting resultMeeting = ClientObjectFactory.getMeetingByID(meetID);
		
		// check if meeting fetched from database is the same as the initial meeting. 
		assertEquals(meetingName, resultMeeting.getName());
		assertEquals(meetingDescription, resultMeeting.getDescription());
		assertEquals(startTime.getTime(), resultMeeting.getStartTime().getTime());
		assertEquals(endTime.getTime(), resultMeeting.getEndTime().getTime());
		assertEquals(meetingPlace, resultMeeting.getPlace());
		
		assertEquals(username, resultMeeting.getResponsible().getUsername());
		assertEquals(password, resultMeeting.getResponsible().getPassword());
		assertEquals(name, resultMeeting.getResponsible().getName());
		assertEquals(email, resultMeeting.getResponsible().getEmail());
		
		assertEquals(room.getCapacity(), resultMeeting.getRoom().getCapacity());
		assertEquals(room.getName(), resultMeeting.getRoom().getName());
		
		for (int i = 0; i < participants.size(); i++) {
			assertEquals(participants.get(i).getUsername(), resultMeeting.getParticipants().get(i).getUsername());
			assertEquals(participants.get(i).getName(), resultMeeting.getParticipants().get(i).getName());
			assertEquals(participants.get(i).getPassword(), resultMeeting.getParticipants().get(i).getPassword());
			assertEquals(participants.get(i).getEmail(), resultMeeting.getParticipants().get(i).getEmail());
		}
		
		assertEquals(meeting.isAppointment(), resultMeeting.isAppointment());
	}
}
