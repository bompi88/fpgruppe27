package view;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import resources.ImageManager;

import model.Participant;
import model.Status;

@SuppressWarnings("serial")
public class ViewParticipantPanel extends JPanel {
	
	private JList<Participant> participantList;
	private HashSet<Participant> participants;
	private JLabel participantsLabel;
	private DefaultListModel<Participant> listModel;
	
	public ViewParticipantPanel() {
		setLayout(new GridBagLayout());
		participantList = new JList<Participant>();
		participantsLabel = new JLabel("Deltakere:");
		
		addUIElements();
		participantList.setCellRenderer(new IconListRenderer());
	}

	private void addUIElements() {
        int anc = GridBagConstraints.WEST; 
        Insets in = new Insets(4,30,4,12);
		add(participantsLabel, new GridBagConstraints(0,0,1,1,1,1,anc,0,in, 0,0));

		add(new JScrollPane(participantList), new GridBagConstraints(0,1,1,1,1,1,anc,0,in, 0,0));
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
	
	public void setParticipantModel(List<Participant> participantModel) {
		
		participants = new HashSet<Participant>(participantModel);
		
		listModel = new DefaultListModel<Participant>();
		
		for(Participant e : participants) {
			listModel.addElement(e);
		}
		
		participantList.setModel(listModel);
		participantList.setCellRenderer(new IconListRenderer());
		participantList.repaint();
	}
	
	public List<Participant> getParticipantModel() {
		return new ArrayList<Participant>(participants);
	}
}
