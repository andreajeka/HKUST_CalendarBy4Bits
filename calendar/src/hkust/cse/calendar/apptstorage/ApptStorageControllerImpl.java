package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.UUID;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Request;
import hkust.cse.calendar.unit.Request.type;
import hkust.cse.calendar.unit.TimeSlotFeedback;
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
	
	public Appt[] RetrieveAppts(User entity) {
		return mApptStorage.RetrieveAppts(entity);
	}
	
	/* retrieve all the appts */
	public Appt[] RetrieveAllAppts(){
		ArrayList<Appt> apptList = new ArrayList<Appt>(mApptStorage.mAppts.values());
		Appt[] apptArray = new Appt[apptList.size()];
		apptArray = apptList.toArray(apptArray);
		return apptArray;
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int apptID) {
		return mApptStorage.RetrieveAppts(apptID);
	}
	
	public ArrayList<TimeSpan> RetrieveAvailTimeSpans(User entity, TimeSpan period) {
		return mApptStorage.RetrieveAvailTimeSpans(entity, period);
	}
	
	public ArrayList<TimeSpan> RetrieveAvailTimeSpans(ArrayList<User> entities, TimeSpan period) {
		return mApptStorage.RetrieveAvailTimeSpans(entities, period);
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
	public ArrayList<Location> getLocationList(){
		return mApptStorage.getLocationList();
	}
	
	// Get location through index
	public Location getLocation(int index){
		return mApptStorage.getLocationList().get(index);
	}
	
	/* set the locationList of mApptStorage */
	public void setLocationList(ArrayList<Location> locations){
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
	
	public User searchUser(UUID userID) {
		return mApptStorage.searchUser(userID);
	}
	
	public void removeUser(String username){
		mApptStorage.removeUser(username);
	}
	
	public void removeUser(UUID userId){
		mApptStorage.removeUser(userId);
	}
	
	// location
	public void removeLocation(Location location)
	{
		mApptStorage.removeLocation(location);
	}
	
	// Request
	public ArrayList<ArrayList<Request>> getRequestList()
	{
		return mApptStorage.mRq2DList;
	}
	
	public void setRequestList(ArrayList<ArrayList<Request>> list)
	{
		mApptStorage.mRq2DList = list;
	}
	
	public void addRequest(Request rq)
	{
		mApptStorage.addRequest(rq);
	}
	
	public void distributeRequest()
	{
		ArrayList<ArrayList<Request>> rq2DList = mApptStorage.mRq2DList;
		if(!rq2DList.isEmpty())
		{
			for (ArrayList<Request> rqList : rq2DList)
			{
				Request rq = rqList.get(0);
				if(rq.TYPE == Request.type.DELETE_USER && rq._receiver == null)
				{
					User user = (User) rq._obj;
					boolean deleteFlag = false;
					for(Appt appt : RetrieveAllAppts())
					{
						if(!appt.getInitiator().equals(user) && (appt.getAttendList().contains(user.getUserId()) || appt.getWaitingList().contains(user.getUserId())))
						{
							rqList.add(new Request(mApptStorage.getCurrentUser(), appt.getInitiator(), rq.TYPE, rq._obj));
							if (!deleteFlag) deleteFlag = true;
						}
					}
					if (deleteFlag) rqList.remove(rq);
					else rq._receiver = user;
				}
				else if (rq.TYPE ==  Request.type.DELETE_LOCATION && rq._receiver == null)
				{
					boolean deleteFlag = false;
					for(Appt appt : RetrieveAllAppts())
					{
						if(appt.getLocationString().equals(((Location) rq._obj).getName()))
						{
							rqList.add(new Request(mApptStorage.getCurrentUser(), appt.getInitiator(), rq.TYPE, rq._obj));
							if (!deleteFlag) deleteFlag = true;
						}
					}
					if (deleteFlag) rqList.remove(rq);
					else 
					{
						rq2DList.remove(rqList);
						removeLocation((Location) rq._obj);
					}
				}
				if(rq2DList.isEmpty()) break;
			}
			mApptStorage.mRq2DList = rq2DList;
		}
	}
	
	public void sortRequest()
	{
		ArrayList<ArrayList<Request>> rq2DList = mApptStorage.mRq2DList;
		Collections.sort
		(
			rq2DList, 
			new Comparator<ArrayList<Request>>() 
			{
				@Override
				public int compare(ArrayList<Request> rqList1, ArrayList<Request> rqList2) 
				{
					Request rq1 = rqList1.get(0);
					Request rq2 = rqList2.get(0);
					if (rq1.TYPE._value < rq2.TYPE._value)
						return -1;
					else if (rq1.TYPE._value > rq2.TYPE._value)
						return 1;
					else
						return 0;
				}
			}
		);
		mApptStorage.mRq2DList = rq2DList;
	}
	
	public void LoadRequestFromXml(){
		mApptStorage.LoadRequestsFromXml();
	}
	
	public void SaveRequestToXml(){
		mApptStorage.SaveRequestsToXml();
	}

	public boolean capaValidation(String location, int numOfUsers){
		return mApptStorage.capaValidation(location, numOfUsers);
	}
	
	public boolean checkLocation(String location){
		return mApptStorage.checkLocation(location);
	}
	
	public void removeUserAppts(UUID userId){
		mApptStorage.removeUserAppts(userId);
	}
	
	public void SaveFeedbacksToXml() {
		mApptStorage.SaveFeedbacksToXml();
	}
	
	public void LoadFeedbacksFromXml(){
		mApptStorage.LoadFeedbacksFromXml();
	}
	
	public void addFeedback(TimeSlotFeedback feedback) {
		mApptStorage.addFeedback(feedback);
	}
	
	public int getFeedBacksListCapacity() {
		return mApptStorage.getFeedBacksListCapacity();
	}

	// Request
	public ArrayList<ArrayList<TimeSlotFeedback>> getFeedbackList()
	{
		return mApptStorage.ts2DList;
	}
	
	public void setFeedbackList(ArrayList<ArrayList<TimeSlotFeedback>> list)
	{
		mApptStorage.ts2DList = list;
	}
	
}
