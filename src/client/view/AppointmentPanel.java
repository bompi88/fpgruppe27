package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
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

import controller.MainCtrl;
import model.Employee;
import model.Meeting;
import model.Room;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;


@SuppressWarnings("serial")
public class AppointmentPanel extends JPanel implements PropertyChangeListener{
	
	private Calendar cal = Calendar.getInstance();
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
	private Meeting model;


	public AppointmentPanel(Meeting model) {
		this.model = model;
		
		placeRadio = new JRadioButton();
		roomRadio = new JRadioButton();
		radioGroup = new ButtonGroup();
		//for var denne Calendar.HOUR_OF_DAY?
		timePickerFrom = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE)); 
		timePickerTo = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));

		dateModelFrom = new UtilDateModel();
		dateModelTo = new UtilDateModel();
		datePanelFrom = new JDatePanelImpl(dateModelFrom);
		datePanelTo = new JDatePanelImpl(dateModelTo);
		meetingNameField = new JTextField(12);
		placeField = new JTextField(12);
		startTimeLabel = new JLabel("Starttid:");
		endTimeLabel= new JLabel("Sluttid:");
		placeLabel  = new JLabel("Sted:");
		descLabel= new JLabel("Beskrivelse:");
		descArea = new JTextArea(4, 15);
		descScroll  = new JScrollPane(descArea);
		datePickerFrom = new JDatePickerImpl(datePanelFrom);
		datePickerTo = new JDatePickerImpl(datePanelTo);
		meetingSizeBox = new JComboBox<Meeting>();
		roomPickerBox = new JComboBox<Room>();
		nameLabel = new JLabel("Navn:");
				
		radioGroup.add(placeRadio); radioGroup.add(roomRadio);
		placeRadio.setSelected(true);
		
		class MyItemListener implements ItemListener {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object source = e.getItemSelectable();
				
				if (source == placeRadio) {
					meetingSizeBox.setEnabled(false);
					roomPickerBox.setEnabled(false);
					//meetingSizeBox.setSelectedIndex(0);
					//roomPickerBox.setSelectedIndex(0);
					placeField.setEnabled(true);
				} else if(source == roomRadio) {
					meetingSizeBox.setEnabled(true);
				    roomPickerBox.setEnabled(true);
				    placeField.setEnabled(false);
				}				
			}
		}
		
		placeRadio.addItemListener(new MyItemListener());
		roomRadio.addItemListener(new MyItemListener());		
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
	
	/**
	 * Checks if input fields have the right values
	 * @return
	 */
	public boolean checkInput() {
		
		Date from = getFromTimeAsDate();
		Date to = getToTimeAsDate();
		
		// check if fields is filled in
		if(!descArea.getText().equals("") && !placeField.getText().equals("") && !meetingNameField.getText().equals("")) {
			if(datePickerTo.getModel().getValue() != null && datePickerFrom.getModel().getValue() != null && timePickerFrom.getValue() != null && timePickerTo.getValue() != null) {
				if(to.compareTo(from) >= 0) {
					return true;
				}	
			}
		}
		
		return false;
	}
	
	/**
	 * Gets the time as a Date, which is selected in the "From time and date pickers".
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getFromTimeAsDate() {
		
		cal.set(Calendar.YEAR, datePickerFrom.getModel().getYear());
		cal.set(Calendar.MONTH, datePickerFrom.getModel().getMonth());
		cal.set(Calendar.DATE, datePickerFrom.getModel().getDay());
		cal.set(Calendar.HOUR_OF_DAY, ((Date)timePickerFrom.getValue()).getHours());
		cal.set(Calendar.MINUTE, ((Date)timePickerFrom.getValue()).getMinutes());
		
		return cal.getTime();
	}
	
	/**
	 * Gets the time as a Date, which is selected in the "To time and date pickers".
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getToTimeAsDate() {
		
		cal.set(Calendar.YEAR, datePickerTo.getModel().getYear());
		cal.set(Calendar.MONTH, datePickerTo.getModel().getMonth());
		cal.set(Calendar.DATE, datePickerTo.getModel().getDay());
		cal.set(Calendar.HOUR_OF_DAY, ((Date)timePickerTo.getValue()).getHours());
		cal.set(Calendar.MINUTE, ((Date)timePickerTo.getValue()).getMinutes());
		
		return cal.getTime();
	}
	
	/**
	 * Gets the time as a SQL Timestamp, which is selected in the "To time and date pickers".
	 * @return
	 */
	public Timestamp getToTimeAsTimestamp() {
		return new Timestamp(getToTimeAsDate().getTime());
	}
	
	/**
	 * Gets the time as a SQL Timestamp, which is selected in the "From time and date pickers".
	 * @return
	 */
	public Timestamp getFromTimeAsTimestamp() {
		return new Timestamp(getFromTimeAsDate().getTime());
	}
	
	public void setModel(Meeting m) {
		model = m;
	}
	
	public void saveModel() {
		
		// set textual fields
		model.setName(meetingNameField.getText());
		model.setPlace(placeField.getText());
		model.setDescription(descArea.getText());
		
		// set time
		model.setStartTime(getFromTimeAsTimestamp());
		model.setEndTime(getToTimeAsTimestamp());
		
		// set the responsible of meeting
		Employee user = MainCtrl.getCurrentEmployee();
		model.setResponsible(user);	
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "name") {
			meetingNameField.setText(model.getName());
		} else if(evt.getPropertyName() == "place") {
			placeField.setText(model.getPlace());
		} else if(evt.getPropertyName() == "startTime") {
			dateModelFrom.setValue(model.getStartTime());
			timePickerFrom.getModel().setValue(model.getStartTime());
		} else if(evt.getPropertyName() == "endTime") {
			dateModelTo.setValue(model.getEndTime());
			timePickerTo.getModel().setValue(model.getEndTime());
		} else if(evt.getPropertyName() == "description") {
			descArea.setText(model.getDescription());
		}		
	}
}
