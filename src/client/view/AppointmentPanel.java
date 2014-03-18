package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import controller.CalendarCtrl;
import controller.MainCtrl;
import framework.Controller;
import model.Employee;
import model.Meeting;
import model.Participant;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;


public class AppointmentPanel extends JPanel implements PropertyChangeListener{
	
	private UtilDateModel dateModelFrom, dateModelTo;
	private JDatePanelImpl datePanelFrom, datePanelTo;
	private JDatePickerImpl datePickerFrom, datePickerTo;
	private JTextField meetingNameField, placeField;
	private JLabel startTimeLabel, endTimeLabel, placeLabel, descLabel, nameLabel; 
	private JTextArea descArea;
	private JScrollPane descScroll;	
	private JComboBox meetingSizeBox, roomPickerBox; 
	private JButton saveButton, cancelButton;
	private JRadioButton placeRadio, roomRadio;
	private ButtonGroup radioGroup;
	private JSpinner timePickerFrom, timePickerTo;	
	private boolean isEditable;
	private Controller ctrl;
	private Meeting meeting;


	public AppointmentPanel(Controller ctrl) {
		this.ctrl = ctrl;
		placeRadio = new JRadioButton();
		roomRadio = new JRadioButton();
		radioGroup = new ButtonGroup();
		//for var denne Calendar.HOUR_OF_DAY?
		timePickerFrom = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.MINUTE)); 
		timePickerTo = new JSpinner(new SpinnerDateModel(new java.util.Date(), null, null, Calendar.MINUTE));
		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Avbryt");
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
		meetingSizeBox = new JComboBox();
		roomPickerBox = new JComboBox();
		nameLabel = new JLabel("Navn:");
				
		radioGroup.add(placeRadio); radioGroup.add(roomRadio);
		placeRadio.setSelected(true);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				descArea.setText("");
				placeField.setText("");
				meetingNameField.setText("");
				ParticipantPanel.participantsModel.clear();
				
				((MainCtrl)((Controller)getCtrl()).getMainCtrl()).setState(CalendarCtrl.class);
				
			}
			
		});
		
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
				    }
				    else if(source == roomRadio) {
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
		
		add(cancelButton, new GridBagConstraints(1,7,1,1,1,1,anc,0,in,0,0));
		
		
	}
	
	public boolean checkInput() {
		try {
			java.util.Date selectedFrom = (java.util.Date) datePickerFrom.getModel().getValue();
			java.util.Date selectedTo = (java.util.Date) datePickerTo.getModel().getValue();
			java.util.Date ye = (java.util.Date) timePickerFrom.getValue();
			java.util.Date ye2 = (java.util.Date) timePickerTo.getValue();
			java.util.Date from = (java.util.Date) new java.util.Date(selectedFrom.getYear(), selectedFrom.getMonth(), selectedFrom.getDate(), ye.getHours(), ye.getMinutes());
			java.util.Date to = (java.util.Date) new java.util.Date(selectedTo.getYear(), selectedTo.getMonth(), selectedTo.getDate(), ye2.getHours(), ye2.getMinutes());
			
			if(!descArea.getText().equals("") && !placeField.getText().equals("") && !meetingNameField.getText().equals("")) {
				if(selectedFrom != null && selectedTo != null && timePickerFrom != null && timePickerTo != null) {
					System.out.println("selectedfrom");
					if(to.compareTo(from) >= 0) {
						System.out.println(from + " " + to);
						return true;
					}
					
				}
				
			}
		}
		catch(Exception e) {
			return false;
		}
		
		return false;
	}
	
	public Meeting createMeeting(Meeting m) {
		m.setName(meetingNameField.getText());
		m.setAppointment(false);
		m.setPlace(placeField.getText());
		m.setDescription(descArea.getText());
		java.util.Date fromDate = (java.util.Date) datePickerFrom.getModel().getValue();
		java.util.Date toDate = (java.util.Date) datePickerTo.getModel().getValue();
		java.util.Date fromTime = (java.util.Date) timePickerFrom.getValue();
		java.util.Date toTime = (java.util.Date) timePickerTo.getValue();
		m.setStartTime(new Timestamp(fromDate.getYear(),fromDate.getMonth(),fromDate.getDate(),fromTime.getHours(),fromTime.getMinutes(),fromTime.getSeconds(),0));
		m.setEndTime(new Timestamp(toDate.getYear(),toDate.getMonth(),toDate.getDate(),toTime.getHours(),toTime.getMinutes(),toTime.getSeconds(),0));
		Employee user = getCtrl().getMainCtrl().getModel();
		//m.setRoom(new Room());
		m.setResponsible(user);	
	
		return m;
		
	}
	
	public Controller getCtrl() {
		return ctrl;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Meeting model = ctrl.getModel();
		if (evt.getPropertyName() == "name") {
			meetingNameField.setText(model.getName());
		}
		else if(evt.getPropertyName() == "place") {
			placeField.setText(model.getPlace());
		}
		
		else if(evt.getPropertyName() == "startTime") {
			dateModelFrom.setValue(model.getStartTime());
			timePickerFrom.getModel().setValue(model.getStartTime());

		}
		
		else if(evt.getPropertyName() == "endTime") {
			dateModelTo.setValue(model.getEndTime());
			timePickerTo.getModel().setValue(model.getEndTime());
		}
		
		else if(evt.getPropertyName() == "description") {
			descArea.setText(model.getDescription());
		}		

		
	}



}
