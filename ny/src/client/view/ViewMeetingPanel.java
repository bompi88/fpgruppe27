package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controller.CalendeerClient;

import model.Meeting;
import model.Participant;

@SuppressWarnings("serial")
public class ViewMeetingPanel extends JPanel {

	private JTextField meetingNameField, placeField, startTimeField, endTimeField, ownerField;
	private JLabel startTimeLabel, endTimeLabel, placeLabel, descLabel, nameLabel, ownerLabel; 
	private JTextArea descArea;
	private JScrollPane descScroll;	
	private JButton saveButton, cancelButton;
	private Meeting meeting;
	
	public ViewMeetingPanel() {

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
	
	public void setMeetingModel(Meeting model) {
		meeting = model;
		meetingNameField.setText(model.getName());
		placeField.setText(model.getPlace());
		SimpleDateFormat ds = new SimpleDateFormat("EEEE dd. MMMM yyyy, 'kl.' hh:mm"); 
		startTimeField.setText(ds.format(model.getStartTime()));
		SimpleDateFormat de = new SimpleDateFormat("EEEE dd. MMMM yyyy, 'kl.' hh:mm"); 
		endTimeField.setText(de.format(model.getEndTime()));
		descArea.setText(model.getDescription());
		ownerField.setText(model.getResponsible().getName());
	}
	
	public Meeting getMeetingModel() {
		return meeting;
	}
}
