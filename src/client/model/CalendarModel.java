package model;

import java.sql.SQLException;
import java.util.ArrayList;

import framework.Model;

public class CalendarModel extends Model {
	
	protected ArrayList<MeetingModel> meetingList;
	
	public ArrayList<MeetingModel>getMeetingList(){
		return meetingList;
	}
	
	public void setMeetingList(ArrayList<MeetingModel> meetingList){
		this.meetingList = meetingList;
	}

	@Override
	public void create() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fetch(String o) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}

}
