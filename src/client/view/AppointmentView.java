package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;

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
import framework.Controller;
import model.MeetingModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import resources.AppConstants;

@SuppressWarnings("serial")
public class AppointmentView extends JPanel {
	
	private Controller ctrl;
	private UtilDateModel dateModelFrom, dateModelTo;
	private JDatePanelImpl datePanelFrom, datePanelTo;
	private JDatePickerImpl datePickerFrom, datePickerTo;
	private JTextField meetingNameField, placeField;
	private JLabel startTimeLabel, endTimeLabel, placeLabel, descLabel; 
	private JTextArea descArea;
	private JScrollPane descScroll;	
	private JComboBox meetingSizeBox, roomPickerBox; 
	private JButton saveButton, cancelButton;
	private JRadioButton placeRadio, roomRadio;
	private ButtonGroup radioGroup;
	private JSpinner timePickerFrom, timePickerTo;	
	private boolean isEditable;
	private ParticipantPanel participantPanel;

	
	public AppointmentView(Controller ctrl) {
		this.ctrl = ctrl;
		ctrl.setModel(new MeetingModel());
		
		placeRadio = new JRadioButton();
		roomRadio = new JRadioButton();
		radioGroup = new ButtonGroup();
		//f¿r var denne Calendar.HOUR_OF_DAY?
		timePickerFrom = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE)); 
		timePickerTo = new JSpinner(new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE));
		saveButton = new JButton("Lagre");
		cancelButton = new JButton("Avbryt");
		dateModelFrom = new UtilDateModel();
		dateModelTo = new UtilDateModel();
		datePanelFrom = new JDatePanelImpl(dateModelFrom);
		datePanelTo = new JDatePanelImpl(dateModelTo);
		meetingNameField = new JTextField(50);
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
		participantPanel = new ParticipantPanel();
		
		radioGroup.add(placeRadio); radioGroup.add(roomRadio);
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (checkInput()) {
					MeetingModel model = ((MeetingModel)getCtrl().getModel());
					model.setDescription(descArea.getText());
					System.out.println(model.getDescription());
					
				}
			}
			
		});
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//do something, aka go back to main calendar window
				
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
	
	public Controller getCtrl() {
		return ctrl;
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

		//adding the labels in the left coloumn
		add(startTimeLabel, new GridBagConstraints(0,0,1,1,1,1,anc,0,in,0,0));
		add(endTimeLabel, new GridBagConstraints(0,1,1,1,1,1,anc,0,in,0,0));
		add(placeLabel, new GridBagConstraints(0,2,1,1,1,1,anc,0,in,0,0));
		add(descLabel, new GridBagConstraints(0,4,1,1,1,1,anc,0,in,0,0));
		
		add(placeField, new GridBagConstraints(1,2,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));

		//adding the date picker and time elements in the middle
		add(timePickerFrom,  new GridBagConstraints(1,0,1,1,1,1,anc,0,in,0,0));
		add(timePickerTo,  new GridBagConstraints(1,1,1,1,1,1,anc,0,in,0,0));
		add(datePickerFrom, new GridBagConstraints(1,0,1,1,1,1,anc,0,new Insets(0,100,0,0),0,0));
		add(datePickerTo, new GridBagConstraints(1,1,1,1,1,1,anc,0,new Insets(0,100,0,0),0,0));
		
		//adding the elements for place description/room choosing
		add(placeRadio,  new GridBagConstraints(1,2,1,1,1,1,anc,0,in,0,0));
		add(roomRadio,  new GridBagConstraints(1,3,1,1,1,1,anc,0,in,0,0));
		add(placeField, new GridBagConstraints(1,2,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));
		add(descScroll,  new GridBagConstraints(1,4,1,1,1,1,anc,0,in,0,0));
		add(meetingSizeBox, new GridBagConstraints(1,3,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));
		add(roomPickerBox, new GridBagConstraints(1,3,1,1,1,1,anc,0,new Insets(0,200,0,0),0,0));
		
		add(saveButton, new GridBagConstraints(1,6,1,1,1,1,anc,0,in,0,0));
		add(cancelButton, new GridBagConstraints(2,6,1,1,1,1,anc,0,in,0,0));
		
		add(participantPanel);

		
	}
	
	private boolean checkInput() {
		return true;
	}
}
