package database;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import model.Employee;

public class DatabaseInitalizer {
	
	public DatabaseInitalizer() {
		
	}
	
	public void initDatabase() throws NoSuchAlgorithmException, InvalidKeySpecException {
		
		// init some test users
		Employee eivingj = new Employee();
		Employee bjorbrat = new Employee();
		Employee einaree = new Employee();
		Employee eivhav = new Employee();
		Employee andybb = new Employee();
		Employee nicholat = new Employee();
		
		eivingj.setUsername("eivingj");
		eivingj.setName("Eivind G");
		eivingj.setEmail("eivingj@stud.ntnu.no");
		eivingj.setPassword(PasswordHash.createHash("eivingj"));
		
		eivhav.setUsername("eivhav");
		eivhav.setName("Eivind H");
		eivhav.setEmail("eivhav@stud.ntnu.no");
		eivhav.setPassword(PasswordHash.createHash("eivhav"));
		
		bjorbrat.setUsername("bjorbrat");
		bjorbrat.setName("Bjørn");
		bjorbrat.setEmail("bjorbrat@stud.ntnu.no");
		bjorbrat.setPassword(PasswordHash.createHash("bjorbrat"));
		
		einaree.setUsername("einaree");
		einaree.setName("Einar");
		einaree.setEmail("einaree@stud.ntnu.no");
		einaree.setPassword(PasswordHash.createHash("einaree"));
		
		andybb.setUsername("andybb");
		andybb.setName("Andreas");
		andybb.setEmail("andybb@stud.ntnu.no");
		andybb.setPassword(PasswordHash.createHash("andybb"));
		
		nicholat.setUsername("nicholat");
		nicholat.setName("Nicholas");
		nicholat.setEmail("nicholat@stud.ntnu.no");
		nicholat.setPassword(PasswordHash.createHash("nicholat"));
		
		ClientObjectFactory.addEmployee(eivingj);
		ClientObjectFactory.addEmployee(eivhav);
		ClientObjectFactory.addEmployee(einaree);
		ClientObjectFactory.addEmployee(bjorbrat);
		ClientObjectFactory.addEmployee(andybb);
		ClientObjectFactory.addEmployee(nicholat);
	}
}
