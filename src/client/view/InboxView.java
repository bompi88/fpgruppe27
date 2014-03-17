package view;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Employee;
import model.Message;
import controller.MainCtrl;
import database.ClientObjectFactory;
import framework.Controller;
import resources.AppConstants;

@SuppressWarnings("serial")
public class InboxView extends JPanel {
	
	private JLabel whereLabel = new JLabel(AppConstants.INBOX_HEADER_TEXT);
	
	protected ArrayList<Message> inbox = new ArrayList<Message>(); 
	protected int noOfUnseenMessages = 0; 
	
	Controller ctrl;
	Employee emp;
	
	
	public InboxView() {
		add(whereLabel);	
	}
	
	
	public void initInbox(){
		emp = (Employee) ((MainCtrl)ctrl.getMainCtrl()).getModel();
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
