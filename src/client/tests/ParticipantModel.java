package tests;

import java.sql.SQLException;

public class ParticipantModel extends EmployeeModel {
	
	private StatusModel status;
	private Alarm alarm;

	public ParticipantModel() {
		
	}
	
	public ParticipantModel(EmployeeModel employeeModel) {
		super(employeeModel);
	}
	public ParticipantModel(String name, String username, String email, String password) {
		super(name,username,email,email);
	}
	
//	@Override
//	public void create() throws ClassNotFoundException, SQLException {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public void save() throws ClassNotFoundException, SQLException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void delete() throws ClassNotFoundException, SQLException {
//		// TODO Auto-generated method stub
//		
//	}
//
//	@Override
//	public void fetch() throws ClassNotFoundException, SQLException {
//		
//	}
	
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

	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

	@Override
	public String toString() {
		return name;
	}
	
	
}
