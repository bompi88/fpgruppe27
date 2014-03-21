package view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import resources.AppConstants;
import resources.ImageManager;
import database.ClientObjectFactory;

import model.Participant;
import model.Status;

@SuppressWarnings("serial")
public class EditParticipantPanel extends JPanel {

	private JComboBox<Participant> participantPicker;
	private JList<Participant> participantsList;
	private JButton addParticipantButton;
	private JButton removeParticipantButton;
	private JButton addExternalParticipantsButton;
	private DefaultListModel<Participant> participantsListModel; 
	private HashSet<Participant> participants;
	
	public EditParticipantPanel() {
		
		setLayout(new GridBagLayout());
		
		participants = new HashSet<Participant>();
		// init UI elements
		addParticipantButton = new JButton(AppConstants.ADD_BUTTON_TEXT);
		removeParticipantButton = new JButton(AppConstants.REMOVE_BUTTON_TEXT);
		addExternalParticipantsButton = new JButton(AppConstants.ADD_EXTERNAL_PARTICIPANTS_BUTTON_TEXT);
		
		participantsListModel = new DefaultListModel<Participant>();
		participantsList = new JList<Participant>();

		participantsList.setModel(participantsListModel);
		participantsList.setPreferredSize(new Dimension(250, 421));
		
		// get all possible participants
		List<Participant> employees = ClientObjectFactory.getEmployeesAsParticipants();
		participantPicker = new JComboBox<Participant>(employees.toArray(new Participant[employees.size()]));
		
		addUIElements();
		participantsList.setCellRenderer(new IconListRenderer());
	}
	
	private void addUIElements() {
        int anc = GridBagConstraints.WEST; 
        Insets in = new Insets(4,12,4,12);

		add(participantPicker, new GridBagConstraints(0,0,1,1,1,1,anc,0,in, 0,0));
		add(new JScrollPane(participantsList), new GridBagConstraints(0,1,1,1,1,1,anc,0,in, 0,0));
		add(removeParticipantButton, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.EAST,0,in, 0,0));
		add(addParticipantButton, new GridBagConstraints(1,0,1,1,1,1,anc,0,in, 0,0));
		add(addExternalParticipantsButton, new GridBagConstraints(0,2,1,1,1,1,anc,0,in, 0,0));
	}
	
	public void addAddExternalParticipantButtonListener(ActionListener listener) {
		addExternalParticipantsButton.addActionListener(listener);
	}
	
	public void addRemoveParticipantButtonListener(ActionListener listener) {
		removeParticipantButton.addActionListener(listener);
	}
	
	public void addAddParticipantButtonListener(ActionListener listener) {
		addParticipantButton.addActionListener(listener);
	}
	
	public void setModel(List<Participant> participantModel) {
		participantsListModel.clear();
		participants = new HashSet<Participant>(participantModel);
		
		participantModel = new ArrayList<Participant>(participants);
		for (int i = 0; i < participantModel.size(); i++)
			participantsListModel.add(i, participantModel.get(i));
	}
	
	public List<Participant> getParticipants() {
		
		HashSet<Participant> parts = new HashSet<Participant>();
		
		for (int i = 0; i < participantsListModel.size(); i++)
			parts.add(participantsListModel.get(i));
		
		return new ArrayList<Participant>(parts);
	}
	
	public Participant getParticipant() {
		return (Participant)participantPicker.getSelectedItem();
	}
	
	public void addParticipant(Participant p) {
		if (participants.add(p))
			participantsListModel.addElement(p);
	}
	
	public void removeParticipant(Participant p) {
		if (participants.remove(p))
			participantsListModel.removeElement(p);
	}	

	class IconListRenderer implements ListCellRenderer<Participant> {
		
		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list,
				Participant value, int index, boolean isSelected,
				boolean cellHasFocus) {
			String name = value.getName();
			JLabel label = new JLabel(name);
			label.setOpaque(true);
			
			ImageManager.getInstance();
			
			// get icons
			ImageIcon attIcon = ImageManager.getAttendingIcon();
			ImageIcon decIcon = ImageManager.getDeclinedIcon();
			ImageIcon invIcon = ImageManager.getInvitedIcon();
			
			// Set correct icon
			if (value.getStatus() == Status.INVITED) {
				label.setIcon(invIcon);
			} else if (value.getStatus() == Status.ATTENDING) {
				label.setIcon(attIcon);
			} else {
				label.setIcon(decIcon);
			}
			
			if(isSelected) {
				label.setBackground(list.getSelectionBackground());
			}
			else {
				label.setForeground(list.getForeground());
				label.setBackground(list.getBackground());
			}
			
			return label;
		}	
	}
}
