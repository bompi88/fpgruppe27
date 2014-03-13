package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientFactory {
	
	private static HttpClient httpClient;
	private static HttpResponse response;
	private static HttpGet request;
	private static HttpPost post;
	private static HttpEntity httpEntity;
	private static HttpPut put;
	private static HttpDelete delete;
	private static final String API = "http://fpgruppe27.bompi88.eu.cloudbees.net/";
	private static final String GET_EMPLOYEES = API + "employee";
	

	public ClientFactory() {
		httpClient = new DefaultHttpClient();
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		//ClientFactory client = new ClientFactory();ory
		//System.out.println(client.getEmployees());
		//Employee employee = new Employee("Andreas Drivenes", "andybb2", "adr@d.nos", "abc12345");
		//System.out.println(employee);
		//client.addEmployee(employee);
		ArrayList<Employee> test = new ArrayList<Employee>();
		
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		Date testdate = new Date(10000000000000l);
		String testdatestring = fmt.format(testdate);
		
		
//		for(int i = 0; i<5; i++) {
//			test.add(new Employee("AndyDDDs Drivenes", "andsybbbbb" +i, "adr@d.noss", "abc12343445"));
//		}
		
		System.out.println(ClientFactory.getMeetingByID(13));
		//ClientFactory.addEmployee(new Employee("Andreas Drivenes", "andydbb1", "adr@no", "abc12343445"));
		//ClientFactory.deleteMeeting(1);
		//ClientFactory.addMeeting(new Meeting( "testnavn", new Employee("andreasdrivenes", "passord"), new Room()));
		//ClientFactory.addMeeting(new Meeting(0, new Date(10000000000000l), new Date(9999999999999l), "kaffe", new Time(0), new Time(0), null, "ntnu",
		//		new Employee("andreas", "abc123"), test, false, "testnavvvn"));
		
//		for(int i = 2; i<20; i++) {
//			ClientFactory.addEmployee(new Employee("Andreas Drivenes"+i, "andybb" +i, "adr@no"+i, "abc12343445"+i));
//			System.out.println(ClientFactory.getEmployeeByUsername("andybb"+i));
//		}
		
		
		

	}
	
	public static Employee getEmployeeByUsername(String username)  {
		
		request = new HttpGet(API + "employee?username="+username);
		String employeeString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Employee employee = new Gson().fromJson(employeeString, Employee.class);
		return employee;
		
	}
	
	public static ArrayList<Employee> getEmployees()  {
		
		request = new HttpGet(GET_EMPLOYEES);
		String employeesString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Employee[] employeePrim = new Gson().fromJson(employeesString, Employee[].class);
		ArrayList<Employee> employees = new ArrayList<Employee>(Arrays.asList(employeePrim));
		return employees;

	}
	
	public static void addEmployee(Employee employee) {
		
		post = new HttpPost(API + "employee");
		String employeeString = new Gson().toJson(employee);
	 	String returnStatement = postRequest(post, employeeString);
		EntityUtils.consumeQuietly(response.getEntity());
	}
	
	public static void addMeeting(Meeting meeting) {
		post = new HttpPost(API + "meeting");
		Gson test = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String meetingString = test.toJson(meeting);
		System.out.println(meetingString);
		String returnStatement = postRequest(post, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
		System.out.println(returnStatement);

	}
	
	
	public static Meeting getMeetingByID(int id) {
		request = new HttpGet(API + "meeting?meetid=" +  id);
		String meetingString = getRequest(request);
		System.out.println(meetingString);
		Gson test = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

		Meeting meeting = test.fromJson(meetingString, Meeting.class);
		return meeting;

	}
	
	public static void updateMeeting(Meeting meeting) {
		put = new HttpPut(API + "meeting");
		Gson test = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		String meetingString = test.toJson(meeting);
		String returnStatement = putRequest(put, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());

	}
	
	public static void deleteMeeting(int id) {
		delete = new HttpDelete(API + "meeting?meetid=" + id);
		String returnString = deleteRequest(delete);
		System.out.println(returnString);
		EntityUtils.consumeQuietly(response.getEntity());
		
	}
	
	public static ArrayList<Meeting> getMeetingsByUsername(String username) {
		request = new HttpGet(API + "meeting?username=" +  username);
		String meetingString = getRequest(request);
		Gson test = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Meeting[] meetingsPrim = test.fromJson(meetingString, Meeting[].class);
		ArrayList<Meeting> meetings = new ArrayList<Meeting>(Arrays.asList(meetingsPrim));
		return meetings;

	}
	
	public static ArrayList<Room> getRooms() {
		request = new HttpGet(API + "room");
		String roomsString = getRequest(request);
		Gson builder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Room[] roomsPrim = builder.fromJson(roomsString, Room[].class);
		ArrayList<Room> rooms = new ArrayList<Room>(Arrays.asList(roomsPrim));
		return rooms;
	}
	
	public static ArrayList<Participant> getChoosableParticipants() {
		request = new HttpGet(API + "participant");
		String participantsString = getRequest(request);
		Gson builder = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
		Participant[] participantsPrim = builder.fromJson(participantsString, Participant[].class);
		ArrayList<Participant> participants = new ArrayList<Participant>(Arrays.asList(participantsPrim));
		return participants;
	}
	
	private static String getRequest(HttpGet requestType) {
        httpClient = new DefaultHttpClient(); // Creating an instance here
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
            httpClient.getConnectionManager().shutdown(); // Close the instance here
        }
    }
	
	
	
	private static String postRequest(HttpPost requestType, String content) {
      
        try {
            httpClient = new DefaultHttpClient(); // Creating an instance here
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
            httpClient.getConnectionManager().shutdown(); // Close the instance here
        }
    }
	
	private static String putRequest(HttpPut requestType, String content) {
	      
        try {
            httpClient = new DefaultHttpClient(); // Creating an instance here
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
            httpClient.getConnectionManager().shutdown(); // Close the instance here
        }
    }
	
	private static String deleteRequest(HttpDelete requestType) {
        httpClient = new DefaultHttpClient(); // Creating an instance here
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
            httpClient.getConnectionManager().shutdown(); // Close the instance here
        }
    }
	
	private static String convertStreamToString(InputStream is) throws IOException {
		String returnString = "";
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				  
		String line = "";
		while ((line = rd.readLine()) != null) {
			returnString += line;
		} 
		//is.close();
	
		return returnString;

	}
	
	
	

}
