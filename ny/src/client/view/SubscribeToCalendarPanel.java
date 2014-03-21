package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.CalendeerClient;

import database.ClientObjectFactory;

import resources.AppConstants;
import utils.RelativeLayout;

import model.Employee;

@SuppressWarnings("serial")
public class SubscribeToCalendarPanel extends JPanel {

	private JLabel addCalendarLabel;
	private JTextField addCalendarTextField;
	private JButton addCalendarButton;
	private JPanel topFormWrapper;
	private SubscribedCalendarListPanel calendarListPanel;
	
	private RelativeLayout rl;
	private int anchor;
	
	public SubscribeToCalendarPanel() {
		this.addCalendarButton = new JButton(AppConstants.SHOW_OTHER_CALENDARS_BUTTON_TEXT);
		this.addCalendarLabel = new JLabel(AppConstants.SHOW_CALENDAR_LABEL_TEXT);
		this.addCalendarTextField = new JTextField();
		this.topFormWrapper = new JPanel();
		this.calendarListPanel = new SubscribedCalendarListPanel();
		this.rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		this.rl.setAlignment(RelativeLayout.CENTER);
		
		this.anchor = GridBagConstraints.NORTHWEST;
		
		setBackground(AppConstants.HEADER_BG_COLOR);
		setLayout(rl);
		
		addCalendarTextField.setPreferredSize(new Dimension(100, 30));
		
		calendarListPanel.setBackground(AppConstants.HEADER_BG_COLOR);
		calendarListPanel.setPreferredSize(new Dimension(300,70));
		
		topFormWrapper.add(addCalendarLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, anchor, 0, new Insets(0, 25, 0, 0), 0, 20));
		topFormWrapper.add(addCalendarTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1, anchor, 0, new Insets(5, 0, 0, 0), 0, 0));
		topFormWrapper.add(addCalendarButton, new GridBagConstraints(2, 0, 1, 1, 1, 1, anchor, 0, new Insets(5, 0, 0, 0), 0, 0));
		topFormWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		topFormWrapper.setLayout(new GridBagLayout());
		
		add(topFormWrapper);
		add(calendarListPanel);
	}
	
	/**
	 * Subscribe to a calendar.
	 */
	public void addCalendar() {
		
		// check if number of subscribed calendars is below 12 and if input text is not empty
		if(((HashSet<Employee>) calendarListPanel.getModel()).size() < 9 && !addCalendarTextField.getText().equals("") && addCalendarTextField.getText() != null) {
			if (!addCalendarTextField.getText().equals(CalendeerClient.getCurrentEmployee().getUsername())) {
				Employee emp = (Employee)ClientObjectFactory.getEmployeeByUsername(addCalendarTextField.getText());
				
				// if employee exists, add calendar
				if(emp != null && emp.getUsername() != null && emp.getUsername().equals(addCalendarTextField.getText())) {
					calendarListPanel.addCalendar(emp);
				}
			}
		}
	}
	
	public HashSet<Employee> getModel() {
		return calendarListPanel.getModel();
	}

	public void setModel(HashSet<Employee> model) {	
		calendarListPanel.setModel(model);
	}
	
	public List<Employee> getAllSubscriptions() {
		return new ArrayList<Employee>(calendarListPanel.getModel());
	}
	
	public void addSubscribeCalendarButtonListener(ActionListener listener) {
		addCalendarButton.addActionListener(listener);
	}
	
	public void addSubscribeCalendarTextFieldListener(KeyListener listener) {
		addCalendarTextField.addKeyListener(listener);
	}
	
	public void fillSizeOfParent() {
		setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - AppConstants.TITLE_BAR_SCALE_WIDTH)/2)+1, getParent().getPreferredSize().height));
	}
}
