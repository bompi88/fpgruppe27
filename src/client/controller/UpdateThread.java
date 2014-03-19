package controller;

import java.sql.Timestamp;

import view.SideBarView;
import framework.Controller;

class updateThread extends Thread {
	
	Timestamp refressRate = new Timestamp(10000);
	private boolean running; 
	private InboxCtrl inboxCtrl; 
	private CalendarCtrl calanderCtrl; 
	private SideBarView sidebarView; 
	
    
    public updateThread(boolean running, InboxCtrl inboxCtrl, CalendarCtrl calanderCtrl, SideBarView sidebarView) {
    	this.running = running; 
    	this.inboxCtrl = inboxCtrl; 
    	this.calanderCtrl= calanderCtrl; 
    	this.sidebarView= sidebarView;
    	
    }
        	
	 public void run(){
		Timestamp lastUpdateTime = new Timestamp(System.currentTimeMillis()); 
		
		while(running){
			Timestamp timeNow = new Timestamp(System.currentTimeMillis()); 
			
			if(timeNow.getTime() > (lastUpdateTime.getTime() + refressRate.getTime())){
				runUpdates(); 
				lastUpdateTime = new Timestamp(System.currentTimeMillis());
			}
			
		}
		
	}
	
	public void stopRefressing(){
		running = false;  
	}
	
	public void runUpdates(){
		inboxCtrl.updateInbox();  
		calanderCtrl.update(); 
		sidebarView.setNumberOfUnseenMessages(inboxCtrl.getNumberOfUnseenMessages()); 
	}
		 
	public InboxCtrl getInboxCtrl(){
		return inboxCtrl; 
	}
	public CalendarCtrl getCalandarCtrl(){
		return calanderCtrl; 
	}
}