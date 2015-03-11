package hkust.cse.calendar.notificationServices;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/* creating a class norificationServices */

public class notificationServices {
	Timer timer;
	
	public notificationServices(String userName, String message, int year, int month, int date, int hour, int minute, int second){
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, date);
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		Date time = calendar.getTime();
		
		
		timer = new Timer();
		timer.schedule(new alartTask(userName, message), time);
		System.out.println(year + " " + month + " " + date);
		System.out.println(time.toString());
		
	}
	
	class alartTask extends TimerTask{
		String message;
		String userName;
		public alartTask(String userName, String message){
			this.userName = userName;
			this.message = message;
		}
		public void run(){
			JOptionPane.showMessageDialog(null, this.message, this.userName, JOptionPane.WARNING_MESSAGE);
		}
	}
}