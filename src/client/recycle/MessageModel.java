package recycle;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MessageModel {
	
	
	protected String type; // er  
	protected Timestamp time; 
	protected String message;
	protected MeetingModel meeting;
	protected boolean isSeen;
	protected ParticipantModel userInQestion; 
	protected ParticipantModel messageOwner; 
	protected int messid;  

	
	
	/*protected Date todaysDate = new Date(System.currentTimeMillis());
	DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss"); 
	Calendar cal = Calendar.getInstance(); 
	DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd"); 
	*/
	
	
	
//	protected String inviteMessage = "Du har blitt invitert til" + meeting.getMeetingName() + "kl" + meeting.getStartDateAsString() + meeting.getStartDateAsString();
//	protected String changeOfTimeMessage = meeting.getMeetingName() + "Har blitt endret. Ny tid er: " + meeting.getStartTimeAsString() + meeting.getStartDateAsString() + "til" + meeting.getEndTimeAsString() + meeting.getEndDateAsString() ; 
//	protected String changeOfPlaceMessage = meeting.getMeetingName() + "har blitt flyttet til" + meeting.getPlaceOrRoom();  
//	protected String meetingCancledMessage = meeting.getMeetingName() + "har blitt avlyst"; 
//	protected String userHasConfirmedMessage = "Har bekreftet m������teinkallingen til" + meeting.getMeetingName();   
//	protected String userHasDeclinedMessage = "Har medlt avbud til" + meeting.getMeetingName();
	
	
	public MessageModel(MeetingModel meetingModel, String type, ParticipantModel messageOwner, ParticipantModel userInQestion) throws ClassNotFoundException, SQLException{
		this.meeting = meetingModel;
		this.type = type; 	
		this.userInQestion = userInQestion; 
		this.messageOwner = messageOwner; 
		setMessage(); 
	}
	
	
	public void setMessage(){
		if (type.equals("meetingCreated")){
			message = "Du har blitt invitert til" + meeting.getMeetingName() + "kl" + meeting.getStartDateAsString() + meeting.getStartDateAsString();  
		}
//		if (type.equals("meetingTimeChanged")){
//			message = changeOfTimeMessage;  
//		}
//		if ((type.equals("placeChanged"))){
//			message = changeOfPlaceMessage; 
//		}
//		if ((type.equals("meetingCancled"))){
//			message = meetingCancledMessage; 
//		}
//		if ((type.equals("partConfrimed"))){ 
//			message = userInQestion.getName() + userHasConfirmedMessage; 
//		}
//		if ((type.equals("partDeclined"))){ 
//			message = userInQestion.getName() + userHasDeclinedMessage; 
//		}
	}
	
	public Date getDate() {
		Date date = new Date(time.getTime());
		return date;
	}
	
	
	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}


	public String getMessage() {
		return message;
	}
	
	public MeetingModel getMeeting() {
		return meeting;
	}
	
	public void setMeeting(MeetingModel meeting) {
		this.meeting = meeting;
	}
	
	public boolean isSeen() {
		return isSeen;
	}
//	
//	public void setSeen(boolean isSeen) throws ClassNotFoundException, SQLException {
//		this.isSeen = isSeen;
//		changeDBSeen(messageOwner.getUsername(), this.time, isSeen); 	
//	}
//	
//	
//	
//	@Override
//	public void create() throws ClassNotFoundException, SQLException {
//		Timestamp timeNow = new Timestamp(System.currentTimeMillis()); 
//		this.time = timeNow;
//		String query=String.format("insert into message " + "(message, time, owner, isSeen) values ('%s','%s','%s', '%d')", message, timeNow, messageOwner.getUsername(), 0); 
//		
//		db.initialize();
//		db.makeSingleUpdate(query);
//		db.close();
//		
//	}
	
//	public void deleteOldMess() throws ClassNotFoundException, SQLException {
//		Timestamp timeNow = new Timestamp(System.currentTimeMillis());  
//		Timestamp time5daysBeforeNow = new Timestamp(0);     
//		long oneWeekinMs = 604800000; 
//		time5daysBeforeNow.setTime(timeNow.getTime() - oneWeekinMs); 
//		
//		String query=String.format("DELETE FROM message WHERE time<'%d'", time5daysBeforeNow ); 
//		
//		db.initialize();
//		db.makeSingleUpdate(query);
//		db.close();
//	}
//	
//	public void fetchMessData(String userOwner, Timestamp timeAfter) throws ClassNotFoundException, SQLException { // henter ut meldinger til brukeren. 
//		
//		String query=String.format("SELECT message, time, isSeen; FROM message; WHERE owner='%s' AND time>'%d' AND isSeen='%b'", userOwner, timeAfter, true);    
//		
//		db.initialize();
//		ResultSet rs = db.makeSingleQuery(query);
//		rs.next();
//		
//		Array Messages = rs.getArray("message"); 
//		Array times = rs.getArray("time");
//		Array isSeen = rs.getArray("isSeen");
//
//		db.close();
//		
//		this.time = (Timestamp) times.getArray(0, 1); 
//		this.message = (String) Messages.getArray(0,1); 
//		this.isSeen = (boolean) isSeen.getArray(0,1); 	
//		
//	}	
//	
//	public int countMessages(String userOwner, Timestamp sinceWhen) throws ClassNotFoundException, SQLException { 
//		
//		String query=String.format("SELECT COUNT(*) FROM message WHERE owner='%s' and time>'%d'", userOwner, sinceWhen); 
//		
//		db.initialize();
//		ResultSet rs = db.makeSingleQuery(query);
//		db.close();
//		
//		rs.next();
//		return rs.getInt(0);
//		
//	}
//	
//	public void changeDBSeen(String userOwner, Timestamp time, boolean isSeen) throws ClassNotFoundException, SQLException{
//		String query=String.format("UPDATE message SET isSeen='%b' WHERE owner ='%s' AND time ='%d'", isSeen, userOwner, time); 
//		
//		db.initialize();
//		db.makeSingleUpdate(query);
//		db.close();
//	}
//	
//	
//	
//	
//	
//	// un������dvendig dritt
//	@Override
//	public void save() throws ClassNotFoundException, SQLException {
//		// Un������dvendig
//		
//	}
//	
//	@Override
//	public void delete() throws ClassNotFoundException, SQLException {
//	}
//	
//	@Override
//	public void fetch() throws ClassNotFoundException, SQLException {
//		
//	}

	public boolean isLastMess(String userOwner) throws ClassNotFoundException, SQLException { 
		
		String query=String.format("SELECT message, time isSeen FROM Message WHERE owner='%s'", userOwner); 
		
		return true; 
		
	}

}
