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
import java.sql.Timestamp;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;




import com.google.gson.Gson;

public class ClientFactory2 {
	
	private static HttpClient httpClient;
	private static HttpResponse response;
	private static HttpGet request;
	private static HttpPost post;
	private static HttpPut put;
	private static HttpEntity httpEntity;
	private static final String API = "http://fpgruppe27.bompi88.eu.cloudbees.net/";
	private static final String GET_EMPLOYEES = API + "employee";
	

	public ClientFactory2() {
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
		
		
		for(int i = 0; i<5; i++) {
			test.add(new Employee("AndyDDDs Drivenes", "andsybbbbb" +i, "adr@d.noss", "abc12343445"));
		}
		//ClientFactory.addMeeting(new Meeting( "testnavn", new Employee("andreasdrivenes", "passord"), new Room()));
		ClientFactory.addMeeting(new Meeting(0, new Date(10000000000000l), new Date(9999999999999l), "kaffe", new Time(0), new Time(0), null, "ntnu",
				new Employee("andreas", "abc123"), test, false, "testnavvvn"));
		
//		for(int i = 4; i<1000; i++) {
//			ClientFactory.addEmployee(new Employee("AndyDDD Drivenes", "andybbbbb" +i, "adr@d.noss", "abc12343445"));
//			System.out.println(ClientFactory.getEmployeeByUsername("andybbbbb"+i));
//		}
		
		
		

	}
	
	
	public static ArrayList<Message> getMessages(String Username, Timestamp timeFrom){
		
		request = new HttpGet(API + "message?username="+Username + "message?time>"+timeFrom);
		String messageString = getRequest(request);
		EntityUtils.consumeQuietly(response.getEntity());
		Message[] messagePrim = new Gson().fromJson(messageString, Message[].class);
		ArrayList<Message> messages = new ArrayList<Message>(Arrays.asList(messagePrim));
		return messages; 
	}
	
	public static void setMessageSeenStatus(Message message, boolean status){
		
		int messid = message.getMessID(); 
		put = new HttpPut(API + "message?messid="+messid + "message?isSeen=" +status);
	 	String returnStatement = putRequest(put);
		EntityUtils.consumeQuietly(response.getEntity());
		
	}
	
	
	private static String putRequest(HttpPut put2) {
		// TODO Auto-generated method stub
		return null;
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
		String meetingString = new Gson().toJson(meeting);
		System.out.println(meetingString);
		String returnStatement = postRequest(post, meetingString);
		EntityUtils.consumeQuietly(response.getEntity());
		System.out.println(returnStatement);

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
