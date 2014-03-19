package view;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import database.ClientObjectFactory;

import model.Employee;
import framework.Observable;
import framework.Observer;

import resources.AppConstants;
import utils.RelativeLayout;
import utils.WrapLayout;

/**
 * AddCalendarPanel is the panel in calendar view which take care of the subscriptions
 * to other calendars. 
 */
@SuppressWarnings("serial")
public class AddCalendarPanel extends JPanel {
	
	private JLabel addCalendarLabel;
	private JTextField addCalendarTextField;
	private static JList<Employee> calendarSubscribedList;
	private JButton addCalendarButton;
	private CalendarListPanel p;
	
	private static HashSet<Employee> subscribedCalendars = new HashSet<Employee>();
	private DefaultListModel<Employee> subscribedCalendarsModel = new DefaultListModel<Employee>();
	
	public AddCalendarPanel() {
		
		int anc = GridBagConstraints.NORTHWEST;
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.CENTER);
		setLayout(rl);
		
		addCalendarLabel = new JLabel(AppConstants.SHOW_CALENDAR_LABEL_TEXT);
		addCalendarTextField = new JTextField();
		addCalendarTextField.setPreferredSize(new Dimension(100, 30));
		
		addCalendarButton = new JButton(AppConstants.SHOW_OTHER_CALENDARS_BUTTON_TEXT);
	
		calendarSubscribedList = new JList<Employee>();
		
		p = new CalendarListPanel(subscribedCalendars);
		p.setBackground(AppConstants.HEADER_BG_COLOR);
		p.setPreferredSize(new Dimension(300,70));
		
		JPanel topFormWrapper = new JPanel();
		topFormWrapper.setLayout(new GridBagLayout());
		topFormWrapper.add(addCalendarLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, anc, 0, new Insets(0, 25, 0, 0), 0, 20));
		topFormWrapper.add(addCalendarTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1, anc, 0, new Insets(5, 0, 0, 0), 0, 0));
		topFormWrapper.add(addCalendarButton, new GridBagConstraints(2, 0, 1, 1, 1, 1, anc, 0, new Insets(5, 0, 0, 0), 0, 0));
		add(topFormWrapper);
		add(p);
		calendarSubscribedList.setBackground(AppConstants.HEADER_BG_COLOR);
		topFormWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		setBackground(AppConstants.HEADER_BG_COLOR);
		
		addCalendarButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				addCalendar();
			}
		});
		
		addCalendarTextField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					addCalendar();
				}
			}
		});
	}

	public static List<Employee> getAllSubscriptions() {
		return new ArrayList<Employee>(subscribedCalendars);
	}
	
	/**
	 * Subscribe to a calendar.
	 */
	public void addCalendar() {
		
		// check if number of subscribed calendars is below 12 and if input text is not empty
		if(((HashSet<Employee>) p.getModel()).size() < 9 && !addCalendarTextField.getText().equals("") && addCalendarTextField.getText() != null) {
			
			Employee emp = (Employee)ClientObjectFactory.getEmployeeByUsername(addCalendarTextField.getText());
			
			// if employee exists
			if(emp != null && emp.getUsername() != null && emp.getUsername().equals(addCalendarTextField.getText())) {
				// fire a change event named "add_calendar"
				p.changeEvent("add_calendar", emp);
			}
		}
	}
	
	public void removeCalendar(int index) {
		subscribedCalendarsModel.remove(index);
	}
	
	public void removeCalendar(Employee obj) {
		subscribedCalendars.remove(obj);
	}
	
	@Override
	public Container getParent() {
		return super.getParent();
	}
	
	public void fillSizeOfParent() {
		setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - AppConstants.HEADER_TITLE_PANEL_SCALE_WIDTH)/2)+1, getParent().getPreferredSize().height));
	}
	
	/**
	 * Contains the list of calendars the user has subscribed to
	 */
	public static class CalendarListPanel extends JPanel implements Observer, Observable {
		
		// only store once
		private HashSet<Employee> model;
		private List<AddCalendarPanelElement> elements = new ArrayList<AddCalendarPanelElement>();
		private List<Observer> observers = new ArrayList<Observer>();
		
		public CalendarListPanel(HashSet<Employee> model) {
			this.model = model;
			
			Employee em = new Employee();
			em.setUsername("test");
			em.setName("test");
			model.add(em);
			setVisible(true);
			setBorder(new EmptyBorder(0, 0, 0, 0));
			WrapLayout wl = new WrapLayout(WrapLayout.LEFT,0,0);
			setLayout(wl);
			
			for(Employee e : model) {
				if(e != null)
					addCalendar(e);
			}
		}
		
		@Override
		public void changeEvent(String event, Object obj) {
			// remove calendar
			if(event.equals("remove_calendar")) {
				if(model.remove(((AddCalendarPanelElement)obj).getModel())) {
					remove((AddCalendarPanelElement)obj);
					updateView();
				}
			// add calendar
			} else if(event.equals("add_calendar")) {
				Employee e = (Employee)obj;
				if (model.add(e)) {
					addCalendar(e);
					updateView();
				}
			}
		}
		
		/**
		 * Adds a calendar to the list
		 * @param e
		 */
		public void addCalendar(Employee e) {
			AddCalendarPanelElement d = new AddCalendarPanelElement(e);
			d.addObserver(this);
			elements.add(d);
			add(d);
		}
		
		/**
		 * Update and repaint the view
		 */
		public void updateView() {
			repaint();
			revalidate();
		}
		
		public HashSet<Employee> getModel() {
			return model;
		}

		public void setModel(HashSet<Employee> model) {
			this.model = model;
		}

		@Override
		public void addObserver(Observer ob) {
			observers.add(ob);
		}

		@Override
		public void fireObserverEvent(String event, Object obj) {
			for (Observer o : observers) {
				o.changeEvent(event, obj);
			}
		}
	}
}