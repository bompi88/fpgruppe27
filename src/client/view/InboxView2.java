package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import controller.AppointmentCtrl;
import controller.MainCtrl;
import framework.Controller;
import framework.State;

import model.MeetingModel;
import model.MessageModel;
import model.ParticipantModel;

import resources.AppConstants;
import utils.RelativeLayout;
import view.CalendarView.CalendarTitlePanel;

@SuppressWarnings("serial")
public class InboxView2 extends JPanel {
	
	float titleBarScaleWidth = 0.33f;
	
	private JPanel topPanelWrapper = new JPanel();
	private InboxListPanel inbox; 
	
	private DefaultListModel<MessageModel> messages;
	
	private Controller ctrl;
	
	protected GridBagConstraints c;
	
	public InboxView2(Controller ctrl) {
		this.ctrl = ctrl;
		
		inbox = new InboxListPanel();
		
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.CENTER);
		setLayout(rl);
		
		RelativeLayout rl2 = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl2.setAlignment(RelativeLayout.LEADING);
		
		topPanelWrapper.setLayout(rl2);
		topPanelWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		topPanelWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		
		add(topPanelWrapper);
		
		InboxTitlePanel inboxTitlePanel = new InboxTitlePanel();
		topPanelWrapper.add(inboxTitlePanel);
		
		inboxTitlePanel.fillSizeOfParent();
		
		add(inbox);
	}
	
	private DefaultListModel<MessageModel> getAllMessages() {
		// Skal hente all beskjeder til denne brukeren
		
		// Test messages
//		MeetingModel meeting1 = new MeetingModel();
//		meeting1.setMeetingName("InboxtestMøte");
//		meeting1.setStartDate(new Date(1,1,1));
//		MessageModel message1 = null;
//		MessageModel message2 = null;
//		MessageModel message3 = null;
//		try {
//			message1 = new MessageModel(meeting1, "meetingCreated", new ParticipantModel(), new ParticipantModel());
//			message2 = new MessageModel(meeting1, "meetingCreated", new ParticipantModel(), new ParticipantModel());
//			message3 = new MessageModel(meeting1, "meetingCreated", new ParticipantModel(), new ParticipantModel());
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		DefaultListModel<MessageModel> messages = new DefaultListModel<MessageModel>();
//		messages.add(0,message1);
//		messages.add(0,message2);
//		messages.add(0,message3);
//		
//		for (int i = 2; i < 40; i++) {
//			MessageModel message = null;
//			try {
//				message = new MessageModel(meeting1, "meetingCreated", new ParticipantModel(), new ParticipantModel());
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			messages.add(0, message);
//		}
		
		return messages;
	}
	
	public class InboxTitlePanel extends JPanel {
		
		public InboxTitlePanel() {
			setBorder(BorderFactory.createEmptyBorder(30, -2, 0, 0));
			JLabel title = new JLabel(AppConstants.INBOX_HEADER_TEXT);
			title.setFont(new Font("Arial", Font.PLAIN, 28));
			setBackground(AppConstants.HEADER_BG_COLOR);
			add(title);
		}
		
		@Override
		public Container getParent() {
			// TODO Auto-generated method stub
			return super.getParent();
		}
		
		public void fillSizeOfParent() {
			setPreferredSize(new Dimension((int)(getParent().getPreferredSize().width * titleBarScaleWidth)+1, getParent().getPreferredSize().height));
		}
	}
	
	public class InboxListPanel extends JPanel implements ListSelectionListener {
		
		public InboxListPanel() {
			
			MeetingModel meeting1 = new MeetingModel();
			meeting1.setMeetingName("InboxtestMøte");
			meeting1.setStartDate(new Date(1,1,1));
			meeting1.setStartTime(new Time(0,0,0));
			meeting1.setPlace("hemmelig møtested");
			
			MeetingModel meeting2 = new MeetingModel();
			meeting2.setName("Et annet møte");
			meeting2.setStartDate(new Date(2,3,4));
			meeting2.setStartTime(new Time(4,6,7));
			MessageModel message1 = null;
			MessageModel message2 = null;
			MessageModel message3 = null;
			MessageModel message4 = null;
			MessageModel message5 = null;
			try {
				message1 = new MessageModel(meeting1, "meetingCreated", new ParticipantModel(), new ParticipantModel(), new Timestamp(1,2,3,4,5,6,7));
				message1.setSeen(true);
				message2 = new MessageModel(meeting2, "meetingCreated", new ParticipantModel(), new ParticipantModel(), new Timestamp(1,2,3,4,5,6,7));
				message2.setSeen(true);
				message3 = new MessageModel(meeting2, "meetingTimeChanged", new ParticipantModel(), new ParticipantModel(), new Timestamp(1,2,3,4,5,6,7));
				message3.setSeen(false);
				message4 = new MessageModel(meeting2, "meetingCancelled", new ParticipantModel(), new ParticipantModel(), new Timestamp(1,2,3,4,5,6,7));
				message4.setSeen(false);
				message5 = new MessageModel(meeting1, "placeChanged", new ParticipantModel(), new ParticipantModel(), new Timestamp(1,2,3,4,5,6,7));
				message5.setSeen(false);
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
//			message1.setTime(new Timestamp(1,2,3,4,5,6,7));
//			message2.setTime(new Timestamp(2,2,1,4,5,6,7));
//			message3.setTime(new Timestamp(3,2,4,4,5,6,7));
//			message4.setTime(new Timestamp(4,2,9,4,5,6,7));
//			message5.setTime(new Timestamp(5,2,8,4,5,6,7));
			
			DefaultListModel<MessageModel> messageses = new DefaultListModel<MessageModel>();
			messageses.add(0,message1);
			messageses.add(1,message2);
			messageses.add(1,message3);
			messageses.add(1,message4);
			messageses.add(1,message5);
			
			
			JList<MessageModel> list = new JList<MessageModel>(messageses);
			//list.setModel(testmessages);		
			list.setCellRenderer(new InboxListCellRenderer());
			list.addListSelectionListener(this);
		
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(list);
			scrollPane.setPreferredSize(new Dimension(780,450));
			
			add(scrollPane);
		}

		public void valueChanged(ListSelectionEvent arg0) {
			// Aktiveres når noen trykker i innboksen
			// UFERDIG, per nå sendes man bare til appointmentView
			ctrl.setState(AppointmentCtrl.class);
		}
	}
	
	public class InboxElementPanel extends JPanel {
		
		public InboxElementPanel(MessageModel message) {
			this.setLayout(new GridLayout());
			
			JLabel label = new JLabel(message.getMessage());
			add(label);
			JLabel button = new JLabel("              Gå til");
			button.setForeground(Color.BLUE);
			add(button);
			
			if (message.isSeen()) {
				this.setBackground(Color.WHITE);
			} else {
				setBackground(Color.GREEN);
			}
		}
	}
	
	public class InboxListCellRenderer extends DefaultListCellRenderer {
		
		public Component getListCellRendererComponent(JList list,
	            Object value,
	            int index,
	            boolean isSelected,
	            boolean cellHasFocus) {
			
			MessageModel message = (MessageModel) value; 
			
			InboxElementPanel element = new InboxElementPanel(message); 
			//setText((String) value);
			return element;
		}
	}
}
