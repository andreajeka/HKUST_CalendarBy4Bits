package hkust.cse.calendar.apptstorage;//

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Request;
import hkust.cse.calendar.unit.TimeSlotFeedback;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.UUID;


public abstract class ApptStorage {

	public HashMap<Integer, Appt> mAppts;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	public int mAssignedApptID;	//a global appointment ID for each appointment record
	public ArrayList<ArrayList<Request>> mRq2DList; // a 2D array of request
	public ArrayList<ArrayList<TimeSlotFeedback>> ts2DList;
	
	public ApptStorage() {	//default constructor
	}

	public abstract void SaveAppt(Appt appt);	//abstract method to save an appointment record

	public abstract Appt[] RetrieveAppts(TimeSpan d);	//abstract method to retrieve an appointment record by a given timespan

	public abstract Appt[] RetrieveAppts(User entity, TimeSpan time);	//overloading abstract method to retrieve an appointment record by a given user object and timespan
	

	public abstract Appt RetrieveAppts(int apptID);					// overload method to retrieve appointment with the given appt appointment id

	public abstract Appt[] RetrieveAppts(User entity);

	public abstract ArrayList<TimeSpan> RetrieveAvailTimeSpans(User entity, TimeSpan period); // overload method to retrieve available time slots for a user with a specified period
	
	public abstract ArrayList<TimeSpan> RetrieveAvailTimeSpans(ArrayList<User> entities, TimeSpan period); // overload method to retrieve available time slots for a group of users with a specified period of time
	
	public abstract void UpdateAppt(Appt appt);	//abstract method to update an appointment record

	public abstract void RemoveAppt(Appt appt);	//abstract method to remove an appointment record
	
	public abstract void setCurrentUser(User user);		//abstract method to set current user 
	
	public abstract User getCurrentUser();		//abstract method to return the current user object
	
	public abstract String getOverlapMessage(); //abstract method to get overlap message
	
	public abstract boolean isOverlap(); // abstract method to check if there is an overlap
	
	public abstract void LoadApptFromXml();		//abstract method to load appointment from xml record into hash map
	
	public abstract void SaveApptToXml();       //abstract method to save appointment from hash map into xml record
	
	public abstract void SaveLocToXml();        //abstract method to save location from Location[] into xml
	
	public abstract void LoadLocFromXml();		//abstract method to load location from xml into Location[]
	
	public abstract void SaveUserToXml();       //abstract method to save userList to xml
	
	public abstract void LoadUserFromXml();     //abstract method to load userList from xml
	
	public abstract void SaveRequestsToXml(); 
	
	public abstract void LoadRequestsFromXml();    
	
	public abstract void SaveFeedbacksToXml(); 
	
	public abstract void LoadFeedbacksFromXml();
	
	
	
	public abstract ArrayList<Location> getLocationList();
	
	public abstract void setLocationList(ArrayList<Location> locations);
	
	public abstract int getLocationCapacity();

	public abstract int LengthInMemory();
	
	public abstract void removeLocation(Location location);
	
	public abstract ArrayList<User> getUserList();
	
	public abstract void addUser(User user);
	
	public abstract void addRequest(Request rq);
	
	public abstract void addFeedback(TimeSlotFeedback feedback);
	
	public abstract void updateUser(User user);
	
	public abstract User searchUser(String username);
	
	public abstract User searchUser(UUID userID);
	
	public abstract void removeUser(String username);
	
	public abstract void removeUser(UUID userId);
	
	public abstract boolean capaValidation(String location, int numOfUsers);
	
	public abstract boolean checkLocation(String location);
	
	public abstract void removeUserAppts(UUID userId);

	public abstract void SaveFeedbackCountToXml();

	public abstract void LoadFeedbackCountFromXml();
	
	public abstract int getCountFeedbackRequest();
	
	public abstract void setCountFeedbackRequest(int countFeedbackRequest);

	public abstract boolean checkDuplicateLocation(String location, TimeSpan timeSpan);
}
