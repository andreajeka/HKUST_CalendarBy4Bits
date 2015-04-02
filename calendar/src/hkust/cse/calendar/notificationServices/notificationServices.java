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
	private Date date;
	private String mTitle, mInfo;

	
	public notificationServices(Date _date, String _mTitle, String _mInfo){		
		//System.out.println(cal.currentY + " " + cal.currentM + " " + cal.currentD);
		this.date = _date;
		this.mTitle = _mTitle;
		this.mInfo = _mInfo;
		timer = new Timer();
		System.out.println("create a timer");
		timer.schedule(new alartTask(mTitle, mInfo), date);
	}
	
	class alartTask extends TimerTask{
		private String mTitle, mInfo;
		
		public alartTask(String _mTitle, String _mInfo){
			this.mTitle = _mTitle;
			this.mInfo = _mInfo;
		}
		public void run(){
			JOptionPane.showMessageDialog(null, this.mInfo, this.mTitle, JOptionPane.WARNING_MESSAGE);
		}
	}
}