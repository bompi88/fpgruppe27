package test;

import java.util.ArrayList;


public class Room {
	
	protected int roomID;
	protected int capacity;
	protected String name;
	protected ArrayList<Meeting> bookingList;
	
	
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
	public ArrayList<Meeting> getBookingList() {
		return bookingList;
	}
	public void setBookingList(ArrayList<Meeting> bookingList) {
		this.bookingList = bookingList;
	}
	
	
	

}
