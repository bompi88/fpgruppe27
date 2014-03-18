package controller;

import javax.swing.DefaultListModel;

import model.Message;
import view.InboxView;

import framework.Controller;
import framework.State;

public class InboxCtrl extends Controller implements State {

	private InboxView inboxPanel;
	private DefaultListModel<Message> inbox = new DefaultListModel<Message>();
	
	public InboxCtrl(Controller ctrl) {
		super(ctrl);
		
		inboxPanel = new InboxView(ctrl, inbox);
		
		ctrl.getMainFrame().getContentPane().add(inboxPanel);
	}

	@Override
	public void show() {
		inboxPanel.setVisible(true);
	}

	@Override
	public void hide() {
		inboxPanel.setVisible(false);
	}
	
	public void initInbox() {
		inboxPanel.initInbox();
	}
	
	public int getNumberOfUnseenMessages() {
		int count = 0;
		for (int i = 0; i < inbox.size(); i++) {
			if(!inbox.get(i).isSeen())
				count++;
		}
		return count;
	}

}
