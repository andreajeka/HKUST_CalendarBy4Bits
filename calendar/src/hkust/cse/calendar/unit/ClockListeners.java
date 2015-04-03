package hkust.cse.calendar.unit;

public interface ClockListeners {
	public void timeIsElapsing (CalendarClock emitter);
	public void timeIsStopped (CalendarClock emitter);
}
