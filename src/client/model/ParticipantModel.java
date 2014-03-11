package model;

import java.sql.SQLException;

public class ParticipantModel extends EmployeeModel {
	
	private StatusModel status;
	private AlarmModel alarm;

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
	public void fetch(String id) throws ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		
	}
	
	public StatusModel getStatus() {
		return status;
	}
	public String getStatusAsString() {
		if (status == StatusModel.ATTENDING){
			return "ATTENDING"; 
		}
		else if (status == StatusModel.INVITED){
			return "INVITED"; 
		}
		else if (status == StatusModel.DECLINED){
			return "DECLINED"; 
		}
		else{
			return ""; 
		}
		
	}

	public void setStatus(StatusModel status) {
		this.status = status;
	}

	public AlarmModel getAlarm() {
		return alarm;
	}

	public void setAlarm(AlarmModel alarm) {
		this.alarm = alarm;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
