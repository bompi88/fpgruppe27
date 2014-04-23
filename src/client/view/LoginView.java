package view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import resources.AppConstants;

@SuppressWarnings("serial")
public class LoginView extends JDialog {

	private LoginPanel loginPanel;

	private JPanel errorPanel;
	
	private Dimension windowSize = new Dimension(AppConstants.LOG_IN_DIALOG_WIDTH, AppConstants.LOG_IN_DIALOG_HEIGHT);
	
	public LoginView(JFrame main) {
		super(main, false);
		
		// create error panel
		errorPanel = new JPanel();
		errorPanel.setPreferredSize(new Dimension(300, 40));
		
		JLabel errorLabel = new JLabel(AppConstants.LOG_IN_ERROR_TEXT);
		errorLabel.setForeground(AppConstants.LOG_IN_ERROR_TEXT_COLOR);
		
		errorPanel.add(errorLabel);
		errorPanel.setVisible(false);
		errorPanel.setBackground(AppConstants.LOGIN_BG_COLOR);
		
		// create the login form
		loginPanel = new LoginPanel();
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
	}
	
	public void addLoginButtonListener(ActionListener listener) {
		loginPanel.addLoginButtonListener(listener);
	}
	
	public void addPasswordFieldKeyListener(KeyListener listener) {
		loginPanel.addPasswordFieldKeyListener(listener);
	}
	
	public void addUsernameFieldKeyListener(KeyListener listener) {
		loginPanel.addUsernameFieldKeyListener(listener);
	}
	
	public String getPassword() {
		return loginPanel.getPassword();
	}
	
	public String getUsername() {
		return loginPanel.getUsername();
	}
	
	public void setPassword(String password) {
		loginPanel.setPassword(password);
	}
	
	public void setUsername(String username) {
		loginPanel.setUsername(username);
	}
	
	public void showErrorMessage() {
		errorPanel.setVisible(true);
	}
	
	public void hideErrorMessage() {
		errorPanel.setVisible(false);
	}
}
