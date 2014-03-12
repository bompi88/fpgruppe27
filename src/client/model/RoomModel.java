package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import framework.Model;

public class RoomModel extends Model {
	
	protected int roomID;
	protected int capacity;
	protected String name;
	protected ArrayList<MeetingModel> bookingList;

	
	public RoomModel getRoom(int roomId){ 
		if(roomId == this.roomID){
			return this; 
		}
		else{
			return null;
		}
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

	public ArrayList<MeetingModel> getBookingList() {
		return bookingList;
	}

	public void setBookingList(ArrayList<MeetingModel> bookingList) {
		this.bookingList = bookingList;
	}

	public void create() throws ClassNotFoundException, SQLException {
		// creates an entry in Room with capacity and name
		// get roomID from database
		
		String query = String.format("insert into Room ( name, capacity) values ('%d','%d');", name, capacity); 
		
		db.initialize();
		db.makeSingleUpdate(query);
		ResultSet rs = db.makeSingleQuery("select last_insert_ID();");
		db.close();
		
		rs.next();
		roomID = rs.getInt(0);
		
	}

	public void save() throws ClassNotFoundException, SQLException {
		// updates entry in Room with this roomid
		
		String query = String.format("update Room set name = '%d', capacity = '%d' where roomid = '%d';", name, capacity, roomID);
		
		db.initialize();
		db.makeSingleUpdate(query);
		db.close();
	}

	public void delete() throws ClassNotFoundException, SQLException {
		// remove entry in Room with this roomid
		
		String query = String.format("delete from Room where roomid = '%d';", roomID);
		
		db.initialize();
		db.makeSingleUpdate(query);
		db.close();
	}

	public RoomModel fetchByModel() throws ClassNotFoundException, SQLException {
		// updates name and capacity in model according to database;
		
		String query = String.format("select name, capacity form Room where roomid = '%d';", roomID);
		
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);
		db.close();
		
		rs.next();
		name = rs.getString("name");
		capacity = rs.getInt("capacity");
		
		return null;
	}
	
	public void fetchByID(int ID) throws ClassNotFoundException, SQLException {
		// updates name and capacity in model according to database;
		
		String query = String.format("select name, capacity form Room where roomid = '%d';", ID);
		
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);
		db.close();
		
		rs.next();
		name = rs.getString("name");
		capacity = rs.getInt("capacity");
		roomID = ID; 
	}
	
	public void fetch() throws ClassNotFoundException, SQLException {
		// updates name and capacity in model according to database;
		
		String query = String.format("select name, capacity form Room where roomid = '%d';", roomID);
		
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);
		db.close();
		
		rs.next();
		name = rs.getString("name");
		capacity = rs.getInt("capacity"); 
	}

}
