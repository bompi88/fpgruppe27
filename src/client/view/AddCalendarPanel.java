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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.EmployeeModel;
import framework.Observable;
import framework.Observer;

import resources.AppConstants;
import utils.RelativeLayout;
import utils.WrapLayout;

@SuppressWarnings("serial")
public class AddCalendarPanel extends JPanel {
	
	private JLabel addCalendarLabel;
	private JTextField addCalendarTextField;
	private JList<EmployeeModel> calendarSubscribedList;
	private JButton addCalendarButton;
	private CalendarListPanel p;
	
	HashSet<EmployeeModel> subscribedCalendars = new HashSet<EmployeeModel>();
	private DefaultListModel<EmployeeModel> subscribedCalendarsModel = new DefaultListModel<EmployeeModel>();
	
	public AddCalendarPanel() {
		
		int anc = GridBagConstraints.NORTHWEST;
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.CENTER);
		setLayout(rl);
		
		addCalendarLabel = new JLabel(AppConstants.SHOW_CALENDAR_LABEL_TEXT);
		addCalendarTextField = new JTextField();
		addCalendarTextField.setPreferredSize(new Dimension(100, 30));
		
		addCalendarButton = new JButton(AppConstants.SHOW_OTHER_CALENDARS_BUTTON_TEXT);
	
		calendarSubscribedList = new JList<EmployeeModel>();
		
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

	public void addCalendar() {
		
		if(((HashSet<EmployeeModel>) p.getModel()).size() < 12 && !addCalendarTextField.getText().equals("") && addCalendarTextField.getText() != null) {
			
			EmployeeModel emp = new EmployeeModel();
			
			try {
				if(((EmployeeModel)emp.fetch(addCalendarTextField.getText())).getUsername().equals(addCalendarTextField.getText())) {
				
					emp.setUsername(addCalendarTextField.getText());
					emp.setName(addCalendarTextField.getText());
					//p.modelsubscribedCalendars.add(emp);
					p.changeEvent("add_calendar", emp);
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void removeCalendar(int index) {
		subscribedCalendarsModel.remove(index);
	}
	
	public void removeCalendar(EmployeeModel obj) {
		subscribedCalendars.remove(obj);
	}
	
	@Override
	public Container getParent() {
		return super.getParent();
	}
	
	public void fillSizeOfParent() {
		setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - AppConstants.HEADER_TITLE_PANEL_SCALE_WIDTH)/2)+1, getParent().getPreferredSize().height));
	}
	
	public static class CalendarListPanel extends JPanel implements Observer, Observable {
		
		private HashSet<EmployeeModel> model;
		public HashSet<EmployeeModel> getModel() {
			return model;
		}

		public void setModel(HashSet<EmployeeModel> model) {
			this.model = model;
		}

		private List<AddCalendarPanelElement> elements = new ArrayList<AddCalendarPanelElement>();
		private List<Observer> observers = new ArrayList<Observer>();
		
		public CalendarListPanel(HashSet<EmployeeModel> model) {
			this.model = model;
			
			EmployeeModel em = new EmployeeModel();
			em.setUsername("test");
			em.setName("test");
			model.add(em);
			setVisible(true);
			setBorder(new EmptyBorder(0, 0, 0, 0));
			WrapLayout wl = new WrapLayout(WrapLayout.LEFT,0,0);
			setLayout(wl);
			
			for(EmployeeModel e : model) {
				if(e != null)
					addCalendar(e);
			}
		}
		
		@Override
		public void changeEvent(String event, Object obj) {
			if(event.equals("remove_calendar")) {
				if(model.remove(((AddCalendarPanelElement)obj).getModel())) {
					//fireObserverEvent(event, obj);
					//removeCalendar((EmployeeModel)obj);
					System.out.println((((AddCalendarPanelElement)obj).getModel()).getName() + " deleted.");
					remove((AddCalendarPanelElement)obj);
					updateView();
				}
				
			} else if(event.equals("add_calendar")) {
				EmployeeModel e = (EmployeeModel)obj;
				if (model.add(e)) {
					addCalendar(e);
					updateView();
					//removeCalendar((EmployeeModel)obj);
					System.out.println(((EmployeeModel)obj).getName() + " added.");
				}
			}
		}
		
		public void addCalendar(EmployeeModel e) {
			AddCalendarPanelElement d = new AddCalendarPanelElement(e);
			d.addObserver(this);
			elements.add(d);
			add(d);
		}
		
		public void updateView() {
			repaint();
			revalidate();
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