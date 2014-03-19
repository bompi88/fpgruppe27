package controller;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import model.Message;
import view.InboxView;

import framework.Controller;
import framework.Observable;
import framework.Observer;
import framework.State;

public class InboxCtrl extends Controller implements State, Observable {

	private InboxView inboxPanel;
	private List<Observer> observers = new ArrayList<Observer>();
	private DefaultListModel<Message> inbox = new DefaultListModel<Message>();
	
	public InboxCtrl(Controller ctrl) {
		super(ctrl);
		
		inboxPanel = new InboxView(ctrl, inbox);
		inboxPanel.setVisible(false);
		ctrl.getMainFrame().getContentPane().add(inboxPanel);
	}

	@Override
	public void show() {
		isHidden = false;
		inboxPanel.setVisible(true);
	}

	@Override
	public void hide() {
		isHidden = true;
		inboxPanel.setVisible(false);
		inboxPanel.setAllMessagesSeen();
		fireObserverEvent("inboxSeen", null);
	}
	
	public void initInbox() {
		inboxPanel.initInbox();
	}
	
	public void init() {
		inboxPanel.setVisible(false);
	}
	
	public int getNumberOfUnseenMessages() {
		int count = 0;
		for (int i = 0; i < inbox.size(); i++) {
			if(!inbox.get(i).isSeen())
				count++;
		}
		return count;
	}

	@Override
	public void addObserver(Observer ob) {
		observers.add(ob);
	}

	@Override
	public void fireObserverEvent(String event, Object obj) {
		for (Observer o : observers)
			o.changeEvent(event, obj);
	}

}
