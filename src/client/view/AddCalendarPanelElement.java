package view;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import framework.Observable;
import framework.Observer;

import model.Employee;
import resources.AppConstants;
import resources.ImageManager;

@SuppressWarnings("serial")
public class AddCalendarPanelElement extends JPanel implements Observable {
	
	private List<Observer> observers;
	
	private ImageIcon removeCalendarIcon;
	private ImageIcon removeCalendarIconHover;
	private JLabel removeCalendarButton;
	private JLabel userLabel;
	
	private Employee model;
    
	@SuppressWarnings("static-access")
	public AddCalendarPanelElement() {
		
		this.observers = new ArrayList<Observer>();
		this.model = new Employee();
		this.removeCalendarButton = new JLabel();
		this.userLabel = new JLabel();
		this.removeCalendarIcon = ImageManager.getInstance().getDeleteIcon();
    	this.removeCalendarIconHover = ImageManager.getInstance().getDeleteIconHover();
    	this.removeCalendarButton.setIcon(removeCalendarIcon);
		
    	// init panel
    	setForeground(Color.black);
        setBackground(AppConstants.HEADER_BG_COLOR);
        setBorder(new EmptyBorder(0, 0, 0, 0));
    	setPreferredSize(new Dimension((int)(300/3),20));
    	setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    	setLayout(new GridBagLayout());
    	setVisible(true);
    	
    	// init and add gui elements
    	userLabel.setText(this.model.getName());
        
        add(userLabel, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.NORTHEAST,0,new Insets(0,0,0,5),0,0));
        add(removeCalendarButton, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,0,new Insets(0,0,0,0),0,0));
        
        
        removeCalendarButton.addMouseListener(new MouseListener() {
    		
    		@Override
    		public void mouseReleased(MouseEvent e) {
    		}
    		
    		@Override
    		public void mousePressed(MouseEvent e) {
    		}
    		
    		@Override
    		public void mouseExited(MouseEvent e) {
    			removeCalendarButton.setIcon(removeCalendarIcon);
    		}
    		
    		@Override
    		public void mouseEntered(MouseEvent e) {
    			removeCalendarButton.setIcon(removeCalendarIconHover);
    		}
    		
    		@Override
    		public void mouseClicked(MouseEvent e) {
    			fireObserverEvent("remove_calendar", getSelf());
    		}
    	});
    }
	
	public void setRemoveSubscribedCalendarButtonListener(MouseListener listener) {
		removeCalendarButton.addMouseListener(listener);
	}
	
	public void setDeleteButtonHover(boolean isHover) {
		if (isHover)
			removeCalendarButton.setIcon(removeCalendarIconHover);
		else
			removeCalendarButton.setIcon(removeCalendarIcon);
	}
	
	public void setModel(Employee model) {
		this.model = model;
		update();
	}
	
	public void update() {
		userLabel.setText(model.getName());
	}
	
	public Employee getModel() {
		return model;
	}
	
	public AddCalendarPanelElement getSelf() {
		return this;
	}

	@Override
	public void addObserver(Observer ob) {
		observers.add(ob);
	}

	@Override
	public void fireObserverEvent(String event, Object obj) {
		for (Observer ob : observers)
			ob.changeEvent("delete", getSelf());
	}
}
