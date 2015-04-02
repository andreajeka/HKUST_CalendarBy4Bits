package hkust.cse.calendar.notificationServices;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;



public class timer {

	private Timer timer;
	private String userName = "good";
	private String message = "bad";
	private Date date;
	private Calendar calendar;

	public timer(Date _date){
		this.date = _date;
		timer = new Timer();

		timer.schedule(new alartTask(userName, message), date);
		System.out.println(date);
	}

	public timer(){
		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2015);
		calendar.set(Calendar.MONTH, 3-1);
		calendar.set(Calendar.DATE, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 12);
		calendar.set(Calendar.SECOND, 12);
		Date time = calendar.getTime();
		
		timer = new Timer();
		timer.schedule(new alartTask(userName, message), time);


	}

	class alartTask extends TimerTask{
		private String message;
		private String userName;

		public alartTask(String userName, String message){
			this.userName = userName;
			this.message = message;
		}

		public void run(){
			JOptionPane.showMessageDialog(null,userName, message, JOptionPane.WARNING_MESSAGE);
		}
	}
}
