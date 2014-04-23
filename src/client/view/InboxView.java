package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;

import database.ClientObjectFactory;

import resources.AppConstants;
import utils.RelativeLayout;

import model.Message;

@SuppressWarnings("serial")
public class InboxView extends JPanel {

	private JPanel topPanelWrapper = new JPanel();
	private InboxListPanel inboxPanel;
	private DefaultListModel<Message> inbox;

	public InboxView() {
		
		inbox = new DefaultListModel<Message>();
		inboxPanel = new InboxListPanel();
		
		RelativeLayout rl = new RelativeLayout(RelativeLayout.Y_AXIS, 0);
		rl.setAlignment(RelativeLayout.CENTER);
		setLayout(rl);
		
		RelativeLayout rl2 = new RelativeLayout(RelativeLayout.X_AXIS, 0);
		rl2.setAlignment(RelativeLayout.LEADING);
		
		topPanelWrapper.setLayout(rl2);
		topPanelWrapper.setBackground(AppConstants.HEADER_BG_COLOR);
		topPanelWrapper.setPreferredSize(new Dimension(AppConstants.MAIN_FRAME_WIDTH-AppConstants.SIDEBAR_WIDTH, AppConstants.HEADER_PANEL_HEIGHT));
		
		add(topPanelWrapper);
		
		DefaultTitlePanel inboxTitlePanel = new DefaultTitlePanel();
		inboxTitlePanel.setTitle(AppConstants.INBOX_HEADER_TEXT);
		topPanelWrapper.add(inboxTitlePanel);
		
		add(inboxPanel);
	}
	
	public void setModel(List<Message> messages) {
		for(int i = 0; i < messages.size(); i++){
			inbox.add(0, messages.get(i));  
		}
	}
	
	public Message getSelectedMessage() {
		return inboxPanel.getSelectedMessage();
	}
	
	public void clearSelection() {
		inboxPanel.clearSelection();
	}
	
	public void addListSelectionListener(ListSelectionListener listener) {
		inboxPanel.addListSelectionListener(listener);
	}
	
	public class InboxListPanel extends JPanel {
		
		JList<Message> list;
		
		public InboxListPanel() {

			list = new JList<Message>(inbox);
			list.setCellRenderer(new InboxListCellRenderer());
		
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setViewportView(list);
			scrollPane.setPreferredSize(new Dimension(780,450));
			
			add(scrollPane);
		}
		
		public Message getSelectedMessage() {
			return (Message)list.getSelectedValue();
		}
		
		public void clearSelection() {
			list.clearSelection();
		}

		public void addListSelectionListener(ListSelectionListener listener) {
			list.addListSelectionListener(listener);
		}
	}
	
	public class InboxElementPanel extends JPanel {
		
		public InboxElementPanel(Message message) {
			this.setLayout(new GridBagLayout());
			
			JLabel date = new JLabel(toDateString(message.getTime()));
			date.setPreferredSize(new Dimension(100,25));
			add(date);
			JLabel time = new JLabel(toTimeString(message.getTime()));
			time.setPreferredSize(new Dimension(60,25));
			add(time);
			JLabel mess = new JLabel(message.getMessage());
			mess.setPreferredSize(new Dimension(500,25));
			add(mess);
			JLabel button = new JLabel("G�� til");
			button.setForeground(Color.BLUE);
			add(button);
			
			if (message.isSeen()) {
				this.setBackground(AppConstants.MESSAGE_SEEN_COLOR);
			} else {
				setBackground(Color.WHITE);
			}
		}
		
		public String toTimeString(Timestamp timestamp) {
			String[] parts = timestamp.toString().split(" ");
			String[] parts2 = parts[1].split(":");
			return parts2[0] + ":" + parts2[1];
		}
		
		public String toDateString(Timestamp timestamp) {
			String[] parts = timestamp.toString().split(" ");
			return parts[0];
		}
	}
	
	public class InboxListCellRenderer extends DefaultListCellRenderer {
	
		private InboxElementPanel element;
		
		@SuppressWarnings("rawtypes")
		public Component getListCellRendererComponent(JList list,
	            Object value,
	            int index,
	            boolean isSelected,
	            boolean cellHasFocus) {
			
			Message message = (Message) value; 
			element = new InboxElementPanel(message); 
			
			return element;
		}
	}
	
	public int getNoOfUnseenMess(){
		int count = 0; 
		for (int i = 0; i < inbox.size(); i++){
			if(inbox.get(i).isSeen() == false){ 
				count++; 
			}
		}
		return count; 
	}
	
	public void setAllMessagesSeen(){
		for(int i = 0; i < inbox.size(); i++){
			if(inbox.get(i).isSeen() == false){  
				ClientObjectFactory.setMessageAsSeen(inbox.get(i)); 
				inbox.get(i).setSeen(true); 
			} 
		}
	}
	
	public List<Message> getModel() {
		List<Message> messages = new ArrayList<Message>();
		
		for (int i = 0; i < inbox.size(); i++) {
			messages.add(inbox.get(i));
		}
		return messages;
	}
}
