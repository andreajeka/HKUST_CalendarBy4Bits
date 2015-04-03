package hkust.cse.calendar.unit;

// Listeners to capture when clock is running/elapsing and when clock has stopped
public interface ClockListeners {
	public void timeIsElapsing (CalendarClock emitter);
	public void timeIsStopped (CalendarClock emitter);
}
