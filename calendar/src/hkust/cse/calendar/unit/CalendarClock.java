package hkust.cse.calendar.unit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.swing.Timer;

public class CalendarClock implements ActionListener{

	private Timer timer;
	private Timestamp currTime;
	private Timestamp sTime;
	private int timeDelay;
	private boolean start;
	private boolean rewind;
	private ArrayList<ClockListeners> CListeners;
	
	// Calendar Clock Constructor
	public CalendarClock() {
		sTime = new Timestamp(0);
		currTime = sTime;
		timeDelay = 1000;
		start = false;
		rewind = false;
		timer = new Timer(1000, this);
		CListeners = new ArrayList<ClockListeners>();
	}

	// Add listener to the clock listener
	public void runningClockListener(ClockListeners listener) {
		CListeners.add(listener);
	}
	
	// Get time delay
	public int getDelay() {
		return timeDelay;
	}
	
	// Set time delay
	public void setDelay(int delay) {
		timeDelay = delay;
	}
	
	// Get the start time
	public Timestamp getStartTime() {
		return sTime;
	}
	
	// Set the start time
	public void setStartTime(Timestamp startTime) {
		sTime = startTime;
		currTime = startTime;
	}
	
	// Start timer
	public void start() {
		// if timer has already started, don't start
		if(!start) {
			timer.start();
			start = true;
		}
	}
	
	// Reset timer
	public void reset() {
		sTime = new Timestamp(0);
		currTime = sTime;
		timeDelay = 1000;
	}
	
	// Resume the timer
	public void resume() {
		this.start();
	}
	
	// Rewind the timer
	public void rewind() {
		if (!rewind) {
			rewind = true;
			this.start();
		}
	}
 
	// Stop the timer
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
	
	// Check if timer is starting
	public boolean isStart() {
		return start;
	}
	
	// Check if timer is being rewinded
	public boolean isRewind() {
		return rewind;
	}
	
	// Output the representation of the current time of the Calendar Clock
	@SuppressWarnings("deprecation")
	@Override
	public String toString() {
		return String.format("%04d-%02d-%02d %02d:%02d", currTime.getYear() + 1900, currTime.getMonth() + 1, currTime.getDate(), 
				currTime.getHours(), currTime.getMinutes());
	}
	
	// Get the current time
	public Timestamp getCurrentTime() {
		return currTime;
	}
	
	// Get the next elapsed time by adding current time + delay time
	// Timestamp is cloned to get exact duplicate, not referencing by just Timestamp ts = currTime.
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
		
		// Set the listeners
		for (int i = 0; i < CListeners.size(); i++) {
			CListeners.get(i).timeIsElapsing(this);
		}
		
	}
	
}
