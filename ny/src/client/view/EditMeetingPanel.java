package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import resources.AppConstants;

import model.Meeting;
import model.Room;
import net.sourceforge.jdatepicker.DateModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

@SuppressWarnings("serial")
public class EditMeetingPanel extends JPanel {
	
	private UtilDateModel dateModelFrom, dateModelTo;
	private JDatePanelImpl datePanelFrom, datePanelTo;
	private JDatePickerImpl datePickerFrom, datePickerTo;
	private JTextField meetingNameField, placeField;
	private JLabel startTimeLabel, endTimeLabel, placeLabel, descLabel, nameLabel; 
	private JTextArea descArea;
	private JScrollPane descScroll;	
	private JComboBox<Meeting> meetingSizeBox;
	private JComboBox<Room> roomPickerBox;
	private JRadioButton placeRadio, roomRadio;
	private ButtonGroup radioGroup;
	private JSpinner timePickerFrom, timePickerTo;
	
	public EditMeetingPanel() {
		this.placeRadio = new JRadioButton();
		this.roomRadio = new JRadioButton();
		this.radioGroup = new ButtonGroup();

		this.timePickerFrom = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE)); 
		this.timePickerTo = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));

		this.dateModelFrom = new UtilDateModel();
		this.dateModelTo = new UtilDateModel();
		this.datePanelFrom = new JDatePanelImpl(dateModelFrom);
		this.datePanelTo = new JDatePanelImpl(dateModelTo);
		this.meetingNameField = new JTextField(12);
		this.placeField = new JTextField(12);
		this.startTimeLabel = new JLabel(AppConstants.START_TIME_TEXT);
		this.endTimeLabel= new JLabel(AppConstants.END_TIME_TEXT);
		this.placeLabel  = new JLabel(AppConstants.PLACE_TEXT);
		this.descLabel= new JLabel(AppConstants.DESCRIPTION_TEXT);
		this.descArea = new JTextArea(4, 15);
		this.descScroll  = new JScrollPane(descArea);
		this.datePickerFrom = new JDatePickerImpl(datePanelFrom);
		this.datePickerTo = new JDatePickerImpl(datePanelTo);
		this.meetingSizeBox = new JComboBox<Meeting>();
		this.roomPickerBox = new JComboBox<Room>();
		this.nameLabel = new JLabel("Navn:");
				
		this.radioGroup.add(placeRadio);
		this.radioGroup.add(roomRadio);
		
		this.placeRadio.setSelected(true);
		
		this.placeRadio.addItemListener(new MyItemListener());
		this.roomRadio.addItemListener(new MyItemListener());	
		
		addUIElements();
	}
	
	private void addUIElements() {
		
		setLayout(new GridBagLayout());
        Insets in = new Insets(4,12,4,12); //padding
        
        int anc = GridBagConstraints.WEST; 
        
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        timePickerFrom.setEditor(new JSpinner.DateEditor(timePickerFrom, "HH:mm"));
        timePickerTo.setEditor(new JSpinner.DateEditor(timePickerTo, "HH:mm"));

		//adding the labels in the left column
        add(nameLabel, new GridBagConstraints(0,0,1,1,1,1,anc,0,in,0,0));
		add(startTimeLabel, new GridBagConstraints(0,1,1,1,1,1,anc,0,in,0,0));
		add(endTimeLabel, new GridBagConstraints(0,2,1,1,1,1,anc,0,in,0,0));
		add(placeLabel, new GridBagConstraints(0,3,1,1,1,1,anc,0,in,0,0));
		add(descLabel, new GridBagConstraints(0,5,1,1,1,1,anc,0,in,0,0));
		
        add(meetingNameField, new GridBagConstraints(1,0,1,1,1,1,anc,0,in,0,0));

		//adding the date picker and time elements in the middle
		add(timePickerFrom,  new GridBagConstraints(1,1,1,1,1,1,anc,0,in,0,0));
		add(timePickerTo,  new GridBagConstraints(1,2,1,1,1,1,anc,0,in,0,0));
		add(datePickerFrom, new GridBagConstraints(1,1,1,1,1,1,anc,0,new Insets(0,100,0,0),0,0));
		add(datePickerTo, new GridBagConstraints(1,2,1,1,1,1,anc,0,new Insets(0,100,0,0),0,0));
		
		//adding the elements for place description/room choosing
		add(placeRadio,  new GridBagConstraints(1,3,1,1,1,1,anc,0,in,0,0));
		add(roomRadio,  new GridBagConstraints(1,4,1,1,1,1,anc,0,in,0,0));
		add(placeField, new GridBagConstraints(1,3,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));
		add(descScroll,  new GridBagConstraints(1,5,1,1,1,1,anc,0,in,0,0));
		add(meetingSizeBox, new GridBagConstraints(1,4,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));
		add(roomPickerBox, new GridBagConstraints(1,4,1,1,1,1,anc,0,new Insets(0,200,0,0),0,0));
	}
	
	class MyItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			
			if (source == placeRadio) {
				meetingSizeBox.setEnabled(false);
				roomPickerBox.setEnabled(false);
				placeField.setEnabled(true);
			} else if(source == roomRadio) {
				meetingSizeBox.setEnabled(true);
			    roomPickerBox.setEnabled(true);
			    placeField.setEnabled(false);
			}				
		}
	}
	
	public void setModel(Meeting meeting) {
		meetingNameField.setText(meeting.getName());
		placeField.setText(meeting.getPlace());
		descArea.setText(meeting.getDescription());
		
		dateModelFrom.setValue(meeting.getStartTime());
		timePickerFrom.getModel().setValue(meeting.getStartTime());
		dateModelTo.setValue(meeting.getEndTime());
		timePickerTo.getModel().setValue(meeting.getEndTime());
		
		if(meeting.getRoom().getRoomID() != 0) {
			roomRadio.setSelected(true);
		} else {
			placeRadio.setSelected(true);
		}
	}
	
	public String getMeetingName() {
		return meetingNameField.getText();
	}
	
	public String getPlace() {
		return placeField.getText();
	}
	
	public String getDescription() {
		return descArea.getText();
	}
	
	public DateModel getFromDate() {
		return datePickerFrom.getModel();
	}
	
	public DateModel getToDate() {
		return datePickerTo.getModel();
	}
	
	public Date getFromTime() {
		return (Date)timePickerFrom.getValue();
	}
	
	public Date getToTime() {
		return (Date)timePickerTo.getValue();
	}
	
	public Room getRoom() {
		return (Room)roomPickerBox.getSelectedItem();
	}
}
