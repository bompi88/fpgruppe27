package view;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import resources.AppConstants;

import model.EmployeeModel;

import framework.Controller;

/**
 * This is the login dialog class.
 */
public class LoginView extends JDialog {

	private static final long serialVersionUID = 1202688913205730100L;
	
	private LoginPanel loginPanel;
	private Controller ctrl;
	private EmployeeModel model;
	private JPanel errorPanel;
	
	private Dimension windowSize = new Dimension(AppConstants.LOG_IN_DIALOG_WIDTH, AppConstants.LOG_IN_DIALOG_HEIGHT);
	
	public LoginView(Controller ctrl, boolean modal) { 
		super(ctrl.getMainFrame(), modal);
		
		this.ctrl = ctrl;
		
		
		
		// create error panel
		errorPanel = new JPanel();
		errorPanel.setPreferredSize(new Dimension(300, 40));
		
		JLabel errorLabel = new JLabel(AppConstants.LOG_IN_ERROR_TEXT);
		errorLabel.setForeground(AppConstants.LOG_IN_ERROR_TEXT_COLOR);
		
		errorPanel.add(errorLabel);
		errorPanel.setVisible(false);
		errorPanel.setBackground(AppConstants.LOGIN_BG_COLOR);
		
		// create the login form
		loginPanel = new LoginPanel(this.ctrl);
		loginPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
		
		// adds errorpanel to login form
		loginPanel.add(errorPanel);
		
		setContentPane(this.loginPanel);
		setSize(windowSize);
		setPreferredSize(windowSize);
		// center form
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		
		// sets model and model listener
		model = ctrl.getModel();
		model.addPropertyChangeListener(loginPanel);
	}
	
	public void showErrorMessage() {
		errorPanel.setVisible(true);
	}
	
	public void hideErrorMessage() {
		errorPanel.setVisible(false);
	}
}
