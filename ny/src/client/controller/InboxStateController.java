package controller;
import java.sql.Timestamp;
import java.util.List;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import database.ClientObjectFactory;
import model.Meeting;
import model.Message;

import view.InboxView;
import framework.State;


public class InboxStateController implements State {

	private CalendeerClient context;
	private List<Message> inboxModel;
	private InboxView inboxView;
	
	public InboxStateController(CalendeerClient context, List<Message> inboxModel, InboxView inboxView) {
		this.context = context;
		this.inboxModel = inboxModel;
		this.inboxView = inboxView;
		
		inboxView.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if (getSelectedMessage() != null) {
					Meeting meeting = ClientObjectFactory.getMeetingByID(getSelectedMessage().getMeetid());
					getContext().setMeetingModel(meeting);
				
					if (meeting != null) {
						clearSelection();
						
						if (CalendeerClient.getCurrentEmployee().getUsername().equals(meeting.getResponsible().getUsername())) {
							getContext().setState(getContext().getEditMeetingState());
						} else {
							getContext().setState(getContext().getViewMeetingState());
						}
					}
				}
			}
		});
	}
	
	public CalendeerClient getContext() {
		return context;
	}
	
	public Message getSelectedMessage() {
		return inboxView.getSelectedMessage();
	}
	
	public void clearSelection() {
		inboxView.clearSelection();
	}
	
	@Override
	public void hideState() {
		inboxView.setVisible(false);
		setAllMessagesSeen();
		context.updateAll();
	}

	@Override
	public void showState() {
		
		inboxView.setVisible(true);
	}

	@Override
	public void updateState() {
		updateInbox();
	}

	@Override
	public void initState() {
		Timestamp timeNull = new Timestamp(0); 
		inboxModel = ClientObjectFactory.getMessages(CalendeerClient.getCurrentEmployee().getUsername(), timeNull); 
		
		inboxView.setModel(inboxModel);
	}
	
	public void addListSelectionListener(ListSelectionListener listener) {
		inboxView.addListSelectionListener(listener);
	}
		
	private void updateInbox(){
		Timestamp timeLastMessage;
		List<Message> inbox = getModel();
		
		if(inbox != null && inbox.size() > 0)
			timeLastMessage = inbox.get(0).getTime();
		else
			timeLastMessage = new Timestamp(0);
		
		inboxModel = ClientObjectFactory.getMessages(CalendeerClient.getCurrentEmployee().getUsername(), timeLastMessage); 
		
		inboxView.setModel(inboxModel);
	}
	
	public int getNoOfUnseenMess(){
		return inboxView.getNoOfUnseenMess();
	}
	
	public void setAllMessagesSeen(){
		inboxView.setAllMessagesSeen();
	}

	public List<Message> getModel() {
		return inboxView.getModel();
	}
}
