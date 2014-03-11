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
	
	private DefaultListModel<EmployeeModel> subscribedCalendars = new DefaultListModel<EmployeeModel>();
	
	public AddCalendarPanel() {
		
		int anc = GridBagConstraints.NORTHWEST;
		
		setLayout(new RelativeLayout(RelativeLayout.Y_AXIS, 0));
		
		EmployeeModel e = new EmployeeModel();
		e.setName("teeeeee");
		subscribedCalendars.add(subscribedCalendars.getSize(),e);
		subscribedCalendars.add(subscribedCalendars.getSize(),e);
		subscribedCalendars.add(subscribedCalendars.getSize(),e);
		subscribedCalendars.add(subscribedCalendars.getSize(),e);
		subscribedCalendars.add(subscribedCalendars.getSize(),e);
		
		addCalendarLabel = new JLabel(AppConstants.SHOW_CALENDAR_LABEL_TEXT);
		addCalendarTextField = new JTextField();
		addCalendarTextField.setPreferredSize(new Dimension(100, 30));
		
		calendarSubscribedList = new JList<EmployeeModel>();
		calendarSubscribedList.setCellRenderer(new MyCellRenderer());
		calendarSubscribedList.setModel(subscribedCalendars);
		
		calendarSubscribedList.setFixedCellHeight(15);
		calendarSubscribedList.setFixedCellWidth(100);
		calendarSubscribedList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		calendarSubscribedList.setVisibleRowCount(0);
		calendarSubscribedList.setPreferredSize(new Dimension(200,50));
		
		JPanel topFormWrapper = new JPanel();
		topFormWrapper.setLayout(new GridBagLayout());
		topFormWrapper.add(addCalendarLabel, new GridBagConstraints(0, 0, 1, 1, 1, 1, anc, 0, new Insets(0, 20, 0, 0), 0, 20));
		topFormWrapper.add(addCalendarTextField, new GridBagConstraints(1, 0, 1, 1, 1, 1, anc, 0, new Insets(5, 0, 0, 0), 0, 0));
		add(topFormWrapper);
		add(calendarSubscribedList);
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
		private JButton btn;
        private static final long serialVersionUID = 1L;
        
        public MyCellRenderer() {
        	btn = new JButton("x");
        	removeCalendarIcon = new ImageIcon(ImageManager.getInstance().resizeImage(ImageManager.getInstance().getImage("delete_icon"), 20, 20));
        }
        
        @Override
        public Component getListCellRendererComponent(JList list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            if (value instanceof Model) {
            	EmployeeModel employee = (EmployeeModel) value;
                setText(employee.getName());
                
                setForeground(Color.black);
               
                //setHorizontalAlignment(SwingConstants.LEFT);
                //setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
                //setPreferredSize(new Dimension(50, 15));
                //setEnabled(list.isEnabled());
                setFont(list.getFont());
                setIcon(removeCalendarIcon);
                setOpaque(true);
            }
            return this;
        }
    }
}