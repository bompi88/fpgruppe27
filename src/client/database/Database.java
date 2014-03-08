package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 
 * This class tries to simplify the connection of the application to the
 * database. All parameters that are dynamic are stored in a Properties File
 * 
 * @author orestis
 * 
 */
public class Database {
	private Properties properties; // file containing the connection properties
	private String jdbcDriver; // String containing the driver Class name
	private String url; // Address to the database
	private Connection conn;

	/**
	 * 
	 * Reads properties from a File
	 * 
	 * @param propertiesFilename
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Database(String propertiesFilename) throws IOException,
			ClassNotFoundException, SQLException {
		File f = new File(propertiesFilename);
		properties = new Properties();
		properties.load(new FileInputStream(f));
		jdbcDriver = properties.getProperty("jdbcDriver");
		url = properties.getProperty("url");
	}

	public Database(Properties properties) throws ClassNotFoundException,
			SQLException {
		jdbcDriver = properties.getProperty("jdbcDriver");
		url = properties.getProperty("url");
		this.properties=properties;

	}

	/**
	 * 
	 * This method should be called before the first query and after we have
	 * closed the connection in order to create a new one
	 * 
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void initialize() throws ClassNotFoundException, SQLException {
		
		Class.forName(jdbcDriver);
		Properties info = new Properties();

		/*
		 * info is a different Properties object from the "properties" one.
		 * "info" is used by the Driver Manager while properties is used by our
		 * program
		 */
		
		info.setProperty("user", properties.getProperty("user"));
		info.setProperty("password", properties.getProperty("user"));
		conn = DriverManager.getConnection(url, info);
	}

	/**
	 * 
	 * Used for SELECT queries
	 * 
	 * 
	 * @param sql
	 *            The query in SQL.No semicolon (;) is needed
	 * @return A result set containing the data.
	 * @throws SQLException
	 */
	public ResultSet makeSingleQuery(String sql) throws SQLException {
		Statement st = conn.createStatement();
		return st.executeQuery(sql);
	}

	/**
	 * Used for single or few insertions, deletions and updates
	 * 
	 * @param sql
	 *            The query in SQL.No semicolon (;) is needed.
	 * @throws SQLException
	 */
	public void makeSingleUpdate(String sql) throws SQLException {
		Statement st = conn.createStatement();
		st.executeUpdate(sql);
	}

	/**
	 * 
	 * Used for batch selections, insertions or other queries
	 * 
	 * @param sql
	 *            The query in SQL.No semicolon (;) is needed.
	 * @return
	 * 			Returns a PreparedStatement for the query. See the description of the PreparedStatement
	 * @throws SQLException
	 */
	public PreparedStatement preparedStatement(String sql) throws SQLException {
		return conn.prepareStatement(sql);
	}

	public void close() throws SQLException {
		conn.close();
	}

}
