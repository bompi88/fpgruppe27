package controller;

import java.sql.Timestamp;

import framework.Controller;

class updateThread extends Thread {
	
	Timestamp refressRate = new Timestamp(500000);
	private boolean running; 
	private InboxCtrl inboxCtrl; 
	private CalendarCtrl calanderCtrl; 
	
    
    public updateThread(boolean running, InboxCtrl inboxCtrl, CalendarCtrl calanderCtrl) {
    	this.running = running; 
    	this.inboxCtrl = inboxCtrl; 
    	this.calanderCtrl= calanderCtrl; 
    	
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
	}
		 
	public InboxCtrl getInboxCtrl(){
		return inboxCtrl; 
	}
	public CalendarCtrl getCalandarCtrl(){
		return calanderCtrl; 
	}
}