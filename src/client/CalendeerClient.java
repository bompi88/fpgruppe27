import controller.MainCtrl;
import database.DatabaseInitalizer;

/**
 * Calendeer Client is an application which displays your personal calendar in an awesome and 
 * fabulous way. It communicates with a SQL-Database through usage of tha' holy JDBC Driver. Or does it...?
 * 
 * Here is where it all begins.
 * Once upon a time there was a...
 */
public class CalendeerClient {
	
	public static void main(String[] args) {
		
		
		// For initializing of the database
		// DatabaseInitalizer dbInit = new DatabaseInitalizer();
		// dbInit.initDatabase();
		
		// create our main controller and start our application
		MainCtrl mainCtrl = new MainCtrl();
		mainCtrl.startApp();
	}
}