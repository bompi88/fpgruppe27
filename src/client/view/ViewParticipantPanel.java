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

import resources.ImageManager;

import database.ClientObjectFactory;
import framework.Controller;

import model.Employee;
import model.Meeting;
import model.Participant;
import model.Status;

@SuppressWarnings("serial")
public class ViewParticipantPanel extends JPanel implements PropertyChangeListener {
	
	private JComboBox<Participant> participantPicker;
	private JList<Participant> participants;
	private JButton addParticipant;
	private JButton addExternals;
	protected static DefaultListModel<Participant> participantsModel; 
	private HashSet<Participant> participantList;

	public ViewParticipantPanel(HashSet<Participant> model) {
		//setPreferredSize(new Dimension(250, 421));
		setLayout(new GridBagLayout());
		participantsModel = new DefaultListModel<Participant>();
		addParticipant = new JButton("Legg til");
		addExternals = new JButton("Legg til eksterne deltakere");
		participantList = model;
		participants = new JList<Participant>();
		participants.setModel(participantsModel);
		//participantPicker = new JComboBox();
		
		List<Participant> employees = ClientObjectFactory.getEmployeesAsParticipants();
		participantPicker = new JComboBox<Participant>(employees.toArray(new Participant[employees.size()]));
		
		addParticipant.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(participantPicker.getSelectedIndex() != -1) {
					Participant m = (Participant) participantPicker.getSelectedItem();
					m.setStatus(Status.INVITED);
					participantsModel.addElement(m);
					participantList.add(m);
				}
				
			}
			
		});
		
		addExternals.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Desktop desktop;
				if (Desktop.isDesktopSupported() 
				    && (desktop = Desktop.getDesktop()).isSupported(Desktop.Action.MAIL)) {
				
						URI mailto;
						try {
							mailto = new URI("mailto:ola@nordmann.no?subject=Moteinvitasjon&body=Du%20er%20invitert%20til%20mote");
							desktop.mail(mailto);

						} catch (URISyntaxException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				

				
				}
			}
		});
		
		disableUIElements();
		
		addUIElements();
		participants.setCellRenderer(new IconListRenderer());
		
	}
	
	private void disableUIElements() {
		participantPicker.setEnabled(false);
		participantPicker.setVisible(false);
		addParticipant.setEnabled(false);
		addParticipant.setVisible(false);
		addExternals.setEnabled(false);
		addExternals.setVisible(false);
		participants.setEnabled(false);
	}
	
	private void addUIElements() {
        int anc = GridBagConstraints.WEST; 
        Insets in = new Insets(4,12,4,12);

		add(participantPicker, new GridBagConstraints(0,0,1,1,1,1,anc,0,in, 0,0));
		add(new JScrollPane(participants), new GridBagConstraints(0,1,1,1,1,1,anc,0,in, 0,0));
		add(addParticipant, new GridBagConstraints(1,0,1,1,1,1,anc,0,in, 0,0));
		add(addExternals, new GridBagConstraints(0,2,1,1,1,1,anc,0,in, 0,0));
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