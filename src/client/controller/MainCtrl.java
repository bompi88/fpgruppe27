package controller;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import resources.AppConstants;

import framework.Controller;
import framework.Model;
import framework.State;

import utils.RelativeLayout;
import view.MainView;
import view.SideBarView;

import model.EmployeeModel;

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
	private EmployeeModel currentEmployee;
	
	private ImageIcon appIcon;
	
	// Our controllers (Internal main states)
	private LoginCtrl loginCtrl;
	private CalendarCtrl calendarCtrl;
	private InboxCtrl inboxCtrl;
	private AppointmentCtrl appointmentCtrl;

	public MainCtrl() {
		
		// set a new user model.
		currentEmployee = new EmployeeModel();
		setModel(currentEmployee);
		
		// initialize the GUI
		initUI();
	}
	
	/**
	 * Initializes the user interface.
	 */
	public void initUI() {
		
		// load app icon
		BufferedImage resizedImage;
		BufferedImage image;
		
		try {
			image = ImageIO.read(getClass().getResource("/resources/deer.png"));
			resizedImage = resizeImage(image,120,90);
			setAppIcon(new ImageIcon(resizedImage));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Create a layout fro mainFrame
		RelativeLayout rl = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl.setAlignment(RelativeLayout.LEADING);
		
		// our sidebar
		sidebarPanel = new SideBarView(this);
		
		// create a wrapper panel and set it as Content Panel.
		mainWrapperPanel.setLayout(rl);
		mainWrapperPanel.add(sidebarPanel);
		mainWrapperPanel.setVisible(false);
		mainFrame.setContentPane(mainWrapperPanel);
		mainFrame.setPreferredSize(new Dimension(1000,800));
		
		// To simplify things, we only have one possible size of our main window.
		mainFrame.setResizable(false);
		
	}
	
	public void startApp() {
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
        		
            	// init the controllers
        		calendarCtrl = new CalendarCtrl(getMainCtrl());
        		loginCtrl = new LoginCtrl(getMainCtrl());
        		inboxCtrl = new InboxCtrl(getMainCtrl());
        		appointmentCtrl = new AppointmentCtrl(getMainCtrl());
            	
        		if(isRemembered()) {
        			
        			EmployeeModel m = new EmployeeModel();
        			
        			try {
        				m.fetch(((EmployeeModel)model).getUsername());
        			} catch (ClassNotFoundException | SQLException e) {
        				// TODO Auto-generated catch block
        				e.printStackTrace();
        			}
        			currentEmployee = m;
        			
        			login();
        		} else {
        			setState(LoginCtrl.class);
        		}
            }
        });
	}
	
	public boolean isRemembered() {
		
		BufferedReader br = null;
		String hash = "";
		String username = "sr";
		model = new EmployeeModel();
		try {
			br = new BufferedReader(new FileReader(cookieFileName));
	        hash = br.readLine();
	        ((EmployeeModel)model).setUsername(br.readLine());
	        
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
		
		return hash.equals("logged in");
	}
	
	public void logout() {
		
		mainWrapperPanel.setVisible(false);
		currentEmployee.setPassword("");
		setState(LoginCtrl.class);
		
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
		System.out.println("logged out");
	}
	
	public void login() {
		currentEmployee.setPassword("");
		setState(CalendarCtrl.class);
		sidebarPanel.init();
		mainWrapperPanel.setVisible(true);
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(cookieFileName, "UTF-8");
			writer.println("logged in");
			writer.println(currentEmployee.getUsername());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} finally {
			
		}
		System.out.println("logged in");
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	@Override
	public void setState(Class<? extends State> c) {
		calendarCtrl.hide();
		inboxCtrl.hide();
		appointmentCtrl.hide();
		loginCtrl.hide();
		
		if(c.equals(loginCtrl.getClass())) {
			loginCtrl.show();
		} else if (c.equals(calendarCtrl.getClass())) {
			calendarCtrl.show();
		} else if (c.equals(inboxCtrl.getClass())) {
			inboxCtrl.show();
		} else if (c.equals(appointmentCtrl.getClass())) {
			appointmentCtrl.show();
		}
	}
	
	public void setAppIcon(ImageIcon icon) {
		appIcon = icon;
	}
	
	public ImageIcon getAppIcon() {
		return appIcon;
	}
	
	public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Model> T getModel() {
		return (T) currentEmployee;
	}
	
	@Override
	public <T extends Model> void setModel(T model) {
		currentEmployee = (EmployeeModel) model;
	}
}
