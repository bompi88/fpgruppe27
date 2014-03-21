package controller;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;

import javax.swing.JPanel;

import database.ClientObjectFactory;
import database.PasswordHash;
import database.UpdateThread;

import model.Employee;
import model.Meeting;
import model.Message;
import model.Participant;

import resources.AppConstants;
import utils.RelativeLayout;
import view.CalendarView;
import view.CreateMeetingView;
import view.EditMeetingView;
import view.InboxView;
import view.LoginView;
import view.MainView;
import view.SideBarView;
import view.ViewMeetingView;

import framework.State;

/**
 * Calendeer Client is an application which displays your personal calendar in
 * an awesome and fabulous way. It communicates with a SQL-Database through
 * usage of tha' holy JDBC Driver. Or does it...?
 * 
 * Here is where it all begins. Once upon a time there was a...
 */
@SuppressWarnings("unused")
public class CalendeerClient {

	private static final int PORT = 14645;
	private static ServerSocket socket;

	// For "remember me" functionality
	private String cookieFileName = AppConstants.COOKIE_FILE_PATH;

	private MainView mainFrame;
	private SideBarView sidebarView;
	private JPanel mainWrapperPanel;

	// the states
	private State editMeetingState;
	private State createMeetingState;
	private State viewMeetingState;
	private State calendarState;
	private State inboxState;
	private State loginState;

	// current state
	private State currentState;

	// the views
	private EditMeetingView editMeetingView;
	private CreateMeetingView createMeetingView;
	private ViewMeetingView viewMeetingView;
	private CalendarView calendarView;
	private InboxView inboxView;
	private LoginView loginView;

	// the models
	private static Employee currentEmployee;

	private Meeting currentMeetingModel;
	private List<Meeting> calendarModel;
	private List<Message> inboxModel;

	public CalendeerClient() {

		// init main frame
		mainFrame = new MainView();
		sidebarView = new SideBarView(this);
		mainWrapperPanel = new JPanel();

		// init models
		currentEmployee = new Employee();
		currentMeetingModel = new Meeting();
		calendarModel = new ArrayList<Meeting>();
		inboxModel = new ArrayList<Message>();

		// init views
		editMeetingView = new EditMeetingView();
		createMeetingView = new CreateMeetingView();
		viewMeetingView = new ViewMeetingView();
		calendarView = new CalendarView();
		inboxView = new InboxView();
		loginView = new LoginView(mainFrame);

		// init states
		editMeetingState = new EditMeetingStateController(this,
				currentMeetingModel, editMeetingView);
		createMeetingState = new CreateMeetingStateController(this,
				currentMeetingModel, createMeetingView);
		viewMeetingState = new ViewMeetingStateController(this,
				currentMeetingModel, viewMeetingView);
		calendarState = new CalendarStateController(this, calendarModel,
				calendarView);
		inboxState = new InboxStateController(this, inboxModel, inboxView);
		loginState = new LoginStateController(this, currentEmployee, loginView);

		// initially hide all states
		editMeetingState.hideState();
		createMeetingState.hideState();
		viewMeetingState.hideState();
		calendarState.hideState();
		inboxState.hideState();
		loginState.hideState();

		initUI();

		// set state according to cookie
		if (isLoggedIn()) {
			currentEmployee = readCookie();
			setState(getCalendarState());
			sidebarView.init();
			editMeetingState.initState();
			createMeetingState.initState();
			viewMeetingState.initState();
			calendarState.initState();
			inboxState.initState();
			loginState.initState();
			mainWrapperPanel.setVisible(true);
		} else {
			setState(getLoginState());
		}
	}

	public void initUI() {

		// Create a layout for mainFrame
		RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);

		// create a wrapper panel and set it as Content Panel.
		mainWrapperPanel.setLayout(rl);
		mainWrapperPanel.add(sidebarView);
		mainWrapperPanel.add(editMeetingView);
		mainWrapperPanel.add(createMeetingView);
		mainWrapperPanel.add(viewMeetingView);
		mainWrapperPanel.add(calendarView);
		mainWrapperPanel.add(inboxView);
		mainWrapperPanel.setVisible(false);

		loginView.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
			}
		});

		mainFrame.setContentPane(mainWrapperPanel);
		mainFrame.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH,
				AppConstants.MAIN_FRAME_HEIGHT));

		// To simplify things, we only have one possible size of our main
		// window.
		mainFrame.setResizable(false);
	}

	/**
	 * Checks if user is logged in
	 * 
	 * @return
	 */
	private boolean isLoggedIn() {
		return readCookie().authenticate();
	}

	public void login(Employee emp) {
		try {
			if (ClientObjectFactory.authenticate(emp)) {
				currentEmployee = ClientObjectFactory.getEmployeeByUsername(emp
						.getUsername());
				setAsLoggedIn();
			} else {
				loginView.showErrorMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAsLoggedIn() {
		writeCookie(currentEmployee);
		sidebarView.init();
		editMeetingState.initState();
		createMeetingState.initState();
		viewMeetingState.initState();
		calendarState.initState();
		inboxState.initState();
		loginState.initState();
		setState(getCalendarState());
		mainWrapperPanel.setVisible(true);
	}

	public void logout() {
		setState(getLoginState());
		currentEmployee.setModel(new Employee());
		mainWrapperPanel.setVisible(false);
		writeCookie(null);
	}

	private Employee readCookie() {

		BufferedReader br = null;

		Employee emp = new Employee();

		try {
			br = new BufferedReader(new FileReader(cookieFileName));
			emp.setPassword(br.readLine());
			emp.setUsername(br.readLine());
		} catch (IOException e) {

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return emp;
	}

	private void writeCookie(Employee emp) {

		PrintWriter writer = null;

		if (emp != null) {
			// bake a cookie!
			try {
				writer = new PrintWriter(cookieFileName, "UTF-8");
				writer.println(emp.getPassword());
				writer.println(emp.getUsername());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {
				writer.close();
			}
		} else {
			// eat cookie! yummy!
			try {
				writer = new PrintWriter(cookieFileName, "UTF-8");
				writer.println("");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} finally {
				writer.close();
			}
		}
	}

	public void setState(State newState) {
		if (getCurrentState() != null)
			getCurrentState().hideState();
		newState.showState();
		currentState = newState;
	}

	public State getEditMeetingState() {
		return editMeetingState;
	}

	public State getInboxState() {
		return inboxState;
	}

	public State getLoginState() {
		return loginState;
	}

	public State getCurrentState() {
		return currentState;
	}

	public static Employee getCurrentEmployee() {
		return currentEmployee;
	}

	public void setCurrentEmployee(Employee currentEmployee) {
		CalendeerClient.currentEmployee = currentEmployee;
	}

	public State getCreateMeetingState() {
		return createMeetingState;
	}

	public State getViewMeetingState() {
		return viewMeetingState;
	}

	public State getCalendarState() {
		return calendarState;
	}

	public ViewMeetingView getViewMeetingView() {
		return viewMeetingView;
	}

	public void setMeetingModel(Meeting m) {
		if (m != null) {
			currentMeetingModel.setModel(m);
			((EditMeetingStateController) editMeetingState).setMeetingModel(m);
			((CreateMeetingStateController) createMeetingState).setMeetingModel(m);
			((ViewMeetingStateController) viewMeetingState).setMeetingModel(m);
		}
	}

	public void updateAll() {
		inboxState.updateState();
		calendarState.updateState();
		sidebarView
				.setNumberOfUnseenMessages(((InboxStateController) getInboxState())
						.getNoOfUnseenMess());
	}

	public static void main(String[] args) {

		// checks whether application is already running
		try {
			socket = new ServerSocket(PORT, 10, InetAddress.getLocalHost());
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
		CalendeerClient calendarClient = new CalendeerClient();

		// And From your main() method or any other method

		// Timer timer = new Timer();
		// timer.schedule(new UpdateThread(calendarClient), 0, 5*60*1000);
	}
}