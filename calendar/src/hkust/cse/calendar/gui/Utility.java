package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.sql.Timestamp;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Utility {

	public static int getNumber(String s) {
		if (s == null)
			return -1;
		if (s.trim().indexOf(" ") != -1) {
			JOptionPane.showMessageDialog(null,
					"Can't Contain Whitespace In Number !");
			return -1;
		}
		int result = 0;
		try {
			result = Integer.parseInt(s);
		} catch (NumberFormatException n) {
			return -1;
		}
		return result;
	}


	public static Appt createDefaultAppt(int currentY, int currentM,
			int currentD, User me) {
		Appt newAppt = new Appt();
		newAppt.setID(0);
		
		String curYear = Integer.toString(currentY);
	    int month = currentM; //no need to minus 1 because Timestamp.valueOf automatically -1 inside it. WOW RIGHT!
	    String curMonth = Integer.toString(month);
	    Integer date = new Integer(currentD);
	    String curDate = Integer.toString(date);
	    Timestamp start = Timestamp.valueOf(curYear+"-"+curMonth+"-"+curDate+" 09:00:00");
		Timestamp end = Timestamp.valueOf(curYear+"-"+curMonth+"-"+curDate+" 09:30:00");
		
		/*
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(currentD);
		start.setHours(9);
		start.setMinutes(0);

		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		end.setDate(currentD);
		end.setHours(9);
		end.setMinutes(30);
		*/

		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		return newAppt;
	}

	@SuppressWarnings("deprecation")
	public static Appt createDefaultAppt(int currentY, int currentM,
			int currentD, User me, int startTime) {
		Appt newAppt = new Appt();
		newAppt.setID(0);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(currentD);
		start.setHours(startTime / 60);
		start.setMinutes(startTime % 60);

		int dur = startTime + 60;
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		end.setDate(currentD);
		end.setHours(dur / 60);
		end.setMinutes(dur % 60);

		newAppt.setTimeSpan(new TimeSpan(start, end));
		User[] temp = new User[1];
		temp[0] = me;

		newAppt.setTitle("Untitled");
		newAppt.setInfo("Input description of this appointment");
		return newAppt;
	}
	
	@SuppressWarnings("deprecation")
	/**
	 * Create a TimeStamp comparison to check whether t1 > t2 or t1 < t2 or t1 = t2
	 * @param t1
	 * @param t2
	 * @return
	 * 0 when t1 = t2
	 * 1 when t1 > t2
	 * -1 when t1 < t2
	 */
	public final static int AfterBeforeEqual(Timestamp t1, Timestamp t2) {
		int t1Year = t1.getYear();
		int t2Year = t2.getYear();
		int t1Month = t1.getMonth();
		int t2Month = t2.getMonth();
		int t1Date = t1.getDate();
		int t2Date = t2.getDate();
		int t1Hours = t1.getHours();
		int t2Hours = t2.getHours();
		int t1Minutes = t1.getMinutes();
		int t2Minutes = t2.getMinutes();
		int t1Seconds = t1.getSeconds();
		int t2Seconds = t2.getSeconds();
		
		if(t1Year > t2Year) {
			return 1;
		}
		else if (t1Year < t2Year) {
			return -1;
		}
		else {
			if(t1Month > t2Month) {
				return 1;
			}
			else if (t1Month < t2Month) {
				return -1;
			}
			else {
				if (t1Date > t2Date) {
					return 1;
				}
				else if (t1Date < t2Date) {
					return -1;
				}
				else {
					if (t1Hours > t2Hours) {
						return 1;
					}
					else if (t1Hours < t2Hours){
						return -1;
					}
					else {
						if (t1Minutes > t2Minutes) {
							return 1;
						}
						else if (t1Minutes < t2Minutes) {
							return -1;
						}
						else { //if minutes are equal
							if (t1Seconds > t2Seconds) {
								return 1;
							}
							else if (t1Seconds < t2Seconds) {
								return -1;
							}
							else return 0;
						}
					}
				}
			}
		}
	}
	
	public final static ArrayList<TimeSpan> createTimeSlotsForADay(TimeSpan period) {
		ArrayList<TimeSpan> timeSlots = new ArrayList<TimeSpan>();
		int hour = 8;
		int minute = 0;
		
		for(int i = 0; i < 40; i++ ) {
			Timestamp start = new Timestamp(0);
			start.setYear(period.StartTime().getYear());
			start.setMonth(period.StartTime().getMonth());
			start.setDate(period.StartTime().getDate());
			start.setHours(hour);
			start.setMinutes(minute);
			
			minute += 15;
			if (minute == 60) {
				hour++;
				minute = 0;
			}
			
			Timestamp end = new Timestamp(0);
			end.setYear(period.StartTime().getYear());
			end.setMonth(period.StartTime().getMonth());
			end.setDate(period.StartTime().getDate());
			end.setHours(hour);
			end.setMinutes(minute);
			
			timeSlots.add(new TimeSpan(start,end));
		}
		
		return timeSlots;
	}
	
	private final static int getMinFromArray(int[] array, int n) {
		int min = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] < min)
				min  = array[i];
		}
		return min;
	}
	
	private final static int getMaxFromArray(int[] array, int n) {
		int max = array[0];
		for (int i = 0; i < array.length; i++) {
			if (array[i] > max)
				max  = array[i];
		}
		return max;
	}
	
	public final static boolean ArrayIsConsecutive(int[] array, int n) {
		if (n < 1) return false;
		
		int min = getMinFromArray(array, n);
		int max = getMaxFromArray(array, n);
		
		// max - min + 1 == n checks whether the whole elements
		// has the potential of being consecutive
		// Example array -> 1. [30, 32, 31, 29, 33] ====> 33 - 29 + 1 = 5  | true
		//  				2. [79, 30, 31, 33, 29] ====> 79 - 29 + 1 = 51 | false
		if ((max - min + 1) == n) {
			
			// After passing the condition, we have to check for repetitions by marking
			for (int i = 0; i < n; i++) {
				int j;
				if (array[i] < 0)
					j = -array[i] - min;
				else 
					j = array[i] - min;
				
				if (array[j] > 0)
					array[j]  = -array[j];
				else
					return false;
			}
			
			// No negative value means all elements are distinct
			return true;
		}
		
		// Because max-min+1 != n
		return false;
	}
}
