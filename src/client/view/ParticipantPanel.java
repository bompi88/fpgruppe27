package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;

import database.DatabaseSearch;
import model.EmployeeModel;

public class ParticipantPanel extends JPanel {
	
	private JComboBox participantPicker;
	private JList participants;
	private JButton addParticipant;
	private DefaultListModel participantsModel; 

	public ParticipantPanel() {
		participantsModel = new DefaultListModel();
		addParticipant = new JButton("Legg til");
		participants = new JList();
		try {
			List andreas = DatabaseSearch.getAllEmployees();
			participantPicker = new JComboBox(andreas.toArray());
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		addParticipant.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(participantPicker.getSelectedIndex() != -1) {
					EmployeeModel m = (EmployeeModel) participantPicker.getSelectedItem();
					participantsModel.addElement(m);
				}
				
			}
			
		});
		addUIElements();
		
	}
	
	private void addUIElements() {
		add(participantPicker);
		add(participants);
		add(addParticipant);
	}
	

}
