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

public class TestRoutes extends JFCTestCase {

	private String username = "testarbrukjar";
	private String description = "description goes here";
	private String password = "test";
	private String email = "test@test.com";
	private String name = "mbebe";
	private String meetingName = "Kaffemote";
	private String meetingPlace = "NTNU Fellesprosjekt";

	@Test
    public void testEmployee() {
		
        //create a new employee and add it to the db
		Employee employee = new Employee(name, username, email, password);
        ClientObjectFactory.addEmployee(employee);
        
        //create an employee from the newly created db entry
        Employee emp = ClientObjectFactory.getEmployeeByUsername(username);

        //check if the employee we just created is the same as we get from the db
        assertEquals(username, emp.getUsername());
        assertEquals(password, emp.getPassword());
        assertEquals(email, emp.getEmail());
        assertEquals(name, emp.getName());
        
        //change the name of the employee we created, update the db with this and then get it back from the db again
        employee.setName(this.name + "update");
        employee.setEmail(this.email + "update");
        employee.setPassword(this.password + "update");
        ClientObjectFactory.updateEmployee(employee);
        emp = ClientObjectFactory.getEmployeeByUsername(username);
        
        //check if the employee we first created locally still matches what we get from the db
        assertEquals(username, emp.getUsername());
        assertEquals(password + "update", emp.getPassword());
        assertEquals(email + "update", emp.getEmail());
        assertEquals(name + "update", emp.getName());
        
        //delete the employee from the db and try to get it anyways
        ClientObjectFactory.deleteEmployee(username);
        emp = ClientObjectFactory.getEmployeeByUsername(username);
		
        //check to see if the employee we just tried to get is null, as it should be
        assertNull(emp);
    }
	
	
	@Test
	public void testMeeting() {
		
		Employee employee = new Employee(name, username, email, password);
		ClientObjectFactory.addEmployee(employee);
		
		ArrayList<Participant> participants = new ArrayList<Participant>();
        
		//init our meeting participants
		for(int i = 0; i < 5; i++) {
			Employee emp = new Employee(name + i, username + i, email, password + i);
			ClientObjectFactory.addEmployee(emp);
			participants.add(new Participant(name + i, username + i, email, password + i, Status.INVITED));
		}
		
		// set time for meeting
		Timestamp startTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60));
		Timestamp endTime = new Timestamp(System.currentTimeMillis() + (1000 * 60 * 60 * 2));
		
		Room room = new Room(3,30,"G027");
		Employee emp = new Employee(name, username, email, password);
		
		// create a meeting
		Meeting meeting = new Meeting(description, startTime, endTime, room, meetingPlace, emp, participants, true, meetingName);
		
		// add the meeting to database
		int id = ClientObjectFactory.addMeeting(meeting);
		
		// fetch the newly created meeting from database
		Meeting resultMeeting = ClientObjectFactory.getMeetingByID(id);
		
		// check if meeting fetched from database is the same as the initial meeting. 
		assertEquals(meetingName, resultMeeting.getName());
		assertEquals(description, resultMeeting.getDescription());
		assertEquals(meetingPlace, resultMeeting.getPlace());
		assertEquals(username, resultMeeting.getResponsible().getUsername());
		assertEquals(password, resultMeeting.getResponsible().getPassword());
		assertEquals(name, resultMeeting.getResponsible().getName());
		assertEquals(email, resultMeeting.getResponsible().getEmail());
		//System.out.println(room.getCapacity());
		//System.out.println(resultMeeting.getRoom().getCapacity());
		//assertEquals(room.getCapacity(), resultMeeting.getRoom().getCapacity());
		//assertEquals(room.getName(), resultMeeting.getRoom().getName());
		
		for (int i = 0; i < participants.size(); i++) {
			assertEquals(participants.get(i).getUsername(), resultMeeting.getParticipants().get(i).getUsername());
			assertEquals(participants.get(i).getName(), resultMeeting.getParticipants().get(i).getName());
			assertEquals(participants.get(i).getEmail(), resultMeeting.getParticipants().get(i).getEmail());
		}
		
		//assertEquals(meeting.isAppointment(), resultMeeting.isAppointment());
		
		ClientObjectFactory.deleteMeeting(id);
		for(int i = 0; i < 5; i++) {
			ClientObjectFactory.deleteEmployee(username + i);
		}
		ClientObjectFactory.deleteEmployee(username);
	}
	
	//public void testAddMessage() {
		//Message message = new Message(name, username, email, password);
	//}
	
	public static void main(String args[]) {
		TestRoutes tr = new TestRoutes();
		tr.testEmployee();
		tr.testMeeting();
	}
}