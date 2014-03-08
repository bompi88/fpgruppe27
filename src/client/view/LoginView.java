package view;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import resources.AppConstants;

import model.EmployeeModel;

import framework.Controller;

public class LoginView extends JDialog {

	private static final long serialVersionUID = 1202688913205730100L;
	
	private LoginPanel loginPanel;
	private Controller ctrl;
	private EmployeeModel model;
	private JPanel errorPanel;
	
	private Dimension windowSize = new Dimension(400, 300);
	
	public LoginView(Controller ctrl, boolean modal) { 
		super(ctrl.getMainFrame(), modal);
		
		this.ctrl = ctrl;
		
		this.loginPanel = new LoginPanel(this.ctrl);
		
		errorPanel = new JPanel();
		errorPanel.setPreferredSize(new Dimension(300, 40));
		
		JLabel errorLabel = new JLabel(AppConstants.LOG_IN_ERROR_TEXT);
		errorLabel.setForeground(Color.RED);
		
		errorPanel.add(errorLabel);
		errorPanel.setVisible(false);
		loginPanel.add(errorPanel);
		
		setContentPane(this.loginPanel);
		setSize(windowSize);
		setPreferredSize(windowSize);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		pack();
		model = ctrl.getModel();
		model.addPropertyChangeListener(loginPanel);
	}
	
	public void showErrorMessage() {
		errorPanel.setVisible(true);
	}
	
	public void hideErrorMessage() {
		errorPanel.setVisible(false);
	}

//	@Override
//	public <T extends Model> void setModel(T model) {
//		this.model = (EmployeeModel) model;
//	}
//
//	@Override
//	public <T extends Model> T getModel() {
//		return (T) model;
//	}
}
