package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.sql.Timestamp;

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
		// newAppt.setParticipants(temp);

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
}
