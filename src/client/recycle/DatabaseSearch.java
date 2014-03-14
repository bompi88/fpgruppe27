package recycle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.EmployeeModel;
import model.ParticipantModel;

public class DatabaseSearch {
	
	private static Database db;
	
	public DatabaseSearch() {
		db = DBConnection.db;
	}

	public static List<EmployeeModel> searchEmployeeByUsername(String username) throws ClassNotFoundException, SQLException {
		String query = "SELECT username, name FROM employee WHERE username LIKE '%" + username + "%'";
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);
		
		List<EmployeeModel> results = new ArrayList<EmployeeModel>();
		
		while(rs.next())
		{
			EmployeeModel e = new EmployeeModel();
			
			e.setUsername(rs.getString(1));
			e.setName(rs.getString(2));
			results.add(e);
		}
		
		rs.close();
		db.close();
		
		return results;
	}
	
	public static List<EmployeeModel> searchEmployeeByName(String name) throws ClassNotFoundException, SQLException {
		String query = "SELECT username, name FROM employee WHERE name LIKE '%" + name + "%'";
		db.initialize();
		ResultSet rs = db.makeSingleQuery(query);
		
		List<EmployeeModel> results = new ArrayList<EmployeeModel>();
		
		while(rs.next())
		{
			EmployeeModel e = new EmployeeModel();
			
			e.setUsername(rs.getString(1));
			e.setName(rs.getString(2));
			results.add(e);
		}
		
		rs.close();
		db.close();
		
		return results;
	}
	
	public static List<ParticipantModel> getAllEmployees() throws ClassNotFoundException, SQLException {
		String query = "SELECT username, name FROM employee";
		DBConnection.db.initialize();
		ResultSet rs = DBConnection.db.makeSingleQuery(query);
		
		List<ParticipantModel> results = new ArrayList<ParticipantModel>();
		
		while(rs.next())
		{
			ParticipantModel e = new ParticipantModel();
			
			e.setUsername(rs.getString(1));
			e.setName(rs.getString(2));
			results.add(e);
		}
		
		rs.close();
		DBConnection.db.close();
		
		return results;

	}
}
