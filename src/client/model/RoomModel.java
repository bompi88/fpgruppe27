package model;

import java.sql.SQLException;
import java.util.ArrayList;

import framework.Model;

public class RoomModel extends Model {
	
	protected int roomID;
	protected int capacity;
	protected String name;
	protected ArrayList<MeetingModel> bookingList;

	public int getRoomID() {
		return roomID;
	}

	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<MeetingModel> getBookingList() {
		return bookingList;
	}

	public void setBookingList(ArrayList<MeetingModel> bookingList) {
		this.bookingList = bookingList;
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
