package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import model.EmployeeModel;
import model.MeetingModel;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import resources.AppConstants;

@SuppressWarnings("serial")
public class AppointmentView extends JPanel {
	private Controller ctrl;
	private JLabel whereLabel = new JLabel(AppConstants.APPOINTMENT_HEADER_TEXT);
	private JTextField meetingNameField = new JTextField(50);
	private JTextField placeField = new JTextField(12);
	private JLabel startTimeLabel = new JLabel("Starttid:");
	private JLabel endTimeLabel = new JLabel("Sluttid:");
	private JLabel placeLabel = new JLabel("Sted:");
	private JLabel descLabel = new JLabel("Beskrivelse:");
	private JTextArea descArea = new JTextArea(4, 15);
	JScrollPane descScroll = new JScrollPane(descArea);
	 String[] numbers = {"maks antall","5","10","15","20","25","50","75","100"};
	 String[] rooms = {"velg rom", "testrom1", "testrom2"};
	
	private JComboBox meetingSizeBox = new JComboBox(numbers);
	private JComboBox roomPickerBox = new JComboBox(rooms);

	private UtilDateModel dateModelFrom = new UtilDateModel();
	private UtilDateModel dateModelTo = new UtilDateModel();
	private JDatePanelImpl datePanelFrom = new JDatePanelImpl(dateModelFrom);
	private JDatePanelImpl datePanelTo = new JDatePanelImpl(dateModelTo);

	private JDatePickerImpl datePickerFrom = new JDatePickerImpl(datePanelFrom);
	private JDatePickerImpl datePickerTo = new JDatePickerImpl(datePanelTo);
	private JButton saveButton = new JButton("Lagre");
	private JButton cancelButton = new JButton("Avbryt");
	private JRadioButton placeRadio = new JRadioButton();
	private JRadioButton roomRadio = new JRadioButton();
	private ButtonGroup buttonGroup = new ButtonGroup();
	
	private Date date = new Date();
	private SpinnerDateModel sm1 = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);
	private SpinnerDateModel sm2 = new SpinnerDateModel(date, null, null, Calendar.HOUR_OF_DAY);

	private JSpinner timePickerFrom = new JSpinner(sm1);
	private JSpinner timePickerTo = new JSpinner(sm2);


	
	public AppointmentView(Controller ctrl) {
		this.ctrl = ctrl;
		ctrl.setModel(new MeetingModel());
		
		addUIElements();
		
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
        
        JSpinner.DateEditor de = new JSpinner.DateEditor(timePickerFrom, "HH:mm");
        timePickerFrom.setEditor(de);
        JSpinner.DateEditor de2 = new JSpinner.DateEditor(timePickerTo, "HH:mm");
        timePickerTo.setEditor(de2);


        
        buttonGroup.add(placeRadio);
        buttonGroup.add(roomRadio);
       

		//adding the labels in the left coloumn
		add(startTimeLabel, new GridBagConstraints(0,0,1,1,1,1,anc,0,in,0,0));
		add(endTimeLabel, new GridBagConstraints(0,1,1,1,1,1,anc,0,in,0,0));
		add(placeLabel, new GridBagConstraints(0,2,1,1,1,1,anc,0,in,0,0));
		add(descLabel, new GridBagConstraints(0,4,1,1,1,1,anc,0,in,0,0));
		
		//adding the date picker and time elements in the middle
		add(timePickerFrom,  new GridBagConstraints(1,0,1,1,1,1,anc,0,in,0,0));
		add(timePickerTo,  new GridBagConstraints(1,1,1,1,1,1,anc,0,in,0,0));

		add(datePickerFrom, new GridBagConstraints(1,0,1,1,1,1,anc,0,new Insets(0,100,0,0),0,0));
		add(datePickerTo, new GridBagConstraints(1,1,1,1,1,1,anc,0,new Insets(0,100,0,0),0,0));
		
		//adding the elements for place description/room choosing
		add(placeRadio,  new GridBagConstraints(1,2,1,1,1,1,anc,0,in,0,0));
		add(roomRadio,  new GridBagConstraints(1,3,1,1,1,1,anc,0,in,0,0));
		add(placeField, new GridBagConstraints(1,2,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));
		add(meetingSizeBox, new GridBagConstraints(1,3,1,1,1,1,anc,0,new Insets(0,40,0,0),0,0));
		add(roomPickerBox, new GridBagConstraints(1,3,1,1,1,1,anc,0,new Insets(0,200,0,0),0,0));
		add(descScroll,  new GridBagConstraints(1,4,1,1,1,1,anc,0,in,0,0));
		
		//adding the buttons down below
		add(saveButton, new GridBagConstraints(1,6,1,1,1,1,anc,0,in,0,0));
		add(cancelButton, new GridBagConstraints(2,6,1,1,1,1,anc,0,in,0,0));

		
	}
	
	private boolean checkInput() {
		return true;
	}
}
