package model;

import java.util.ArrayList;
import java.util.List;

public class Room {

	private int roomID;
	private int capacity;
	private String name;
	private List<Meeting> bookingList;
	
	public Room() {
		this.roomID = 0;
		this.capacity = 0;
		this.name = "";
		this.bookingList = new ArrayList<Meeting>();
	}
	
	public Room(int roomID, int capacity, String name, List<Meeting> bookingList) {
		super();
		this.roomID = roomID;
		this.capacity = capacity;
		this.name = name;
		this.bookingList = bookingList;
	}
	
	public Room(int roomID, int capacity, String name) {
		super();
		this.roomID = roomID;
		this.capacity = capacity;
		this.name = name;
	}

	public Room(int capacity, String name) {
		super();
		this.capacity = capacity;
		this.name = name;
	}

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

	public List<Meeting> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<Meeting> bookingList) {
		this.bookingList = bookingList;
	}
}
