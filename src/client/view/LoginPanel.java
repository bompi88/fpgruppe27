package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import resources.AppConstants;
import resources.ImageManager;

import model.Employee;

import controller.LoginCtrl;
import database.PasswordHash;
import framework.Controller;

/**
 * LoginPanel is basically a login form, which fire login events on user request.
 */
public class LoginPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -6617272538650375547L;

	private Controller ctrl;
	
	private JTextField usernameField = new JTextField();
	private JTextField passwordField = new JPasswordField();
	private JButton loginButton = new JButton();
	private Color backgroundColor = AppConstants.LOGIN_BG_COLOR;
	private JLabel picLabel;
	
	public LoginPanel(Controller ctrl) {
		
		this.ctrl = ctrl;
		
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
		
		// Adds event listeners 
		
		usernameField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireLogin();
				}
			}
		});
		
		passwordField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					fireLogin();
				}
			}
		});
		
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireLogin();
			}
			
		});
	}
	
	/**
	 * fires login 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	private void fireLogin() {
		String hash = "";
		try {
			hash = PasswordHash.createHash(passwordField.getText());
			((Employee)getCtrl().getModel()).setUsername(usernameField.getText());
			((Employee)getCtrl().getModel()).setPassword(hash);
			((LoginCtrl)getCtrl()).login(passwordField.getText());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public Controller getCtrl() {
		return ctrl;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyChanged = evt.getPropertyName();
		Employee model = (Employee)ctrl.getModel();
		
		if (propertyChanged.equals("username")) {
			usernameField.setText(model.getUsername());
		} else if (propertyChanged.equals("password")) {
			passwordField.setText(model.getPassword());
		}
	}	
}
