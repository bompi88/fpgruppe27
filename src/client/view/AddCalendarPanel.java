package view;

import java.awt.BorderLayout;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;

import model.EmployeeModel;

import framework.Model;

import resources.AppConstants;
import resources.ImageManager;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class AddCalendarPanel extends JPanel {
	
	private JLabel addCalendarLabel;
	private JTextField addCalendarTextField;
	private JList<EmployeeModel> calendarSubscribedList;
	private JButton addCalendarButton;
	
	private DefaultListModel<EmployeeModel> subscribedCalendars = new DefaultListModel<EmployeeModel>();
	
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
		calendarSubscribedList.setCellRenderer(new MyCellRenderer());
		calendarSubscribedList.setModel(subscribedCalendars);
		
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
		if(subscribedCalendars.getSize() < 12) {
			EmployeeModel emp = new EmployeeModel();
			emp.setName(addCalendarTextField.getText());
			subscribedCalendars.add(subscribedCalendars.getSize(), emp);
		}
	}
	
	public void removeCalendar(int index) {
		subscribedCalendars.remove(index);
	}
	
	@Override
	public Container getParent() {
		return super.getParent();
	}
	
	public void fillSizeOfParent() {
		setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * (1 - AppConstants.HEADER_TITLE_PANEL_SCALE_WIDTH)/2)+1, getParent().getPreferredSize().height));
	}
	
	private static class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {

		private ImageIcon removeCalendarIcon;
        private static final long serialVersionUID = 1L;
        
        public MyCellRenderer() {
        	removeCalendarIcon = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon"), 15, 15));
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Model) {
            	EmployeeModel employee = (EmployeeModel) value;
                setText(employee.getName());
                
                JLabel l = new JLabel("");
                setIcon(removeCalendarIcon);
        
                
                add(l);
                setForeground(Color.black);
                setBackground(AppConstants.HEADER_BG_COLOR);
                
                setHorizontalAlignment(SwingConstants.LEFT);
                setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

                setFont(list.getFont());
                
                setOpaque(true);
            }
            return this;
        }
    }
}