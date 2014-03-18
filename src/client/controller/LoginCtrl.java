package controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import database.ClientObjectFactory;

import model.Employee;
import framework.Controller;
import framework.State;
import view.LoginView;

public class LoginCtrl extends Controller implements State {
	
	private LoginView loginDialog;

	public LoginCtrl(Controller ctrl) {
		super(ctrl);
		
		
		setModel(ctrl.getModel());
		loginDialog = new LoginView(this, false);
		loginDialog.setVisible(true);
		loginDialog.pack();
		
		loginDialog.addWindowListener(new WindowAdapter() { 
		    @Override public void windowClosed(WindowEvent e) { 
		      System.exit(0);
		    }
		  });
	}
	
	public void login(String password) {
		
		Employee model = (Employee) getModel();
		
		try {
			if(ClientObjectFactory.authenticate(model, password)) {
				model = ClientObjectFactory.getEmployeeByUsername(model.getUsername());
				((MainCtrl)getParentCtrl()).login();
			} else {
				loginDialog.showErrorMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void show() {
		loginDialog.hideErrorMessage();
		loginDialog.setVisible(true);
		loginDialog.setEnabled(true);
	}

	@Override
	public void hide() {
		loginDialog.setVisible(false);
		loginDialog.setEnabled(false);
	}
}
