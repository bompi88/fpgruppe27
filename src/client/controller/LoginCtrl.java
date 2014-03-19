package controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import database.ClientObjectFactory;

import model.Employee;
import framework.Controller;
import framework.State;
import view.LoginView;

public class LoginCtrl extends Controller implements State {
	
	private LoginView loginDialog;
	
	public LoginCtrl(Controller ctrl) {
		super(ctrl);
		
		setModel((Employee)ctrl.getModel());
		
		loginDialog = new LoginView(this, (Employee)getModel(), false);
		loginDialog.setVisible(true);
		loginDialog.pack();
		
		loginDialog.addWindowListener(new WindowAdapter() { 
		    @Override public void windowClosed(WindowEvent e) { 
		      System.exit(0);
		    }
		  });
	}
	
	public void login(String password) {
		MainCtrl ctrl = (MainCtrl)getMainCtrl();
		Employee model = (Employee) getModel();
		
		try {
			if(ClientObjectFactory.authenticate(model, password)) {
				model = ClientObjectFactory.getEmployeeByUsername(model.getUsername());
				ctrl.setModel(model);
				ctrl.login();
			} else {
				loginDialog.showErrorMessage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void show() {
		isHidden = false;
		loginDialog.hideErrorMessage();
		loginDialog.setVisible(true);
		loginDialog.setEnabled(true);
	}

	@Override
	public void hide() {
		isHidden = true;
		loginDialog.setVisible(false);
		loginDialog.setEnabled(false);
	}
}
