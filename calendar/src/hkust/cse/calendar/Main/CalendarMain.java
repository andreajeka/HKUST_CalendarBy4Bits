//main method, code starts here
package hkust.cse.calendar.Main;


import javax.swing.UIManager;

import hkust.cse.calendar.gui.LoginDialog;
import hkust.cse.calendar.notificationServices.notificationServices;


public class CalendarMain {
	public static boolean logOut = false;

	public static void main(String[] args) {
		while(true){
			logOut = false;
			try{
//	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			}catch(Exception e){

			}
			LoginDialog loginDialog = new LoginDialog();
			//new notificationServices("userName: Jack Hui", "Message: Time's up", 2015, 3, 10, 13, 30, 20);
			while(logOut == false){
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
