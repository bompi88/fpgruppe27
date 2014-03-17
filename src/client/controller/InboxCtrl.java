package controller;

import view.InboxView;

import framework.Controller;
import framework.State;

public class InboxCtrl extends Controller implements State {

	private InboxView inboxPanel;
	
	public InboxCtrl(Controller ctrl) {
		super(ctrl);
		
		inboxPanel = new InboxView(ctrl);
		
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

}
