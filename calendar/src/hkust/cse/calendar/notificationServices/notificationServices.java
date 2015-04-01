package hkust.cse.calendar.notificationServices;

import hkust.cse.calendar.gui.CalGrid;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

/* creating a class norificationServices */

public class notificationServices {
	private Timer timer;
	private Calendar calendar;
	private Date time, time2;
	private String message;
	private GregorianCalendar today;
	
	
	
	public notificationServices(CalGrid cal, String userName, String message){
		
		today = cal.getToday();
		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, cal.currentY);
		calendar.set(Calendar.MONTH, cal.currentM-1);
		calendar.set(Calendar.DATE, cal.currentD);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 12);
		calendar.set(Calendar.SECOND, 12);
		time = calendar.getTime();
		
		System.out.println(cal.currentY + " " + cal.currentM + " " + cal.currentD);
		
		timer = new Timer();
		timer.schedule(new alartTask(userName, message, cal), time);
		
		
	}
	
	class alartTask extends TimerTask{
		private String message;
		private String userName;
		CalGrid cals;
		
		public alartTask(String userName, String message, CalGrid cal){
			this.userName = userName;
			this.message = message;
			this.cals = cal;
		}
		public void run(){
			JOptionPane.showMessageDialog(null, today.getTime(), this.userName, JOptionPane.WARNING_MESSAGE);
		}
	}
}