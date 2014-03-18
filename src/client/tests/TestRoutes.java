package tests;

import java.sql.Timestamp;
import java.util.ArrayList;

import model.Employee;
import model.Meeting;
import model.Participant;
import model.Room;
import model.Status;

import org.junit.Test;

import model.Message;

import database.ClientObjectFactory;
import junit.extensions.jfcunit.JFCTestCase;

public class TestRoutes extends JFCTestCase {

	private String username = "nicholat";
	private String description = "description goes here";
	private String password = "test";
	private String email = "test@test.com";
	private String name = "test user";
	private String meetingName = "Kaffemote";
	private String meetingDescription = "Kaffe med gutta.";
	private String meetingPlace = "NTNU Fellesprosjekt";
	private int meetid = 574;

	@Test
    public void testEmployee() {
		
        Employee employee = new Employee(name, username, email, password);
        ClientObjectFactory.addEmployee(employee);
        Employee emp = ClientObjectFactory.getEmployeeByUsername(username);

        assertEquals(username, emp.getUsername());
        assertEquals(password, emp.getPassword());
        assertEquals(email, emp.getEmail());
        assertEquals(name, emp.getName());
        
        employee.setName(this.name + "update");
        employee.setEmail(this.email + "update");
        employee.setPassword(this.password + "update");
        ClientObjectFactory.updateEmployee(employee);
        emp = ClientObjectFactory.getEmployeeByUsername(username);
        
        assertEquals(username, emp.getUsername());
        assertEquals(password + "update", emp.getPassword());
        assertEquals(email + "update", emp.getEmail());
        assertEquals(name + "update", emp.getName());
        
        ClientObjectFactory.deleteEmployee(username);
        emp = ClientObjectFactory.getEmployeeByUsername(username);
		
        assertNull(emp);
    }
	
	
	@Test
	public void testAddMeeting() {
		
		ArrayList<Participant> participants = new ArrayList<Participant>();
		Employee employee = new Employee(name, username, email, password);
        ClientObjectFactory.addEmployee(employee);
		
		
		//init our meeting participants
		for(int i = 0; i < 5; i++) {
			participants.add(new Participant(name + i, username + i, email, password + i, Status.INVITED));
		}
		
		// set time for meeting
		Timestamp startTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60));
		Timestamp endTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 2));
		
		Room room = new Room(3,30,"G027");
		Employee emp = new Employee(name, username, email, password);
		
		// create a meeting
		Meeting meeting = new Meeting(meetid, description, startTime, endTime, room, meetingPlace, emp, participants, true, meetingName);
		System.out.println(meeting);
		
		// add the meetinh to database
		ClientObjectFactory.addMeeting(meeting);
		
		// fetch the newly created meeting from database
		Meeting resultMeeting = ClientObjectFactory.getMeetingByID(meetid);
		System.out.println(resultMeeting);
		
		// check if meeting fetched from database is the same as the initial meeting. 
		assertEquals(meetingName, resultMeeting.getName());
		assertEquals(meetingDescription, resultMeeting.getDescription());
		assertEquals(meetingPlace, resultMeeting.getPlace());
		assertEquals(username, resultMeeting.getResponsible().getUsername());
		assertEquals(password, resultMeeting.getResponsible().getPassword());
		assertEquals(name, resultMeeting.getResponsible().getName());
		assertEquals(email, resultMeeting.getResponsible().getEmail());
		System.out.println(room.getCapacity());
		System.out.println(resultMeeting.getRoom().getCapacity());
		assertEquals(room.getCapacity(), resultMeeting.getRoom().getCapacity());
		assertEquals(room.getName(), resultMeeting.getRoom().getName());
		
		for (int i = 0; i < participants.size(); i++) {
			assertEquals(participants.get(i).getUsername(), resultMeeting.getParticipants().get(i).getUsername());
			assertEquals(participants.get(i).getName(), resultMeeting.getParticipants().get(i).getName());
			assertEquals(participants.get(i).getPassword(), resultMeeting.getParticipants().get(i).getPassword());
			assertEquals(participants.get(i).getEmail(), resultMeeting.getParticipants().get(i).getEmail());
		}
		
		assertEquals(meeting.isAppointment(), resultMeeting.isAppointment());
		

		ClientObjectFactory.deleteEmployee(username);
	}
	
	//public void testAddMessage() {
		//Message message = new Message(name, username, email, password);
	//}
	
	public static void main(String args[]) {
		TestRoutes tr = new TestRoutes();
		tr.testEmployee();
		tr.testAddMeeting();
	}
}
