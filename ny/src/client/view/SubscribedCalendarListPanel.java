package view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Employee;
import utils.WrapLayout;
import framework.Observer;

/**
 * Contains the list of calendars the user has subscribed to
 */
@SuppressWarnings("serial")
public class SubscribedCalendarListPanel extends JPanel implements Observer {
	
	// only store once
	private HashSet<Employee> model;
	private List<AddCalendarPanelElement> elements;
	
	private WrapLayout wl;
	
	public SubscribedCalendarListPanel() {
		
		this.model = new HashSet<Employee>();
		this.elements = new ArrayList<AddCalendarPanelElement>();
		this.wl = new WrapLayout(WrapLayout.LEFT,0,0);
		
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setLayout(wl);
		
		setVisible(true);
	}
	
	/**
	 * Adds a calendar to the list
	 * @param e
	 */
	public void addCalendar(Employee emp) {
		if (model.add(emp)) {
			AddCalendarPanelElement element = new AddCalendarPanelElement();
			element.setModel(emp);
			element.addObserver(this);
			elements.add(element);
			add(element);
			updateView();
		}
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
		for (Employee emp : model)
			if(emp != null)
				addCalendar(emp);
	}

	@Override
	public void changeEvent(String event, Object obj) {
		if (event.equals("delete")) {
			if(model.remove(((AddCalendarPanelElement)obj).getModel())) {
				remove((AddCalendarPanelElement)obj);
				updateView();
			}
		}
	}
}
