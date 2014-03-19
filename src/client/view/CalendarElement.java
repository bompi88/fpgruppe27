package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import model.Meeting;
import resources.AppConstants;
import resources.ImageManager;
import utils.RoundedPanel;
import framework.Observable;
import framework.Observer;

@SuppressWarnings("serial")
public class CalendarElement extends RoundedPanel implements PropertyChangeListener, Observable {
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	private JLabel meetingTitle;
	private JLabel fromTimeLabel = new JLabel();
	private JLabel toTimeLabel = new JLabel();
	
	private Meeting model;
	private JLabel deleteButton;
	private ImageIcon normalDeleteIcon;
	private ImageIcon hoverDeleteIcon;
	private Calendar cal = Calendar.getInstance();
	private Color color; 
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	@SuppressWarnings({ "static-access", "deprecation" })
	public CalendarElement(Meeting model, Date thisDate, Color color) {
		this.model = model;
		this.color = color; 
		
		deleteButton = new JLabel();
		
		normalDeleteIcon = ImageManager.getInstance().getDeleteIcon();
		hoverDeleteIcon = ImageManager.getInstance().getDeleteIconHover();
		deleteButton.setIcon(normalDeleteIcon);
		
		setLayout(new GridBagLayout());
		
		Font meetTitleFont = new Font("Arial", Font.BOLD, 12);
		
		setBackground(color);
		meetingTitle = new JLabel(model.getName());
		ImageManager.getInstance();
		meetingTitle.setIcon(ImageManager.getLetterClosedIcon());
		meetingTitle.setFont(meetTitleFont);
		meetingTitle.setMinimumSize(new Dimension(95, 20));
		meetingTitle.setPreferredSize(new Dimension(95, 20));
		meetingTitle.setMaximumSize(new Dimension(95, 20));
		
		cal.setTime(thisDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		
		long early = cal.getTimeInMillis();
		
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		
		long late = cal.getTimeInMillis();
		
		// create different labels based on startTime and endtime
		if(model.getStartTime().getTime() < early && model.getEndTime().getTime() > late) {
			fromTimeLabel = new JLabel(AppConstants.WHOLE_DAY_TEXT);
		} else {
			if(model.getStartTime().getTime() <= early) {
				fromTimeLabel.setText("<-");
			} else {
				fromTimeLabel.setText(parseTime(model.getStartTime().getHours()) + ":" + parseTime(model.getStartTime().getMinutes()));
			}
			if(model.getEndTime().getTime() >= late) {
				toTimeLabel.setText("->");
			} else {
				toTimeLabel.setText(parseTime(model.getEndTime().getHours()) + ":" + parseTime(model.getEndTime().getMinutes()));
			}
		}
		
		add(meetingTitle, new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.NORTHWEST,0,new Insets(5,5,0,0),0,0));
		add(deleteButton, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHEAST,0,new Insets(5,0,0,7),0,0));
		
		add(fromTimeLabel, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.SOUTHWEST,0,new Insets(0,5,10,0),0,0));
		add(toTimeLabel, new GridBagConstraints(0,1,1,1,1,1,GridBagConstraints.SOUTHEAST,0,new Insets(0,0,10,5),0,0));
		
		deleteButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				fireObserverEvent("delete",this);
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				deleteButton.setIcon(hoverDeleteIcon);
				setBackground(AppConstants.MEETING_BOX_COLOR_HOVER_DELETE);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				deleteButton.setIcon(normalDeleteIcon);
				setBackground(getColor());
			}
			
		});
		
		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				setBackground(getColor());
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				setBackground(CalendarColumn.choiceColor(getModel(), true));
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				fireObserverEvent("view_appointment", this);
			}
		});
	}
	
	public String parseTime(int timeVar) {
		if(timeVar < 10) {
			return "0" + timeVar;
		}
		return "" + timeVar;
	}

	public Meeting getModel() {
		return model;
	}

	public void setModel(Meeting model) {
		this.model = model;
		model.addPropertyChangeListener(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("name")) {
			meetingTitle.setText(model.getName());
		}
	}

	@Override
	public void addObserver(Observer ob) {
		observers.add(ob);
		
	}

	@Override
	public void fireObserverEvent(String event, Object obj) {
		for (Observer ob : observers) {
			ob.changeEvent(event, this);
		}
	}
}
