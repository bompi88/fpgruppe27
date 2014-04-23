package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import model.Employee;
import view.LoginView;
import framework.State;


public class LoginStateController implements State {

	private CalendeerClient context;
	private Employee employeeModel;
	private LoginView loginView;
	
	public LoginStateController(CalendeerClient context, Employee employeeModel, LoginView loginView) {
		this.context = context;
		this.employeeModel = employeeModel;
		this.loginView = loginView;
		
		loginView.addUsernameFieldKeyListener(new KeyListener() {
			
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
		
		loginView.addPasswordFieldKeyListener(new KeyListener() {
			
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
		
		loginView.addLoginButtonListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fireLogin();
			}
			
		});
	}
	
	public Employee getModel() {
		return employeeModel;
	}
	
	public LoginView getView() {
		return loginView;
	}
	
	/**
	 * fires login 
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchAlgorithmException 
	 */
	private void fireLogin() {
		
		Employee emp = getModel();
		emp.setUsername(getView().getUsername());
		emp.setPassword(getView().getPassword());
		
		login(emp);
	}
	
	public void login(Employee emp) {
		context.login(emp);
	}
	
	@Override
	public void hideState() {
		loginView.setVisible(false);
	}

	@Override
	public void showState() {
		loginView.setVisible(true);
	}

	@Override
	public void updateState() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initState() {
		// TODO Auto-generated method stub

	}

}
