package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
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

public class ClientFactory {
	
	private static HttpClient httpClient;
	private static HttpResponse response;
	private static HttpGet request;
	private static HttpPost post;
	private static HttpEntity httpEntity;
	private static final String API = "http://fpgruppe27.bompi88.eu.cloudbees.net/";
	private static final String GET_EMPLOYEES = API + "employee";
	

	public ClientFactory() {
		httpClient = new DefaultHttpClient();
	}
	
	public static void main(String[] args) throws MalformedURLException, IOException {
		
		//ClientFactory client = new ClientFactory();
		//System.out.println(client.getEmployees());
		//Employee employee = new Employee("Andreas Drivenes", "andybb2", "adr@d.nos", "abc12345");
		//System.out.println(employee);
		//client.addEmployee(employee);
		
		for(int i = 4; i<1000; i++) {
			ClientFactory.addEmployee(new Employee("AndyDDD Drivenes", "andybbbbb" +i, "adr@d.noss", "abc12343445"));
			System.out.println(ClientFactory.getEmployeeByUsername("andybbbbb"+i));
		}
		
		
		

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
