package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.EmployeeModel;
import framework.Model;
import framework.Observable;
import framework.Observer;

import resources.AppConstants;
import resources.ImageManager;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class AddCalendarPanel extends JPanel implements Observer {
	
	private JLabel addCalendarLabel;
	private JTextField addCalendarTextField;
	private JList<EmployeeModel> calendarSubscribedList;
	private JButton addCalendarButton;
	
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
		
		MyCellRenderer cellRenderer = new MyCellRenderer();
		cellRenderer.addObserver(this);
		calendarSubscribedList.setCellRenderer(cellRenderer);
		
		calendarSubscribedList.setModel(subscribedCalendarsModel);
		
		calendarSubscribedList.setFixedCellHeight(15);
		calendarSubscribedList.setFixedCellWidth(85);
		calendarSubscribedList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		calendarSubscribedList.setVisibleRowCount(4);
		calendarSubscribedList.setPreferredSize(new Dimension(250,70));
		
		JPanel topFormWrapper = new JPanel();
		topFormWrapper.setLayout(new GridBagLayout());
		topFormWrapper.add(addCalendarLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, anc, 0, new Insets(0, 25, 0, 0), 0, 20));
		topFormWrapper.add(addCalendarTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1, anc, 0, new Insets(5, 0, 0, 0), 0, 0));
		topFormWrapper.add(addCalendarButton, new GridBagConstraints(2, 0, 1, 1, 1, 1, anc, 0, new Insets(5, 0, 0, 0), 0, 0));
		add(topFormWrapper);
		add(calendarSubscribedList);
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
		
		if(subscribedCalendarsModel.getSize() < 12 && !addCalendarTextField.getText().equals("") && addCalendarTextField.getText() != null) {
			
			EmployeeModel emp = new EmployeeModel();
			
			try {
				if(((EmployeeModel)emp.fetch(addCalendarTextField.getText())).getUsername().equals(addCalendarTextField.getText())) {
				
					emp.setUsername(addCalendarTextField.getText());
					emp.setName(addCalendarTextField.getText());
					subscribedCalendars.add(emp);
					subscribedCalendarsModel.removeAllElements();
					for (EmployeeModel ee : subscribedCalendars) {
						subscribedCalendarsModel.add(subscribedCalendarsModel.size(), ee);
					}
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
	
	private static class MyCellRenderer extends JPanel implements ListCellRenderer<Object>, Observable {

		private ImageIcon removeCalendarIcon;
        private static final long serialVersionUID = 1L;
        private JLabel calendarOwnerText = new JLabel();
        private JLabel l = new JLabel("");
        private List<Observer> observers = new ArrayList<Observer>(); 
        private EmployeeModel employee;
        
        public JLabel getL() {
			return l;
		}

		public MyCellRenderer() {
        	removeCalendarIcon = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon"), 15, 15));
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Model) {
            	
            	setLayout(new GridBagLayout());
            	employee = (EmployeeModel) value;
            	
            	calendarOwnerText.setText(employee.getName());
                
                l.setIcon(removeCalendarIcon);
                
                l.addMouseListener(new MouseListener() {
					
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
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						
						fireObserverEvent("remove_calendar");
					}
				});
                
                add(l, new GridBagConstraints(1,0,1,1,1,1,GridBagConstraints.NORTHEAST,0,new Insets(0,0,0,5),0,0));
                add(calendarOwnerText, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.NORTHWEST,0,new Insets(0,0,0,0),0,0));
                setForeground(Color.black);
                setBackground(AppConstants.HEADER_BG_COLOR);
                setBorder(new EmptyBorder(0, 0, 0, 0));
                
                calendarOwnerText.setHorizontalAlignment(SwingConstants.LEFT);
                //setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                setFont(list.getFont());
                
                setOpaque(true);
            }
            return this;
        }

		@Override
		public void addObserver(Observer ob) {
			observers.add(ob);
		}

		@Override
		public void fireObserverEvent(String event) {

			for (Observer o : observers) {
				o.changeEvent(event, employee);
			}
		}
    }

	@Override
	public void changeEvent(String event, Object obj) {
		if(event.equals("remove_calendar")) {
			removeCalendar((EmployeeModel)obj);
		}
	}
}