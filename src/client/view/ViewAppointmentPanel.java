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
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import controller.CalendarCtrl;
import controller.MainCtrl;

import model.Meeting;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import framework.Controller;

public class ViewAppointmentPanel extends JPanel implements PropertyChangeListener {
	
	private JTextField meetingNameField, placeField, startTimeField, endTimeField, ownerField;
	private JLabel startTimeLabel, endTimeLabel, placeLabel, descLabel, nameLabel, ownerLabel; 
	private JTextArea descArea;
	private JScrollPane descScroll;	
	private JButton saveButton, cancelButton;
	private boolean isEditable;
	private Controller ctrl;
	private Meeting model;
	
	public ViewAppointmentPanel(Meeting model) {
		this.model = model;
		//for var denne Calendar.HOUR_OF_DAY?
		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Avbryt");
		meetingNameField = new JTextField(12);
		placeField = new JTextField(12);
		startTimeLabel = new JLabel("Starttid:");
		endTimeLabel= new JLabel("Sluttid:");
		placeLabel  = new JLabel("Sted:");
		descLabel= new JLabel("Beskrivelse:");
		descArea = new JTextArea(4, 15);
		descScroll  = new JScrollPane(descArea);
		nameLabel = new JLabel("Navn:");
		ownerLabel = new JLabel("Møteinnkaller: ");
		startTimeField = new JTextField(19);
		endTimeField = new JTextField(19);
		ownerField = new JTextField(12);
				
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Skal sendes tilbake til calnderView
				
//				descArea.setText("");
//				placeField.setText("");
//				meetingNameField.setText("");
//				ParticipantPanel.participantsModel.clear();
//				
//				((MainCtrl)((Controller)getCtrl()).getMainCtrl()).setState(CalendarCtrl.class);
				
			}
			
		});
		

		disableIUElements();
		addUIElements();
	}
	
	private void disableIUElements() {
		saveButton.setEnabled(false);
		cancelButton.setEnabled(false);
		meetingNameField.setEditable(false);
		placeField.setEditable(false);
		descArea.setEditable(false);
		startTimeField.setEditable(false);
		endTimeField.setEditable(false);
		ownerField.setEditable(false);
	}
	
	private void addUIElements() {
		setLayout(new GridBagLayout());
        Insets in = new Insets(4,12,4,12); //padding
        int anc = GridBagConstraints.WEST; 
        
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        

		//adding the labels in the left column
        add(nameLabel, new GridBagConstraints(0,0,1,1,1,1,anc,0,in,0,0));
        add(ownerLabel, new GridBagConstraints(0,1,1,1,1,1,anc,0,in,0,0));
        add(ownerField, new GridBagConstraints(1,1,1,1,1,1,anc,0,in,0,0));


        
		add(startTimeLabel, new GridBagConstraints(0,2,1,1,1,1,anc,0,in,0,0));
		add(endTimeLabel, new GridBagConstraints(0,3,1,1,1,1,anc,0,in,0,0));
		add(placeLabel, new GridBagConstraints(0,4,1,1,1,1,anc,0,in,0,0));
		add(descLabel, new GridBagConstraints(0,6,1,1,1,1,anc,0,in,0,0));
		
        add(meetingNameField, new GridBagConstraints(1,0,1,1,1,1,anc,0,in,0,0));

		//adding the date picker and time elements in the middle
		add(startTimeField,  new GridBagConstraints(1,2,1,1,1,1,anc,0,in,0,0));
		add(endTimeField,  new GridBagConstraints(1,3,1,1,1,1,anc,0,in,0,0));
		
		//adding the elements for place description/room choosing
		add(placeField, new GridBagConstraints(1,4,1,1,1,1,anc,0,in,0,0));
		add(descScroll,  new GridBagConstraints(1,6,1,1,1,1,anc,0,in,0,0));		
		
	}
	
	public Controller getCtrl() {
		return ctrl;
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName() == "name") {
			meetingNameField.setText(model.getName());
		}
		else if(evt.getPropertyName() == "place") {
			placeField.setText(model.getPlace());
		}
		
		else if(evt.getPropertyName() == "startTime") {
			SimpleDateFormat dt = new SimpleDateFormat("EEEE dd. MMMM yyyy, 'kl.' hh:mm"); 
			startTimeField.setText(dt.format(model.getStartTime()));
		}
		
		else if(evt.getPropertyName() == "endTime") {
			SimpleDateFormat dt = new SimpleDateFormat("EEEE dd. MMMM yyyy, 'kl.' hh:mm"); 
			endTimeField.setText(dt.format(model.getEndTime()));
		}
		
		else if(evt.getPropertyName() == "description") {
			descArea.setText(model.getDescription());
		}		
		
		else if(evt.getPropertyName() == "responsible") {
			ownerField.setText(model.getResponsible().getName());
		}

		
	}

}
