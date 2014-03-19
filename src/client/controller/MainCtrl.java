package controller;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import database.ClientObjectFactory;

import resources.AppConstants;
import framework.Controller;
import framework.State;

import framework.Model;
import utils.RelativeLayout;
import view.AppointmentView;
import view.MainView;
import view.SideBarView;

import model.Employee;
import model.Meeting;

/**
 * This Controller takes care of bottom layer function calls, like login() and logout() etc.
 * It creates a main window and keeps track of which child controller which's in charge. 
 */
public class MainCtrl extends Controller {
	
	// For "remember me" functionality
	private String cookieFileName = AppConstants.COOKIE_FILE_PATH;
	
	// Our main window setup
	private MainView mainFrame = new MainView();
	private SideBarView sidebarPanel;
	private JPanel mainWrapperPanel = new JPanel();
	
	// Our current logged in user
	private static Employee currentEmployee;
	
	// Our controllers (Internal main states)
	private LoginCtrl loginCtrl;
	private CalendarCtrl calendarCtrl;
	private InboxCtrl inboxCtrl;
	private AppointmentCtrl appointmentCtrl;
	private ViewAppointmentCtrl viewAppointmentCtrl;

	public MainCtrl() {
		
		// set a new user model.
		currentEmployee = new Employee();
		setModel(currentEmployee);
		
		// initialize the GUI
		initUI();
	}
	
	/**
	 * Initializes the user interface.
	 */
	public void initUI() {

		// Create a layout for mainFrame
		RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);
		
		// our sidebar
		sidebarPanel = new SideBarView(this);
		
		// create a wrapper panel and set it as Content Panel.
		mainWrapperPanel.setLayout(rl);
		mainWrapperPanel.add(sidebarPanel);
		mainWrapperPanel.setVisible(false);
		mainFrame.setContentPane(mainWrapperPanel);
		mainFrame.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH,AppConstants.MAIN_FRAME_HEIGHT));
		
		// To simplify things, we only have one possible size of our main window.
		mainFrame.setResizable(false);
	}
	
	/**
	 * Creates the respective controllers and views, and and then
	 * starts the application.
	 */
	public void startApp() {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		
            	// init the controllers
        		
        		loginCtrl = new LoginCtrl(getMainCtrl());
        		appointmentCtrl = new AppointmentCtrl(getMainCtrl());
        		viewAppointmentCtrl = new ViewAppointmentCtrl(getMainCtrl());
        		inboxCtrl = new InboxCtrl(getMainCtrl());
        		inboxCtrl.addObserver(sidebarPanel);
            	
        		updateThread uThread = new updateThread(true, inboxCtrl, calendarCtrl); 
        		uThread.start(); 
        		inboxCtrl = uThread.getInboxCtrl(); 
        		calendarCtrl = uThread.getCalandarCtrl(); 
        		
        		
        		
        		// if cookie: login
        		if(isRemembered()) {
        			
        			Employee m = ClientObjectFactory.getEmployeeByUsername(((Employee)model).getUsername());
        			
        			//get user based on username and login
        			currentEmployee = m;
        			if (m != null) {
        				login();
        			} else {
        				currentEmployee = new Employee();
        				setState(LoginCtrl.class);
        			}
        		} else {
        			// set login state
        			setState(LoginCtrl.class);
        		}
            }
        });
	}
	
	/**
	 * Is there  a ckoookie?!? nam.. nam..
	 */
	public boolean isRemembered() {
		
		BufferedReader br = null;
		
		Employee emp = new Employee();
		
		try {
			br = new BufferedReader(new FileReader(cookieFileName));
	        emp.setPassword(br.readLine());
	        emp.setUsername(br.readLine());
	        model = emp;
		} catch (IOException e) {
			
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// security issues :P
		return ClientObjectFactory.authenticateWithHash(emp);
	}
	
	/**
	 * Logs out current user.
	 */
	public void logout() {
		
		// hide all views
		mainWrapperPanel.setVisible(false);
		
		// reset password
		currentEmployee.setPassword("");
		
		// set Login state
		setState(LoginCtrl.class);
		
		// eat cookie! yummy!
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(cookieFileName, "UTF-8");
			writer.println("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	/**
	 * Handles login event after authentification done by Employee model
	 * against the database.
	 */
	public void login() {
		calendarCtrl = new CalendarCtrl(getMainCtrl());
		// go to calendar
		
		
		// have to initialize our sidebar
		sidebarPanel.init();

		inboxCtrl.initInbox();
		appointmentCtrl.init();
		calendarCtrl.init();
		inboxCtrl.init();
		setState(CalendarCtrl.class);
		sidebarPanel.setNumberOfUnseenMessages(inboxCtrl.getNumberOfUnseenMessages());
		// finally show the contents of our app.
		mainWrapperPanel.setVisible(true);
		
		// bake a cookie! 
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(cookieFileName, "UTF-8");
			writer.println(currentEmployee.getPassword());
			writer.println(currentEmployee.getUsername());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			
		}
	}
	
	@Override
	public void setState(Class<? extends State> c) {
		
		// hide all states if they exists and if they are allready not hidden
		if (calendarCtrl != null && !calendarCtrl.isHidden())
			calendarCtrl.hide();
		if (inboxCtrl != null && !inboxCtrl.isHidden())
			inboxCtrl.hide();
		if (appointmentCtrl != null && !appointmentCtrl.isHidden())
			appointmentCtrl.hide();
		if (viewAppointmentCtrl != null)
			viewAppointmentCtrl.hide();
		loginCtrl.hide();
		
		if (loginCtrl != null)
			loginCtrl.hide();
			
		// show a new state
		if(c.equals(loginCtrl.getClass())) {			
			loginCtrl.show();
		} else if (c.equals(calendarCtrl.getClass())) {
			calendarCtrl.show();
		} else if (c.equals(inboxCtrl.getClass())) {
			inboxCtrl.show();
		} else if (c.equals(appointmentCtrl.getClass())) {
			appointmentCtrl.appointmentPanel.setMeetingModel(new Meeting());
			appointmentCtrl.show();
		} else if (c.equals(viewAppointmentCtrl.getClass())) {
			viewAppointmentCtrl.show();
		}
	}
	
	public static Employee getCurrentEmployee() {
		return currentEmployee;
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Model> T getModel() {
		return (T) currentEmployee;
	}
	
	@Override
	public <T extends Model> void setModel(T model) {
		currentEmployee = (Employee) model;
	}
	
	public void setMeetingModel(Meeting model) {
		viewAppointmentCtrl.appointmentPanel.setModel(model);
		appointmentCtrl.appointmentPanel.setMeetingModel(model);
	}
}
