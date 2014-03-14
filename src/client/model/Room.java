package model;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;

import framework.Model;


public class Room extends Model {
	
	private int roomID;
	private int capacity;
	private String name;
	private ArrayList<Meeting> bookingList;
	
	
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
	
	public String toString() {
		return "Room [roomID=" + roomID + ", capacity=" + capacity + ", name="
				+ name + "]";
	}
	

}
