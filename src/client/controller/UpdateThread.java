package controller;

import java.sql.Timestamp;

import framework.Controller;

class updateThread extends Thread {
	
	Timestamp refressRate = new Timestamp(300000);
	boolean running; 
	Controller ctrl; 
	String type; 
	
    
    public updateThread(boolean running, Controller ctrl, String type) {
    	this.running = running; 
    	this.ctrl = ctrl; 
    	this.type= type; 
    	
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
		if (type.equals("updateInbox")){
			//((MainCtrl)ctrl).inboxCtrl.updateInbox();   
		}
		else if (type.equals("updateMeetings")){
			//((MainCtrl)ctrl).inboxCtrl.updateInbox();   
		}
		 
		// 
	}
}