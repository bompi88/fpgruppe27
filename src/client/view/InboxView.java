package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import model.Employee;
import model.Message;
import controller.AppointmentCtrl;
import controller.MainCtrl;
import database.ClientObjectFactory;
import framework.Controller;
import resources.AppConstants;
import utils.RelativeLayout;

@SuppressWarnings("serial")
public class InboxView extends JPanel {
	
	private float titleBarScaleWidth = 0.33f;
	
	private JPanel topPanelWrapper = new JPanel();
	private InboxListPanel inboxPanel;
	
	private JLabel whereLabel = new JLabel(AppConstants.INBOX_HEADER_TEXT);
	
	protected DefaultListModel<Message> inbox = new DefaultListModel<Message>(); 
	protected int noOfUnseenMessages = 0; 
	
	Controller ctrl;
	Employee emp;
	
	
	public InboxView(Controller controll) {
		
		ctrl = controll;
		initInbox();
		
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
		
		InboxTitlePanel inboxTitlePanel = new InboxTitlePanel();
		topPanelWrapper.add(inboxTitlePanel);
		
		inboxTitlePanel.fillSizeOfParent();
		
		add(inboxPanel);
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
		
		JList<Message> list;
		
		public InboxListPanel() {
			
			
			
			list = new JList<Message>(inbox);
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
		
		public InboxElementPanel(Message message) {
			this.setLayout(new GridLayout());
			
			//String inboxLine = "    " + message.getTime() + "    " + message.getMessage();
			JLabel label = new JLabel(message.getMessage());
			add(label);
			JLabel button = new JLabel("              Gå til");
			button.setForeground(Color.BLUE);
			add(button);
			
			if (message.isSeen()) {
				this.setBackground(Color.DARK_GRAY);
			} else {
				setBackground(Color.WHITE);
			}
		}
	}
	
	public class InboxListCellRenderer extends DefaultListCellRenderer {
		
		public Component getListCellRendererComponent(JList list,
	            Object value,
	            int index,
	            boolean isSelected,
	            boolean cellHasFocus) {
			
			Message message = (Message) value; 
			
			InboxElementPanel element = new InboxElementPanel(message); 
			//setText((String) value);
			return element;
		}
	}
	
	public void initInbox(){
		emp = (Employee) ((MainCtrl)ctrl.getMainCtrl()).getModel();
		System.out.print(emp.getUsername());
		Timestamp timeNull = new Timestamp(0); 
		List<Message> messages = ClientObjectFactory.getMessages(emp.getUsername(), timeNull); 
		
		for(int i = 0; i < messages.size(); i++){
			inbox.add(0, messages.get(i));  
			if (messages.get(i).isSeen() == false){
				noOfUnseenMessages++; 
			}
				
		}
	}
		
	public void updateInbox(){	
		Timestamp timeLastMessage = inbox.get(0).getTime();
		List<model.Message> messages = ClientObjectFactory.getMessages(emp.getUsername(), timeLastMessage); 
		
		for(int i = 0; i < messages.size(); i++){
			inbox.add(0, messages.get(i)); 
			if (messages.get(i).isSeen() == false){
				noOfUnseenMessages++; 
			}
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
		noOfUnseenMessages = 0; 
	}
}
