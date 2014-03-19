package view;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
public class ParticipantPanel extends JPanel implements PropertyChangeListener {
	
	private JComboBox<Participant> participantPicker;
	private JList<Participant> participants;
	private JButton addParticipantButton;
	private JButton removeParticipantButton;
	private JButton addExternalParticipantsButton;
	private DefaultListModel<Participant> participantsModel; 
	private HashSet<Participant> participantList;

	public ParticipantPanel(HashSet<Participant> model) {
		this.participantList = model;
		
		setLayout(new GridBagLayout());
		
		// init UI elements
		addParticipantButton = new JButton(AppConstants.ADD_BUTTON_TEXT);
		removeParticipantButton = new JButton(AppConstants.REMOVE_BUTTON_TEXT);
		addExternalParticipantsButton = new JButton(AppConstants.ADD_EXTERNAL_PARTICIPANTS_BUTTON_TEXT);
		
		participantsModel = new DefaultListModel<Participant>();
		participants = new JList<Participant>();
		participants.setModel(participantsModel);
		participants.setPreferredSize(new Dimension(250, 421));
		
		// get all possible participants
		List<Participant> employees = ClientObjectFactory.getEmployeesAsParticipants();
		participantPicker = new JComboBox<Participant>(employees.toArray(new Participant[employees.size()]));
		
		addParticipantButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(participantPicker.getSelectedIndex() != -1) {
					Participant m = (Participant) participantPicker.getSelectedItem();
					m.setStatus(Status.INVITED);
					if (participantList.add(m))
						participantsModel.addElement(m);
				}
			}
		});
		
		removeParticipantButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Participant m = (Participant) participantPicker.getSelectedItem();
				ClientObjectFactory.deleteParticipant(m.getUsername(),m.getMeetid());
				
				if (participantList.remove(m))
					participantsModel.removeElement(m);
			}
		});
		
		addExternalParticipantsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop;
				if (Desktop.isDesktopSupported() && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
				
					URI mailto;
					
					try {
						mailto = new URI("mailto:ola@nordmann.no?subject=Moteinvitasjon&body=Du%20er%20invitert%20til%20mote");
						desktop.mail(mailto);
					} catch (URISyntaxException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		
		addUIElements();
		participants.setCellRenderer(new IconListRenderer());
	}
	
	private void addUIElements() {
        int anc = GridBagConstraints.WEST; 
        Insets in = new Insets(4,12,4,12);

		add(participantPicker, new GridBagConstraints(0,0,1,1,1,1,anc,0,in, 0,0));
		add(new JScrollPane(participants), new GridBagConstraints(0,1,1,1,1,1,anc,0,in, 0,0));
		add(removeParticipantButton, new GridBagConstraints(0,0,1,1,1,1,GridBagConstraints.EAST,0,in, 0,0));
		add(addParticipantButton, new GridBagConstraints(1,0,1,1,1,1,anc,0,in, 0,0));
		add(addExternalParticipantsButton, new GridBagConstraints(0,2,1,1,1,1,anc,0,in, 0,0));
	}
	

	public DefaultListModel<Participant> getParticipantsModel() {
		return participantsModel;
	}
	
	public HashSet<Participant> getParticipantList() {
		return participantList;
	}

	public void setParticipantsModel(DefaultListModel<Participant> participantsModel) {
		this.participantsModel = participantsModel;
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName() == "participants") {
			DefaultListModel<Participant> listModel = new DefaultListModel<>();
			 for(Participant e : participantList) {
			         listModel.addElement(e);
			 }		
			 participants.setModel(listModel);
		}
		
	}

}
