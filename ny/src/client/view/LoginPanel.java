package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import resources.AppConstants;
import resources.ImageManager;

/**
 * LoginPanel is basically a login form, which fire login events on user request.
 */
@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	
	private JTextField usernameField = new JTextField();
	private JTextField passwordField = new JPasswordField();
	private JButton loginButton = new JButton();
	private Color backgroundColor = AppConstants.LOGIN_BG_COLOR;
	private JLabel picLabel;
	
	public LoginPanel() {
		
		// panel setup
		setPreferredSize(new Dimension(AppConstants.LOG_IN_DIALOG_WIDTH, AppConstants.LOG_IN_DIALOG_HEIGHT));
		setBackground(backgroundColor);
		
		ImageManager.getInstance();
		// set the app icon
		ImageIcon appIcon = ImageManager.getAppIcon();
		picLabel = new JLabel(appIcon);
		
		// initialize fields
		usernameField.setPreferredSize(new Dimension((AppConstants.LOG_IN_DIALOG_WIDTH / 4) * 3, 30));
		passwordField.setPreferredSize(new Dimension((AppConstants.LOG_IN_DIALOG_WIDTH / 4) * 3, 30));
		loginButton.setText(AppConstants.LOG_IN_BUTTON_TEXT);
		
		// create a wrapper panel and add all elements to it.
		JPanel wrapper = new JPanel();
		wrapper.setPreferredSize(new Dimension((AppConstants.LOG_IN_DIALOG_WIDTH / 4) * 3, (AppConstants.LOG_IN_DIALOG_WIDTH / 4) * 2));
		
		wrapper.setBackground(backgroundColor);
		
		wrapper.add(picLabel);
		wrapper.add(usernameField);
		wrapper.add(passwordField);
		wrapper.add(loginButton);
	
		add(wrapper);
	}
	
	public void addLoginButtonListener(ActionListener listener) {
		loginButton.addActionListener(listener);
	}
	
	public void addPasswordFieldKeyListener(KeyListener listener) {
		passwordField.addKeyListener(listener);
	}
	
	public void addUsernameFieldKeyListener(KeyListener listener) {
		usernameField.addKeyListener(listener);
	}
	
	public String getPassword() {
		return usernameField.getText();
	}
	
	public String getUsername() {
		return passwordField.getText();
	}
	
	public void setPassword(String password) {
		passwordField.setText(password);
	}
	
	public void setUsername(String username) {
		usernameField.setText(username);
	}
}