package model;

import java.util.ArrayList;
import java.util.List;

import framework.Model;


public class Room extends Model {
	
	private int roomID;
	private int capacity;
	private String name;
	private List<Meeting> bookingList;
	
	public Room() {}
	
	public Room(int roomID, int capacity, String name) {
		this.roomID = roomID;
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
	public void setBookingList(ArrayList<Meeting> bookingList) {
		this.bookingList = bookingList;
	}
	
	public String toString() {
		return "Room [roomID=" + roomID + ", capacity=" + capacity + ", name="
				+ name + "]";
	}
	

}
