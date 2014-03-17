package database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import framework.Model;

/**
 * ClientObjectFactory generates objects on request. It fetches JSON objects from our REST API
 * and returns java objects based on the JSON schema in the server response.
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
	 * Get a message by username and timeFrom(time after last retrieved message). 
	 * @param meeting
	 * @param Employee
	 * @param status
	 */
	public static void setAttandence(Meeting meeting, Employee emp, String status){
		int meetid = meeting.getMeetid(); 
		String username = emp.getUsername(); 
		 
		put = new HttpPut(API + "meeting_participants?meetid="+meetid + "meeting_participants?username="+username + "meeting_participants?status="+status );
		putRequest(put, null);
		EntityUtils.consumeQuietly(response.getEntity());
		
	}
	
	/**
	 * Get a message by username and timeFrom(time after last retrieved message). 
	 * @param username
	 * @param timeFrom
	 * @return Employee
	 */
	public static List<Message> getMessages(String Username, Timestamp timeFrom){
		
		request = new HttpGet(API + "message?username="+ Username + "&time="+timeFrom);
		String messageString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

		Message[] messagePrim = test.fromJson(messageString, Message[].class);
		List<Message> messages = new ArrayList<Message>(Arrays.asList(messagePrim));
		return messages; 
	}
	
	
	/**
	 * Sets a message by messid as seen
	 * @param Message(messid)
	 */
	public static void setMessageAsSeen(Message message){
		
		int messid = message.getMessID(); 
		put = new HttpPut(API + "message?messid="+messid + "message?isSeen=" +true);
		putRequest(put, null);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	/**
	 * Get an employee by username.
	 * @param username
	 * @return Employee
	 */
	public static Employee getEmployeeByUsername(String username)  {
		
		request = new HttpGet(API + "employee?username="+username);
		String employeeString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		
		Employee employee = test.fromJson(employeeString, Employee.class);
		
		return employee;
	}
	
	/**
	 * Get all employees in database.
	 * @return ArrayList<Employee>
	 */
	public static ArrayList<Employee> getEmployees()  {
		
		request = new HttpGet(GET_EMPLOYEES);
		String employeesString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Employee[] employeePrim = test.fromJson(employeesString, Employee[].class);
		ArrayList<Employee> employees = new ArrayList<Employee>(Arrays.asList(employeePrim));
		
		return employees;
	}
	
	/**
	 * Get all employees in database as potential participants.
	 * @return ArrayList<Employee>
	 */
	public static ArrayList<Participant> getEmployeesAsParticipants()  {
		
		request = new HttpGet(GET_EMPLOYEES);
		String employeesString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Participant[] employeePrim = test.fromJson(employeesString, Participant[].class);
		ArrayList<Participant> employees = new ArrayList<Participant>(Arrays.asList(employeePrim));
		
		return employees;
	}
	
	/**
	 * Add a new employee to the database.
	 * @param employee
	 */
	public static void addEmployee(Employee employee) {
		
		post = new HttpPost(API + "employee");
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String employeeString = test.toJson(employee);
	 	postRequest(post, employeeString);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	/**
	 * Delete an employee from database.
	 * @param id
	 */
	public static void deleteEmployee(String username) {
		delete = new HttpDelete(API + "employee?username=" + username);
		deleteRequest(delete);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	/**
	 * Update a meeting in database.
	 * @param meeting
	 */
	public static void updateEmployee(Employee emp) {
		put = new HttpPut(API + "employee");
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String meetingString = test.toJson(emp);
		putRequest(put, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	/**
	 * Add a new meeting to the database.
	 * @param meeting
	 */
	public static int addMeeting(Meeting meeting) {
		post = new HttpPost(API + "meeting");
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String meetingString = test.toJson(meeting);
		String meetID = postRequest(post, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
		
		return Integer.valueOf(meetID);
	}
	
	/**
	 * Get a meeting by id.
	 * @param id
	 * @return
	 */
	public static Meeting getMeetingByID(int id) {
		request = new HttpGet(API + "meeting?meetid=" +  id);
		String meetingString = getRequest(request);
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Meeting meeting = test.fromJson(meetingString, Meeting.class);
		
		return meeting;
	}
	
	/**
	 * Get all meetings in a particular week for X number of employees.
	 * @param weekNumber
	 * @return
	 */
	public static ArrayList<Meeting> getMeetingByWeek(int weekNumber, String[] usernames) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		
		String s = "";
		
		for (int i = 0; i < usernames.length; i++) {
			s += "&username" + "=" + usernames[i];
		}
		
		// get timestamps
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.WEEK_OF_YEAR, weekNumber);        
		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
		String startDateParam = sdf.format(cal.getTime());
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 23, 59, 59);
		String endDateParam = sdf.format(cal.getTime());
		
		request = new HttpGet(API + "meeting?startTime=" + startDateParam + "&endTime=" + endDateParam + s);
		String meetingString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Meeting[] meetingsPrim = test.fromJson(meetingString, Meeting[].class);
		ArrayList<Meeting> meetings = new ArrayList<Meeting>(Arrays.asList(meetingsPrim));
		
		return meetings;
	}
	
	/**
	 * Get all meetings in a particular week for an particular employee.
	 * @param weekNumber
	 * @return
	 */
	public static ArrayList<Meeting> getMeetingByWeek(int weekNumber, String username) {
		String[] usernames = {username};
		return getMeetingByWeek(weekNumber, usernames);
	}
	
	/**
	 * Update a meeting in database.
	 * @param meeting
	 */
	public static void updateMeeting(Meeting meeting) {
		put = new HttpPut(API + "meeting");
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		String meetingString = test.toJson(meeting);
		putRequest(put, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	/**
	 * Delete a meeting from database.
	 * @param id
	 */
	public static void deleteMeeting(int id) {
		delete = new HttpDelete(API + "meeting?meetid=" + id);
		deleteRequest(delete);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	/**
	 * Get all meetings which an employee with username X is owner of. 
	 * @param username
	 * @return
	 */
	public static ArrayList<Meeting> getMeetingsByUsername(String username) {
		request = new HttpGet(API + "meeting?username=" +  username);
		String meetingString = getRequest(request);
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Meeting[] meetingsPrim = test.fromJson(meetingString, Meeting[].class);
		ArrayList<Meeting> meetings = new ArrayList<Meeting>(Arrays.asList(meetingsPrim));
		
		return meetings;

	}
	
	/**
	 * Get all rooms in database.
	 * @return
	 */
	public static ArrayList<Room> getRooms() {
		request = new HttpGet(API + "room");
		String roomsString = getRequest(request);
		Gson test = new GsonBuilder().setExclusionStrategies(new ModelListenerExclusionStrategy()).setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
		Room[] roomsPrim = test.fromJson(roomsString, Room[].class);
		ArrayList<Room> rooms = new ArrayList<Room>(Arrays.asList(roomsPrim));
		
		return rooms;
	}
	
	/**
	 * Used for creating and executing a GET request.
	 * @param requestType
	 * @return
	 */
	private static String getRequest(HttpGet requestType) {
        httpClient = HttpClientBuilder.create().build(); // Creating an instance here
        try {
            response = httpClient.execute(requestType); 
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                httpEntity = response.getEntity();

                if (httpEntity != null) {
                    InputStream instream = httpEntity.getContent(); 
                    String convertedString = convertStreamToString(instream);
                    return convertedString;
                } else return null;

            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  finally {
            try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
        }
    }
	
	/**
	 * Used for creating and executing a POST request.
	 * @param requestType
	 * @param content
	 * @return
	 */
	private static String postRequest(HttpPost requestType, String content) {
      
        try {
            httpClient = HttpClientBuilder.create().build(); // Creating an instance here
    		requestType.setHeader("Content-Type", "application/json; charset=utf-8");
			StringEntity input  = new StringEntity(content);
			requestType.setEntity(input);

            response = httpClient.execute(requestType); 
            if (response != null && response.getStatusLine().getStatusCode() == 201) {
                httpEntity = response.getEntity();

                if (httpEntity != null) {
                    InputStream instream = httpEntity.getContent(); 
                    String convertedString = convertStreamToString(instream);
                    return convertedString;
                    } 
                else return null;

            } 
            else return null;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
          finally {
            try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
        }
    }
	
	/**
	 * Used for creating and executing a PUT request.
	 * @param requestType
	 * @param content
	 * @return
	 */
	private static String putRequest(HttpPut requestType, String content) {
	      
        try {
            httpClient = HttpClientBuilder.create().build(); // Creating an instance here
    		requestType.setHeader("Content-Type", "application/json; charset=utf-8");
			StringEntity input  = new StringEntity(content);
			requestType.setEntity(input);

            response = httpClient.execute(requestType); 
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                httpEntity = response.getEntity();

                if (httpEntity != null) {
                    InputStream instream = httpEntity.getContent(); 
                    String convertedString = convertStreamToString(instream);
                    return convertedString;
                    } 
                else return null;

            } 
            else return null;
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
          finally {
            try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
        }
    }
	
	/**
	 * Used for creating and executing a DELETE request.
	 * @param requestType
	 * @return
	 */
	private static String deleteRequest(HttpDelete requestType) {
        httpClient = HttpClientBuilder.create().build(); // Creating an instance here
        try {
            response = httpClient.execute(requestType); 
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                httpEntity = response.getEntity();

                if (httpEntity != null) {
                    InputStream instream = httpEntity.getContent(); 
                    String convertedString = convertStreamToString(instream);
                    return convertedString;
                } else return null;

            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  finally {
            try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			} // Close the instance here
        }
    }
	
	private static String convertStreamToString(InputStream is) throws IOException {
		String returnString = "";
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				  
		String line = "";
		while ((line = rd.readLine()) != null) {
			returnString += line;
		}
	
		return returnString;
	}
	
	public static boolean authenticate(Employee emp) {
		
		if (emp.getUsername() != null && emp.getPassword() != null && !emp.getPassword().equals("") && !emp.getUsername().equals("")) {
			// Not safe :p
			Employee e = getEmployeeByUsername(emp.getUsername());
			
			if( e != null)
				return emp.getPassword().equals(e.getPassword());
			else
				return false;
		}
		
		return false;
	}
	
	
	public static class ModelListenerExclusionStrategy implements ExclusionStrategy {

        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }

        public boolean shouldSkipField(FieldAttributes f) {

            return (f.getDeclaringClass() == Model.class && f.getName().equals("propertyChangeSupport"));
        }

    }
}
