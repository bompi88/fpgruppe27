import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

import controller.MainCtrl;
import database.DatabaseInitalizer;

/**
 * Calendeer Client is an application which displays your personal calendar in
 * an awesome and fabulous way. It communicates with a SQL-Database through
 * usage of tha' holy JDBC Driver. Or does it...?
 * 
 * Here is where it all begins. Once upon a time there was a...
 */
@SuppressWarnings("unused")
public class CalendeerClient {

	private static final int PORT = 12345; // random large port number
	private static ServerSocket s;

	public static void main(String[] args) {

		// checks whether application is already running
		try {
			s = new ServerSocket(PORT, 10, InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			// shouldn't happen for localhost
		} catch (IOException e) {
			// port taken, so app is already running
			System.exit(0);
		}

		// For initializing of the database
		// DatabaseInitalizer dbInit = new DatabaseInitalizer();
		// dbInit.initDatabase();

		// create our main controller and start our application
		MainCtrl mainCtrl = new MainCtrl();
		mainCtrl.startApp();
	}
}
