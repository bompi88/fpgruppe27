package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import model.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import resources.AppConstants;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import framework.Model;

/**
 * ClientObjectFactory generates objects on request. It fetches JSON objects
 * from our REST API and returns java objects based on the JSON schema in the
 * server response.
 */
public class ClientObjectFactory {

	private static CloseableHttpClient httpClient;
	private static HttpResponse response;
	private static HttpGet request;
	private static HttpPost post;
	private static HttpEntity httpEntity;
	private static HttpPut put;
	private static HttpDelete delete;
	private static final String API = AppConstants.REST_API_PATH;
	private static final String GET_EMPLOYEES = API + "employee";

	public ClientObjectFactory() {
		httpClient = HttpClientBuilder.create().build();
	}

	
	/**
	 * Sets status attandence DB by meeting, employee and status.
	 * 
	 * @param meeting
	 * @param Employee
	 * @param status
	 */
	public static void setAttandence(Meeting meeting, Employee emp, String status) {
		int meetid = meeting.getMeetid();
		String username = emp.getUsername();

		put = new HttpPut(API + "meeting_participants?meetid=" + meetid
				+ "&username=" + username
				+ "&status=" + status);
		putRequest(put, null);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Removes employees pat in meeting. For use when user deletes meeting from
	 * calander.
	 * 
	 * @param meeting
	 * @param Employee
	 */
	public static void setNegAttandenceAndRemove(Meeting meeting, Employee emp) {
		setAttandence(meeting, emp, "DECLINED");
		int meetid = meeting.getMeetid();
		String username = emp.getUsername();
		delete = new HttpDelete((API + "meeting_participants?meetid=" + meetid
				+ "&username=" + username));
		deleteRequest(delete);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Get a message by username and timeFrom(time after last retrieved
	 * message).
	 * 
	 * @param username
	 * @param timeFrom
	 * @return Employee
	 */
	public static List<Message> getMessages(String Username, Timestamp timeFrom) {

		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		String fromTimeFormatted = sdf.format(timeFrom.getTime());

		request = new HttpGet(API + "message?username=" + Username + "&time="
				+ fromTimeFormatted);
		String messageString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

		Message[] messagePrim = builder
				.fromJson(messageString, Message[].class);
		List<Message> messages = new ArrayList<Message>(
				Arrays.asList(messagePrim));
		return messages;
	}

	/**
	 * Sets a message by messid as seen
	 * 
	 * @param Message (messid)
	 */
	public static void setMessageAsSeen(Message message) {

		int messid = message.getMessID();
		put = new HttpPut(API + "message?messid=" + messid + "&isSeen=1");
		putRequest(put, "");
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Get an employee by username.
	 * 
	 * @param username
	 * @return Employee
	 */
	public static Employee getEmployeeByUsername(String username) {

		request = new HttpGet(API + "employee?username=" + username);
		String employeeString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

		Employee employee = builder.fromJson(employeeString, Employee.class);
		return employee;
	}

	/**
	 * Get all employees in database.
	 * 
	 * @return ArrayList<Employee>
	 */
	public static ArrayList<Employee> getEmployees() {

		request = new HttpGet(GET_EMPLOYEES);
		String employeesString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Employee[] employeePrim = builder.fromJson(employeesString,
				Employee[].class);
		ArrayList<Employee> employees = new ArrayList<Employee>(
				Arrays.asList(employeePrim));

		return employees;
	}

	/**
	 * Get all employees in database as potential participants.
	 * 
	 * @return ArrayList<Employee>
	 */
	public static ArrayList<Participant> getEmployeesAsParticipants() {

		request = new HttpGet(GET_EMPLOYEES);
		String employeesString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Participant[] employeePrim = builder.fromJson(employeesString,
				Participant[].class);
		ArrayList<Participant> employees = new ArrayList<Participant>(
				Arrays.asList(employeePrim));

		return employees;
	}

	/**
	 * Add a new employee to the database.
	 * 
	 * @param employee
	 */
	public static void addEmployee(Employee employee) {
		post = new HttpPost(API + "employee");
		Gson builder = new GsonBuilder()
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String employeeString = builder.toJson(employee);
		postRequest(post, employeeString);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Delete an employee from database.
	 * 
	 * @param id
	 */
	public static void deleteEmployee(String username) {
		delete = new HttpDelete(API + "employee?username=" + username);
		deleteRequest(delete);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Update a meeting in database.
	 * 
	 * @param meeting
	 */
	public static void updateEmployee(Employee emp) {
		put = new HttpPut(API + "employee");
		Gson builder = new GsonBuilder()
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String meetingString = builder.toJson(emp);
		putRequest(put, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Add a new meeting to the database.
	 * 
	 * @param meeting
	 */
	public static int addMeeting(Meeting meeting) {
		post = new HttpPost(API + "meeting");
		Gson builder = new GsonBuilder()
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String meetingString = builder.toJson(meeting);
		String meetid = postRequest(post, meetingString);
		System.out.println("kk:" + meetingString);
		System.out.println("ll:" + meetid);
		EntityUtils.consumeQuietly(response.getEntity());
		return Integer.valueOf(meetid);
	}

	/**
	 * Get a meeting by id.
	 * 
	 * @param id
	 * @return
	 */
	public static Meeting getMeetingByID(int id) {
		request = new HttpGet(API + "meeting?meetid=" + id);
		String meetingString = getRequest(request);
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Meeting meeting = builder.fromJson(meetingString, Meeting.class);

		return meeting;
	}

	/**
	 * Get all meetings in a particular week for X number of employees.
	 * 
	 * @param weekNumber
	 * @return
	 */
	public static ArrayList<Meeting> getMeetingByWeek(int weekNumber,
			String[] usernames) {

		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

		String s = "";

		for (int i = 0; i < usernames.length; i++) {
			s += "&username" + "=" + usernames[i];
		}

		// get timestamps
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekNumber);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 0, 0, 0);
		String startDateParam = sdf.format(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DATE), 23, 59, 59);
		String endDateParam = sdf.format(cal.getTime());

		request = new HttpGet(API + "meeting?startTime=" + startDateParam
				+ "&endTime=" + endDateParam + s);
		String meetingString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());

		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Meeting[] meetingsPrim = builder.fromJson(meetingString,
				Meeting[].class);
		ArrayList<Meeting> meetings = new ArrayList<Meeting>(
				Arrays.asList(meetingsPrim));

		return meetings;
	}

	/**
	 * Get all meetings in a particular week for an particular employee.
	 * 
	 * @param weekNumber
	 * @return
	 */
	public static ArrayList<Meeting> getMeetingByWeek(int weekNumber,
			String username) {
		String[] usernames = { username };
		return getMeetingByWeek(weekNumber, usernames);
	}

	/**
	 * Update a meeting in database.
	 * 
	 * @param meeting
	 */
	public static void updateMeeting(Meeting meeting) {
		put = new HttpPut(API + "meeting");
		Gson builder = new GsonBuilder()
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String meetingString = builder.toJson(meeting);
		putRequest(put, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Delete a meeting from database.
	 * 
	 * @param id
	 */
	public static void deleteMeeting(int id) {
		delete = new HttpDelete(API + "meeting?meetid=" + id);
		deleteRequest(delete);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Deletes a meeting participant to a meeting with id X
	 * 
	 * @param username
	 * @param meetid
	 */
	public static void deleteParticipant(String username, int meetid) {
		delete = new HttpDelete(API + "meeting_participants?username="
				+ username + "&meetid=" + meetid);
		deleteRequest(delete);
		EntityUtils.consumeQuietly(response.getEntity());
	}

	/**
	 * Get all meetings which an employee with username X is owner of.
	 * 
	 * @param username
	 * @return
	 */
	public static ArrayList<Meeting> getMeetingsByUsername(String username) {
		request = new HttpGet(API + "meeting?username=" + username);
		String meetingString = getRequest(request);
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Meeting[] meetingsPrim = builder.fromJson(meetingString,
				Meeting[].class);
		ArrayList<Meeting> meetings = new ArrayList<Meeting>(
				Arrays.asList(meetingsPrim));

		return meetings;

	}

	/**
	 * Get all rooms in database.
	 * 
	 * @return
	 */
	public static ArrayList<Room> getRooms() {
		request = new HttpGet(API + "room");
		String roomsString = getRequest(request);
		Gson builder = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, booleanAsIntAdapter)
				.registerTypeAdapter(boolean.class, booleanAsIntAdapter)
				.setExclusionStrategies(new ModelListenerExclusionStrategy())
				.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Room[] roomsPrim = builder.fromJson(roomsString, Room[].class);
		ArrayList<Room> rooms = new ArrayList<Room>(Arrays.asList(roomsPrim));

		return rooms;
	}

	/**
	 * Used for creating and executing a GET request.
	 * 
	 * @param requestType
	 * @return
	 */
	private static String getRequest(HttpGet requestType) {
		httpClient = HttpClientBuilder.create().build(); // Creating an instance
															// here
		try {
			response = httpClient.execute(requestType);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				httpEntity = response.getEntity();

				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
					String convertedString = convertStreamToString(instream);
					return convertedString;
				} else
					return null;

			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
		}
	}

	/**
	 * Used for creating and executing a POST request.
	 * 
	 * @param requestType
	 * @param content
	 * @return
	 */
	private static String postRequest(HttpPost requestType, String content) {

		try {
			httpClient = HttpClientBuilder.create().build(); // Creating an
																// instance here
			requestType.setHeader("Content-Type",
					"application/json; charset=utf-8");
			StringEntity input = new StringEntity(content);
			requestType.setEntity(input);

			response = httpClient.execute(requestType);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 201) {
				httpEntity = response.getEntity();

				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
					String convertedString = convertStreamToString(instream);
					return convertedString;
				} else
					return null;

			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
		}
	}

	/**
	 * Used for creating and executing a PUT request.
	 * 
	 * @param requestType
	 * @param content
	 * @return
	 */
	private static String putRequest(HttpPut requestType, String content) {

		try {
			httpClient = HttpClientBuilder.create().build(); // Creating an
																// instance here
			requestType.setHeader("Content-Type",
					"application/json; charset=utf-8");
			StringEntity input = new StringEntity(content);
			requestType.setEntity(input);

			response = httpClient.execute(requestType);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				httpEntity = response.getEntity();

				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
					String convertedString = convertStreamToString(instream);
					return convertedString;
				} else
					return null;

			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
		}
	}

	/**
	 * Used for creating and executing a DELETE request.
	 * 
	 * @param requestType
	 * @return
	 */
	private static String deleteRequest(HttpDelete requestType) {
		httpClient = HttpClientBuilder.create().build(); // Creating an instance
															// here
		try {
			response = httpClient.execute(requestType);
			if (response != null
					&& response.getStatusLine().getStatusCode() == 200) {
				httpEntity = response.getEntity();

				if (httpEntity != null) {
					InputStream instream = httpEntity.getContent();
					String convertedString = convertStreamToString(instream);
					return convertedString;
				} else
					return null;

			} else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
		}
	}

	private static String convertStreamToString(InputStream is)
			throws IOException {
		String returnString = "";
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		String line = "";
		while ((line = rd.readLine()) != null) {
			returnString += line;
		}

		return returnString;
	}

	public static boolean authenticate(Employee emp, String password)
			throws NoSuchAlgorithmException, InvalidKeySpecException {

		if (emp.getUsername() != null && emp.getPassword() != null
				&& !emp.getPassword().equals("")
				&& !emp.getUsername().equals("")) {
			// Not safe :p
			Employee e = getEmployeeByUsername(emp.getUsername());
			if (e != null) {
				if (PasswordHash.validatePassword(password, e.getPassword()))
					return true;
			} else
				return false;
		}

		return false;
	}

	public static boolean authenticateWithHash(Employee s) {

		if (s.getUsername() != null && s.getPassword() != null
				&& !s.getUsername().equals("") && !s.getPassword().equals("")) {
			// Not safe :p
			Employee e = getEmployeeByUsername(s.getUsername());
			if (e != null) {
				if (e.getPassword().equals(s.getPassword()))
					return true;
			}
		}
		return false;
	}

	public static class ModelListenerExclusionStrategy implements
			ExclusionStrategy {

		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}

		public boolean shouldSkipField(FieldAttributes f) {

			return (f.getDeclaringClass() == Model.class && f.getName().equals(
					"propertyChangeSupport"));
		}
	}

	private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {
		@Override
		public void write(JsonWriter out, Boolean value) throws IOException {
			if (value == null) {
				out.nullValue();
			} else {
				out.value(value);
			}
		}

		@Override
		public Boolean read(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			switch (peek) {
			case BOOLEAN:
				return in.nextBoolean();
			case NULL:
				in.nextNull();
				return null;
			case NUMBER:
				return in.nextInt() != 0;
			case STRING:
				return Boolean.parseBoolean(in.nextString());
			default:
				throw new IllegalStateException(
						"Expected BOOLEAN or NUMBER but was " + peek);
			}
		}
	};
}