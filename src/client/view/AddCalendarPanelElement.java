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

import model.Employee;
import resources.AppConstants;
import resources.ImageManager;
import framework.Observable;
import framework.Observer;

@SuppressWarnings("serial")
public class AddCalendarPanelElement extends JPanel implements Observable{
	
	private ImageIcon removeCalendarIcon;
	private ImageIcon removeCalendarIconHover;
	private JLabel removeCalendarButton = new JLabel();
    private List<Observer> observers = new ArrayList<Observer>(); 
	private JLabel userLabel = new JLabel();
	
	private Employee emp;
    
	@SuppressWarnings("static-access")
	public AddCalendarPanelElement(Employee emp) {
		setVisible(true);
    	removeCalendarIcon = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon"), 15, 15));
    	removeCalendarIconHover = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon_hover"), 15, 15));
    	
    	removeCalendarButton.setIcon(removeCalendarIcon);
    	setPreferredSize(new Dimension((int)(300/4),20));
    	setLayout(new GridBagLayout());
    	this.emp = emp;
    	
    	userLabel.setText(this.emp.getName());
        
        add(userLabel, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.NORTHEAST,0,new Insets(0,0,0,5),0,0));
        add(removeCalendarButton, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,0,new Insets(0,0,0,0),0,0));
        setForeground(Color.black);
        setBackground(AppConstants.HEADER_BG_COLOR);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        //userLabel.setHorizontalAlignment(SwingConstants.LEFT);
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        
    	removeCalendarButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
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
	public Object getSelf() {
		return this;
	}
	
	public Employee getModel() {
		return emp;
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