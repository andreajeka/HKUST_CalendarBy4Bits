package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.sql.Timestamp;
import java.util.GregorianCalendar;

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
	    int month = currentM - 1;
	    String curMonth = Integer.toString(month);
	    Integer day = new Integer(currentD);
	    String curDay = Integer.toString(day);
	    Timestamp start = Timestamp.valueOf(curYear+"-"+curMonth+"-"+curDay+" 09:00:00");
		Timestamp end = Timestamp.valueOf(curYear+"-"+curMonth+"-"+curDay+" 09:30:00");
		
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
		if(t1.getYear() > t2.getYear()) {
			return 1;
		}
		else if (t1.getYear() < t2.getYear()) {
			return -1;
		}
		else {
			if(t1.getMonth() > t2.getMonth()+1) {
				return 1;
			}
			else if (t1.getMonth() < t2.getMonth()+1) {
				return -1;
			}
			else {
				if (t1.getDay() > t2.getDay()) {
					return 1;
				}
				else if (t1.getDay() < t2.getDay()) {
					return -1;
				}
				else {
					if (t1.getHours() > t2.getHours()) {
						return 1;
					}
					else if (t1.getHours() < t2.getHours()){
						return -1;
					}
					else {
						if (t1.getMinutes() > t2.getMinutes()) {
							return 1;
						}
						else if (t1.getMinutes() < t2.getMinutes()) {
							return -1;
						}
						else { //if minutes are equal
							if (t1.getSeconds() > t2.getSeconds()) {
								return 1;
							}
							else if (t1.getSeconds() < t2.getSeconds()) {
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
