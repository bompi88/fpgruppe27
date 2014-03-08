package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import resources.AppConstants;

import model.EmployeeModel;

import controller.LoginCtrl;
import framework.Controller;

/**
 *
 */
public class LoginPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -6617272538650375547L;

	private Controller ctrl;
	
	private JTextField usernameField = new JTextField();
	private JTextField passwordField = new JPasswordField();
	private JButton loginButton = new JButton();
	
	public LoginPanel(Controller ctrl) {
		
		this.ctrl = ctrl;
		
		this.setPreferredSize(new Dimension(500, 300));
		
		JPanel wrapper = new JPanel();
		wrapper.setPreferredSize(new Dimension(300, 200));
		
		usernameField.setPreferredSize(new Dimension(300,30));
		passwordField.setPreferredSize(new Dimension(300,30));
		loginButton.setText(AppConstants.LOG_IN_BUTTON_TEXT);
		
		wrapper.add(usernameField);
		wrapper.add(passwordField);
		wrapper.add(loginButton);
		add(wrapper);
		
		loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				((EmployeeModel)getCtrl().getModel()).setUsername(usernameField.getText());
				((EmployeeModel)getCtrl().getModel()).setPassword(passwordField.getText());
				((LoginCtrl)getCtrl()).login();
			}
			
		});
	}

	public Controller getCtrl() {
		return ctrl;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String propertyChanged = evt.getPropertyName();
		EmployeeModel model = (EmployeeModel)ctrl.getModel();
		
		if (propertyChanged.equals("username")) {
			usernameField.setText(model.getUsername());
		} else if (propertyChanged.equals("password")) {
			passwordField.setText(model.getPassword());
		}
	}	
}
