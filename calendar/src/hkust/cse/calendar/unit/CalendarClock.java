package hkust.cse.calendar.unit;

import hkust.cse.calendar.gui.Utility;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.Timer;

public class CalendarClock implements ActionListener{

	private GregorianCalendar today;
	private Timer timer;
	private Timestamp currTime;
	private Timestamp sTime;
	private int timeDelay;
	private boolean start;
	private boolean rewind;
	private ArrayList<ClockListeners> CListeners;
	
	public CalendarClock() {
		sTime = new Timestamp(0);
		currTime = sTime;
		timeDelay = 1000;
		start = false;
		rewind = false;
		timer = new Timer(1000, this);
		CListeners = new ArrayList<ClockListeners>();
	}

	public void runningClockListener(ClockListeners listener) {
		CListeners.add(listener);
	}
	
	public int getDelay() {
		return timeDelay;
	}
	
	public void setDelay(int delay) {
		timeDelay = delay;
	}
	
	public Timestamp getStartTime() {
		return sTime;
	}
	
	public void setStartTime(Timestamp startTime) {
		sTime = startTime;
		currTime = startTime;
	}
	
	public void start() {
		// if timer has already started, don't start
		if(!start) {
			timer.start();
			start = true;
		}
	}
	
	public void reset() {
		sTime = new Timestamp(0);
		currTime = sTime;
		timeDelay = 1000;
	}
	
	public void resume() {
		this.start();
	}
	
	public void rewind() {
		if (!rewind) {
			rewind = true;
			this.start();
		}
	}
 
	public void stop() {
		if(start) {
			timer.stop();
			start = false;
			rewind = false;
			for (int i = 0; i < CListeners.size(); i++) {
				CListeners.get(i).timeIsElapsing(this);
			}
		}
	}
	
	public boolean isStart() {
		return start;
	}
	
	public boolean isRewind() {
		return rewind;
	}
	
	@Override
	public String toString() {
		return String.format("%04d-%02d-%02d %02d:%02d", currTime.getYear() + 1900, currTime.getMonth() + 1, currTime.getDate(), 
				currTime.getHours(), currTime.getMinutes());
	}
	
	public Timestamp getCurrentTime() {
		return currTime;
	}
	
	public Timestamp getNextElapsedTime() {
		Timestamp ts = (Timestamp) currTime.clone();
		ts.setTime(currTime.getTime() + this.timeDelay);
		return ts;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == timer) {
			// If not rewinding, we fast forward
			if (!rewind) 
				currTime.setTime(currTime.getTime() + timeDelay);
			else // if rewind is true, we rewind the time 
				currTime.setTime(currTime.getTime() - timeDelay);
		}
		
		for (int i = 0; i < CListeners.size(); i++) {
			CListeners.get(i).timeIsElapsing(this);
		}
		
	}
	
}
