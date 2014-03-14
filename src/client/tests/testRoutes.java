package tests;

import model.Employee;

import org.junit.Test;

import database.ClientObjectFactory;
import junit.extensions.jfcunit.JFCTestCase;

public class testRoutes extends JFCTestCase {

	private String username = "test";
	private String password = "test";
	private String email = "test@test.com";
	private String name = "test user";

	@Test
	public void testRemoveEmployee() {
		
		ClientObjectFactory.deleteEmployee(username);
		
		Employee emp = ClientObjectFactory.getEmployeeByUsername(username);
		
		assertNull(emp);
	}
	
	@Test
	public void testUpdateEmployee() {
		Employee employee = new Employee(name + "update", username, email + "update", password + "update");
		
		ClientObjectFactory.updateEmployee(employee);
		
		Employee emp = ClientObjectFactory.getEmployeeByUsername(username);
		
		assertEquals(username, emp.getUsername());
        assertEquals(password + "update", emp.getPassword());
        assertEquals(email + "update", emp.getEmail());
        assertEquals(name + "update", emp.getName());
	}
	
	@Test
    public void testAddAndGETEmployee() {
		
        Employee employee = new Employee(name, username, email, password);

        ClientObjectFactory.addEmployee(employee);
        
        Employee emp = ClientObjectFactory.getEmployeeByUsername(username);

        assertEquals(username, emp.getUsername());
        assertEquals(password, emp.getPassword());
        assertEquals(email, emp.getEmail());
        assertEquals(name, emp.getName());
    }
}
