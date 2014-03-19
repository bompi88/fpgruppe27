package view;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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

import controller.MainCtrl;

import resources.ImageManager;

import database.ClientObjectFactory;
import framework.Controller;

import model.Employee;
import model.Meeting;
import model.Participant;
import model.Status;

@SuppressWarnings("serial")
public class ViewParticipantPanel extends JPanel implements PropertyChangeListener {
	
	private JList<Participant> participants;
	private HashSet<Participant> participantList;
	private JLabel participantsLabel;

	public ViewParticipantPanel(HashSet<Participant> model) {
		//setPreferredSize(new Dimension(250, 421));
		setLayout(new GridBagLayout());
		ArrayList<Participant> m = new ArrayList<Participant>(model);
		participants = new JList<Participant>();
		participantsLabel = new JLabel("Deltakere:");
		
		
		addUIElements();
		participants.setCellRenderer(new IconListRenderer());
		
	}
	
	
	private void addUIElements() {
        int anc = GridBagConstraints.WEST; 
        Insets in = new Insets(4,30,4,12);
		add(participantsLabel, new GridBagConstraints(0,0,1,1,1,1,anc,0,in, 0,0));

		add(new JScrollPane(participants), new GridBagConstraints(0,1,1,1,1,1,anc,0,in, 0,0));
	}
	
	public HashSet<Participant> getParticipantList() {
		return participantList;
	}

	
	class IconListRenderer implements ListCellRenderer<Participant> {
	
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("participants")) {
			ArrayList<Participant> teit = (ArrayList<Participant>) evt.getNewValue();
			DefaultListModel<Participant> listModel = new DefaultListModel<>();
			 for(Participant e : teit) {
			         listModel.addElement(e);
			 }	
//			Object[] testing = listModel.toArray();
			 
//			 for(int i = 0; i<testing.length; i++) {
//				 System.out.println((((Participant) testing[i]).getStatus()));
//			 }
//			 
//			 
			 participants.setModel(listModel);
			 participants.setCellRenderer(new IconListRenderer());
			 participants.repaint();
//			 
//			 System.out.println("particpantpanel");
//			 
//			String ourUser = MainCtrl.getCurrentEmployee().getName();
//			for(Participant part : teit) {
//				if(part.getName().equals(ourUser)) {
//					System.out.println(part.getStatus() + "panelll");
//					if(part.getStatus() == Status.ATTENDING) {
//						view.ViewAppointmentView.acceptButton.setEnabled(false);
//						view.ViewAppointmentView.declineButton.setEnabled(true);
//					}
//					else if(part.getStatus() == Status.DECLINED) {
//						view.ViewAppointmentView.acceptButton.setEnabled(true);
//						view.ViewAppointmentView.declineButton.setEnabled(false);
//					}
//				}
//			}

		}
		
	}

}
