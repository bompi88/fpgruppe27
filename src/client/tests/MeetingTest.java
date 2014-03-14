package tests;

import java.sql.Timestamp;
import java.util.ArrayList;

import database.ClientObjectFactory;



import model.Employee;
import model.Meeting;
import model.Participant;
import model.Room;
import model.Status;

public class MeetingTest extends junit.framework.TestCase {
	
	
	public static void main(String[] args) {
		MeetingTest test = new MeetingTest();
		test.testAddMeeting();
	}
	
	
	public void testAddMeeting() {
		ClientObjectFactory.addMeeting(getTestMeeting());
	}
	
	
	public Meeting getTestMeeting() {
		ArrayList<Participant> participants = new ArrayList<Participant>();
		participants.add(new Participant("andreasd", "andybb3", "a@d", "asdsad", Status.INVITED));
		Meeting meeting = new Meeting();
		meeting.setName("UnitTestMote");
		meeting.setDescription("Dette er en test");
		meeting.setStartTime(new Timestamp(System.currentTimeMillis()));
		meeting.setEndTime(new Timestamp(System.currentTimeMillis()));
		meeting.setPlace("NTNU Fellesprosjekt");
		meeting.setResponsible(new Employee("andybb1", "Andreas"));
		meeting.setRoom(new Room());
		meeting.setParticipants(participants);
		return meeting;
	}

}
