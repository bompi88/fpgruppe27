package database;

import java.util.TimerTask;

import controller.CalendeerClient;

public class UpdateThread extends TimerTask {

	private CalendeerClient context;
	
    public UpdateThread(CalendeerClient context) {
    	super();
    	this.context = context; 
    }
 
    public void run(){
		 context.updateAll();
	}
}