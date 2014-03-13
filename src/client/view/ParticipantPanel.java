package view;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import database.DatabaseSearch;
import model.EmployeeModel;
import model.ParticipantModel;

public class ParticipantPanel extends JPanel {
	
	private JComboBox participantPicker;
	private JList participants;
	private JButton addParticipant;
	private JButton addExternals;
	protected static DefaultListModel<EmployeeModel> participantsModel; 


	public ParticipantPanel() {
		//setPreferredSize(new Dimension(250, 421));
		setLayout(new GridBagLayout());
		participantsModel = new DefaultListModel();
		addParticipant = new JButton("Legg til");
		addExternals = new JButton("Legg til eksterne deltakere");
		participants = new JList();
		participants.setModel(participantsModel);
		//participantPicker = new JComboBox();
		try {
			List<ParticipantModel> employees = DatabaseSearch.getAllEmployees();
			participantPicker = new JComboBox(employees.toArray());
			
			
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
					ParticipantModel m = (ParticipantModel) participantPicker.getSelectedItem();
					participantsModel.addElement(m);
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
		
		addUIElements();
		participants.setCellRenderer(new IconListRenderer());
		
	}
	
	private void addUIElements() {
        int anc = GridBagConstraints.WEST; 
        Insets in = new Insets(4,12,4,12);

		add(participantPicker, new GridBagConstraints(0,0,1,1,1,1,anc,0,in, 0,0));
		add(new JScrollPane(participants), new GridBagConstraints(0,1,1,1,1,1,anc,0,in, 0,0));
		add(addParticipant, new GridBagConstraints(1,0,1,1,1,1,anc,0,in, 0,0));
		add(addExternals, new GridBagConstraints(0,2,1,1,1,1,anc,0,in, 0,0));
	}
	

	public DefaultListModel getParticipantsModel() {
		return participantsModel;
	}

	public void setParticipantsModel(DefaultListModel participantsModel) {
		this.participantsModel = participantsModel;
	}
	
	class IconListRenderer implements ListCellRenderer<ParticipantModel> {
	
			@Override
			public Component getListCellRendererComponent(JList list,
					ParticipantModel value, int index, boolean isSelected,
					boolean cellHasFocus) {
				String name = value.getName();
				JLabel label = new JLabel(name);
				label.setOpaque(true);
				
				ImageIcon attIcon = new ImageIcon("src/client/resources/PersonIconGreen.png");
				ImageIcon decIcon = new ImageIcon("src/client/resources/PersonIconRed.png");
				ImageIcon invIcon = new ImageIcon("src/client/resources/personIconYellow.png");
				
				Image img = invIcon.getImage();  
				Image newimg = img.getScaledInstance(10, 10,  java.awt.Image.SCALE_SMOOTH);  
				ImageIcon newIcon = new ImageIcon(newimg);  


				
				label.setIcon(newIcon);
				
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
