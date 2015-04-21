package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.UUID;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

/* This class is for managing the Appt Storage according to different actions */
public class ApptStorageControllerImpl {

	/* Remove the Appt from the storage */
	public final static int REMOVE = 1;

	/* Modify the Appt the storage */
	public final static int MODIFY = 2;

	/* Add a new Appt into the storage */
	public final static int NEW = 3;
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	/* The Appt storage */
	private ApptStorage mApptStorage;

	/* Create a new object of ApptStorageControllerImpl from an existing storage of Appt */
	public ApptStorageControllerImpl(ApptStorage storage) {
		mApptStorage = storage;
	}

	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}
	
	/* retrieve all the appts */
	public Appt[] RetrieveAllAppts(){
		ArrayList<Appt> apptList = new ArrayList<Appt>(mApptStorage.mAppts.values());
		Appt[] apptArray = new Appt[apptList.size()];
		apptArray = apptList.toArray(apptArray);
		return apptArray;
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int joinApptID) {
		return mApptStorage.RetrieveAppts(joinApptID);
	}
	
	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	public void ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return;
			mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return;
			mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			mApptStorage.RemoveAppt(appt);
		} 
	}

	public void setCurrentUser(User user) {
		mApptStorage.setCurrentUser(user);
	}
	
	/* Get the currentUser of mApptStorage */
	public User getCurrentUser() {
		return mApptStorage.getCurrentUser();
	}
	
	public String getOverlapMessage() {
		return mApptStorage.getOverlapMessage();
	}

	public boolean isOverlap() {
		return mApptStorage.isOverlap();
	}

	// method used to load appointment from xml record into hash map
	public void LoadApptFromXml(){
		mApptStorage.LoadApptFromXml();
	}
	
	public void SaveApptToXml(){
		mApptStorage.SaveApptToXml();
	}
	
	public void LoadLocFromXml(){
		mApptStorage.LoadLocFromXml();
	}
	
	public void SaveLocToXml(){
		mApptStorage.SaveLocToXml();
	}
	
	public void SaveUserToXml(){
		mApptStorage.SaveUserToXml();
	}
	
	public void LoadUserFromXml(){
		mApptStorage.LoadUserFromXml();
	}
	/* get the locationList of mApptStorage */
	public Location[] getLocationList(){
		return mApptStorage.getLocationList();
	}
	
	/* set the locationList of mApptStorage */
	public void setLocationList(Location[] locations){
		mApptStorage.setLocationList(locations);
	}
	
	/* get the capacity of the locationList of mApptStorage */
	public int getLocationCapacity(){
		return mApptStorage.getLocationCapacity();
	}
	
	public int LengthInMemory() {
		return mApptStorage.LengthInMemory();
	}
	
	public ArrayList<User> getUserList(){
		return mApptStorage.getUserList();
	}
	
	public void addUser(User user){
		mApptStorage.addUser(user);
	}
	
	public void updateUser(User user){
		mApptStorage.updateUser(user);
	}
	
	public User searchUser(String username) {
		return mApptStorage.searchUser(username);
	}
	
	public void removeUser(String username){
		mApptStorage.removeUser(username);
	}
	
	public void removeUser(UUID userId){
		mApptStorage.removeUser(userId);
	}

	public boolean userExist(String userName){
		return mApptStorage.userExist(userName);
	}
}