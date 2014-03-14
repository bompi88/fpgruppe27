import controller.MainCtrl;
import database.DatabaseInitalizer;

/**
 * Calendeer Client is an application which displays your personal calendar in an awesome and 
 * fabulous way. It communicates with a SQL-Database through usage of tha' holy JDBC Driver.
 * 
 * Here is where it all begins.
 * Once upon a time there was a...
 */
public class CalendeerClient {
	
	public static void main(String[] args) {
		
//		DatabaseInitalizer dbInit = new DatabaseInitalizer();
//		dbInit.initDatabase();
		
		// create our main controller and start our application
		MainCtrl mainCtrl = new MainCtrl();
		mainCtrl.startApp();
	}
}