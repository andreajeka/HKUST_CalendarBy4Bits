package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.Utility;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Request;
import hkust.cse.calendar.unit.TimeSlotFeedback;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;

public class ApptStorageNullImpl extends ApptStorage {

	private XStream xstream;
	private User currentUser;
	private File xmlFile;
	private File locFile;
	private File userFile;
	private String overlapMessage="";
	private boolean isOverlap = false;
	private ArrayList<Location> _locations;
	private ArrayList<User> userList;
	private boolean capacityValidation;
	private boolean check;
	private ArrayList<Appt> removeApptList;

	public ApptStorageNullImpl()
	{
		mAppts = new HashMap<Integer, Appt>();
		xstream = new XStream();
		userList = new ArrayList<User>();
		mRq2DList = new ArrayList<ArrayList<Request>>();
		_locations = new ArrayList<Location>();
		ts2DList = new ArrayList<ArrayList<TimeSlotFeedback>>();
	}


	@Override
	public void SaveAppt(Appt appt) {

		if (appt.isJoint()) {
			int key = LengthInMemory() + 1;
			appt.setID(key);
			int jointKey = jointApptAmount( new ArrayList<Appt>(mAppts.values()));
			appt.setJoinID(jointKey);
			mAppts.put(key, appt);
		} else {
			Appt[] userApptList = RetrieveAppts(currentUser);
			ArrayList<Appt> apptList = new ArrayList<Appt>(Arrays.asList(userApptList));
			String digitHour="";
			if (!apptList.isEmpty()) {
				for (Appt anAppt : apptList) {
					if (anAppt.TimeSpan().Overlap(appt.TimeSpan())) {
						isOverlap = true;

						if (appt.TimeSpan().StartTime().getMinutes() < 10 )
							digitHour = "0";
						else digitHour = "";

						overlapMessage = "Your new appointment: [" + appt.getTitle() + "] at " 
								+ appt.TimeSpan().StartTime().getHours() + ":" + 
								digitHour + appt.TimeSpan().StartTime().getMinutes() + 
								" clashes with the following appointment: [" +
								anAppt.getTitle() + "] at " + anAppt.TimeSpan().StartTime().getHours() + ":" + 
								digitHour + anAppt.TimeSpan().StartTime().getMinutes() + "\n";

						return;

					} else 
						isOverlap = false;
				}
			}

			if (isOverlap == false) {
				// We put the pair appointment and its id into the HashMap
				int key = LengthInMemory() + 1;
				appt.setID(key);
				mAppts.put(key, appt);
				appt.addAttendant(currentUser.getUserId());
			}
		}
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// Retrieve appointment by time
		// Create an array list, add items in, convert back to regular array and return

		// Retrieve the whole Appointments (in a set of keys) into the container ArrayList
		ArrayList<Appt> apptList = new ArrayList<Appt>(mAppts.values());
		// Create a container ArrayList to contain the appointments which fall inside the requirement
		ArrayList<Appt> apptsByTime = new ArrayList<Appt>();

		for (Appt anAppt: apptList) {
			// Check which appointments is inside TimeSpan d
			if (Utility.AfterBeforeEqual(anAppt.TimeSpan().StartTime(), d.StartTime()) == 1  | 
					Utility.AfterBeforeEqual(anAppt.TimeSpan().StartTime(), d.StartTime()) == 0) {
				if (Utility.AfterBeforeEqual(anAppt.TimeSpan().EndTime(), d.EndTime()) == - 1 | 
						Utility.AfterBeforeEqual(anAppt.TimeSpan().EndTime(), d.EndTime()) == 0) {
					apptsByTime.add(anAppt);
				}
			}
		}
		if (apptsByTime.isEmpty()) {
			return null;
		}
		else {
			// Convert ArrayList back into array of Appt
			Appt[] apptArray = new Appt[apptsByTime.size()];
			apptArray = apptsByTime.toArray(apptArray);
			return apptArray;	
		}
	}		 	

	@Override		 	
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {	

		// Retrieve all appointments according to the specified timespan
		Appt[] apptList = RetrieveAppts(time);
		if (apptList == null) return null;
		// Create a new array list to contain list of appointments that involves currentUser
		ArrayList<Appt> userApptList = new ArrayList<Appt>();

		// Iterate through the list of retrieved appointments
		for (int i = 0; i < apptList.length; i++) {
			// Retrieve all the attendants (by its id) of the appointment
			ArrayList<UUID> allPeople = new ArrayList<UUID>(apptList[i].getAllPeople());
			// If the list of attendant contains current user, add that appointment to userApptList
			if (allPeople.contains(entity.getUserId()))
				userApptList.add(apptList[i]);
		}


		Appt[] userApptArray = new Appt[userApptList.size()];
		userApptArray = userApptList.toArray(userApptArray);
		return userApptArray;		
	}	

	public Appt[] RetrieveAppts(User entity) {

		ArrayList<Appt> apptList = new ArrayList<Appt>(mAppts.values());
		if (apptList == null) return null;

		// Create a new array list to contain list of appointments that involves currentUser
		ArrayList<Appt> userApptList = new ArrayList<Appt>();

		// Iterate through the list of retrieved appointments
		for (Appt appt : apptList) {
			// Retrieve all the attendants (by its id) of the appointment
			ArrayList<UUID> attendantList = new ArrayList<UUID>(appt.getAttendList());
			// If the list of attendant contains current user, add that appointment to userApptList
			if (attendantList.contains(currentUser.getUserId()))
				userApptList.add(appt);
		}

		Appt[] userApptArray = new Appt[userApptList.size()];
		userApptArray = userApptList.toArray(userApptArray);
		return userApptArray;
	}

	@Override
	public Appt RetrieveAppts(int apptID) {
		return mAppts.get(apptID);
	}


	public ArrayList<TimeSpan> RetrieveAvailTimeSpans(User entity, TimeSpan period) {
		ArrayList<TimeSpan> timeSlots = Utility.createTimeSlotsForADay(period);
		Appt[] temp = RetrieveAppts(entity, period);

		if (temp == null) return timeSlots;

		for(int j = 0; j < temp.length; j++) {
			int startTH = temp[j].TimeSpan().StartTime().getHours();
			int startTM = temp[j].TimeSpan().StartTime().getMinutes();
			int endTH = temp[j].TimeSpan().EndTime().getHours();
			int endTM = temp[j].TimeSpan().EndTime().getMinutes();
			int minutesDiff =  (endTH * 60 + endTM) - (startTH * 60 + startTM);

			int numSlots = minutesDiff / 15;

			for(int k = 0; k < numSlots; k++) {
				Timestamp start = new Timestamp(0);
				start.setYear(period.StartTime().getYear());
				start.setMonth(period.StartTime().getMonth());
				start.setDate(period.StartTime().getDate());
				start.setHours(startTH);
				start.setMinutes(startTM);

				startTM += 15;
				if (startTM == 60) {
					startTH++;
					startTM = 0;
				}

				Timestamp end = new Timestamp(0);
				start.setYear(period.EndTime().getYear());
				start.setMonth(period.EndTime().getMonth());
				start.setDate(period.EndTime().getDate());
				end.setHours(startTH);
				end.setMinutes(startTM);

				TimeSpan aSlot = new TimeSpan(start,end);
				for (TimeSpan ts : timeSlots) {
					if ((ts.StartTime().getHours() == aSlot.StartTime().getHours()) && 
							(ts.StartTime().getMinutes() == aSlot.StartTime().getMinutes())) {
						if ((ts.EndTime().getHours() == aSlot.EndTime().getHours()) && 
								(ts.EndTime().getMinutes() == aSlot.EndTime().getMinutes())) {
							timeSlots.remove(ts);
							break;
						}
					}
				}
			}
		}

		if (timeSlots.isEmpty()) return null;
		else return timeSlots;
	}

	// We cannot simply use RetrieveAvailTimeSpans for a user and add all up because there will be duplicates
	@Override
	public ArrayList<TimeSpan> RetrieveAvailTimeSpans(ArrayList<User>  entities, TimeSpan period) {
		ArrayList<TimeSpan> timeSlots = Utility.createTimeSlotsForADay(period);
		for (int i = 0; i < entities.size(); i++) {

			Appt[] temp = RetrieveAppts(entities.get(i), period);

			if (temp == null) continue;

			for(int j = 0; j < temp.length; j++) {
				int startTH = temp[j].TimeSpan().StartTime().getHours();
				int startTM = temp[j].TimeSpan().StartTime().getMinutes();
				int endTH = temp[j].TimeSpan().EndTime().getHours();
				int endTM = temp[j].TimeSpan().EndTime().getMinutes();
				int minutesDiff =  (endTH * 60 + endTM) - (startTH * 60 + startTM);

				int numSlots = minutesDiff / 15;

				for(int k = 0; k < numSlots; k++) {
					Timestamp start = new Timestamp(0);
					start.setYear(period.StartTime().getYear());
					start.setMonth(period.StartTime().getMonth());
					start.setDate(period.StartTime().getDate());
					start.setHours(startTH);
					start.setMinutes(startTM);

					startTM += 15;
					if (startTM == 60) {
						startTH++;
						startTM = 0;
					}

					Timestamp end = new Timestamp(0);
					start.setYear(period.EndTime().getYear());
					start.setMonth(period.EndTime().getMonth());
					start.setDate(period.EndTime().getDate());
					end.setHours(startTH);
					end.setMinutes(startTM);

					TimeSpan aSlot = new TimeSpan(start,end);
					for (TimeSpan ts : timeSlots) {
						if ((ts.StartTime().getHours() == aSlot.StartTime().getHours()) && 
								(ts.StartTime().getMinutes() == aSlot.StartTime().getMinutes())) {
							if ((ts.EndTime().getHours() == aSlot.EndTime().getHours()) && 
									(ts.EndTime().getMinutes() == aSlot.EndTime().getMinutes())) {
								timeSlots.remove(ts);
								break;
							}
						}
					}
				}
			}
		}
		if (timeSlots.isEmpty()) return null;
		else return timeSlots;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		int apptID = appt.getID();
		// According to Java Doc, If the map previously contained a mapping for this key, 
		// the old value is replaced by the specified value.
		mAppts.put(apptID, appt);
		if (!appt.isJoint())
			appt.addAttendant(currentUser.getUserId());
	}

	@Override
	public void RemoveAppt(Appt appt) {
		if (appt.isJoint()) {
			// Cannot delete a pending request --> easier to implement

			if (!appt.getWaitingList().isEmpty()) {
				return;
			}
			
			// Don't allow other than initiator to delete it
			if (!getCurrentUser().equals(appt.getInitiator())) 
				return;

		}
			// Whether it is a private or confirmed group event(only for initiator),
			// Just simply delete it
			mAppts.remove(appt.getID(), appt);

	}

	public void setCurrentUser(User user) {
		currentUser = user;
	}

	@Override
	public User getCurrentUser() {
		return currentUser;
	}

	public String getOverlapMessage() {
		return overlapMessage;
	}

	public boolean isOverlap() {
		return isOverlap;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void LoadApptFromXml() {
		try{
			xmlFile = new File("saveAppt.xml");
			if(xmlFile.exists() && xmlFile.isFile()){
				mAppts = (HashMap<Integer, Appt>)xstream.fromXML(xmlFile);
			}
		}catch(Exception e){
			System.out.println("loadApptFailed");
		}
	}

	@Override
	public void SaveApptToXml(){
		try {
			xstream.toXML(mAppts, new FileWriter("saveAppt.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void LoadLocFromXml(){
		try{
			locFile = new File("locations.xml");
			if(locFile.exists() && locFile.isFile()){
				_locations = (ArrayList<Location>)xstream.fromXML(locFile);
				//System.out.println("enter loadLocFromXml" + _locations.length);
			} 
		}catch (Exception e){
			System.out.println("loadLocationFailed");
		}
	}

	@Override
	public void SaveLocToXml(){
		try{
			xstream.toXML(_locations, new FileWriter("locations.xml"));
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public void LoadUserFromXml(){
		try{
			userFile = new File("userList.xml");
			if(userFile.exists() && userFile.isFile()){
				userList = (ArrayList<User>)xstream.fromXML(userFile);
			}
		}catch (Exception e){
			System.out.println("loadUserListFailed");
		}
	}
	@Override
	public void SaveUserToXml(){
		try{
			xstream.toXML(userList, new FileWriter("userList.xml"));
		} catch(IOException e){
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Location> getLocationList(){
		return _locations;
	}

	@Override
	public void setLocationList(ArrayList<Location> locations){
		this._locations = locations;
	}

	@Override
	public int getLocationCapacity(){
		return this._locations.size();
	}

	@Override
	// Return the length of mAppts
	public int LengthInMemory() {
		return mAppts.size();
	}	

	@Override
	public ArrayList<User> getUserList(){
		return userList;
	}

	@Override
	public void addUser(User user){
		// Duplication is checked in logindialog
		userList.add(user);
	}

	@Override
	public void updateUser(User user){
		if(!userList.isEmpty()){
			for(int i=0; i<userList.size(); i++){
				if(user.getUserId() == userList.get(i).getUserId()){
					userList.get(i).setUsername(user.getUsername());
					userList.get(i).setPassword(user.getPassword());
					userList.get(i).setFirstName(user.getFirstName());
					userList.get(i).setLastName(user.getLastName());
				}
			}
		}
		else
			JOptionPane.showMessageDialog(null, "The user list is empty", "invalid input", JOptionPane.WARNING_MESSAGE);
	}

	// Search and return an instance of User class based on the username
	// Heavily needed for login and sign up
	public User searchUser(String username) {
		User target = null;
		if (!userList.isEmpty()){
			for (User u : userList) {
				if (u.getUsername().equals(username)) {
					target = u;
					break;
				}
			}
		}
		return target;
	}

	// Search and return an instance of User class based on the userid
	public User searchUser(UUID userID) {
		User target = null;
		if (!userList.isEmpty()){
			for (User u : userList) {
				if (u.getUserId().equals(userID)) {
					target = u;
					break;
				}
			}
		}
		return target;
	}

	@Override
	public void removeUser(String username){
		if(!userList.isEmpty()){
			for(int i=0; i<userList.size(); i++){
				if(userList.get(i).getUsername().equals(username)){
					userList.remove(i);
					break;
				}
			}
		}
	}

	@Override
	public void removeUser(UUID userId){
		if(!userList.isEmpty()){
			for(int i=0; i<userList.size(); i++){
				if(userList.get(i).getUserId().equals(userId)){
					userList.remove(i);
					break;
				}
			}
		}
	}

	// Locations
	@Override
	public void removeLocation(Location location)
	{
		// Remove appts related to this location
		for(Appt appt : mAppts.values())
		{
			if (appt.getLocationString().equals(location.getName())){
				RemoveAppt(appt);
			}
		}

		_locations.remove(location);
	}

	// Request
	@Override
	public void addRequest(Request rq)
	{
		ArrayList<Request> rqList = new ArrayList<Request>();
		rqList.add(rq);
		mRq2DList.add(rqList);
	}

	@Override
	public void SaveRequestsToXml()
	{
		try {
			xstream.toXML(mRq2DList, new FileWriter("Request.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void LoadRequestsFromXml()
	{
		try{
			xmlFile = new File("Request.xml");
			if(xmlFile.exists() && xmlFile.isFile()){
				mRq2DList = (ArrayList<ArrayList<Request>>)xstream.fromXML(xmlFile);
			}
		}catch(Exception e){
			System.out.println("loadRequestFailed");
		}
	}

	public boolean capaValidation(String location, int numOfUsers){
		capacityValidation = false;
		if(numOfUsers < 0 || numOfUsers == 0){
			JOptionPane.showMessageDialog(null, "The number of user should be a positive integer.", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		if(_locations != null){
			for(int i=0; i<_locations.size(); i++){

				if(_locations.get(i).getName().equals(location)){
					if(numOfUsers == _locations.get(i).getCapacity() || numOfUsers < _locations.get(i).getCapacity())
						capacityValidation = true;
					else
						capacityValidation = false;
				}
			}
		}
		else
			capacityValidation = false;
		return capacityValidation;

	}
	
	public boolean checkLocation(String location){
		check = true;
		for(Appt i: mAppts.values()){
			if(i.getLocationString().equals(location)){
				check = false;
				break;
			}
		}
		return check;
	}
	
	public void removeUserAppts(UUID userId){
		removeApptList = new ArrayList<Appt>();
		LoadApptFromXml();
		//System.out.println(userId);
		for(Appt a: mAppts.values()){
			//System.out.println(a.getAllPeople().size() + " outside");
			for(UUID id: a.getAllPeople()){
				// delete event if single user created
				if(id.equals(userId)){
					if(a.getAllPeople().size() == 1){
						//System.out.println(a.getAllPeople().size());
						removeApptList.add(a);
						//System.out.println(a.hashCode() + " inside the for loop");
					}
					// remove from attend or waiting list
					else if(a.getAttendList().contains(userId))
						a.removeFromAttend(userId);
					else if(a.getWaitingList().contains(userId))
						a.removeFromWaiting(userId);
				}
			}
		}
		for(Appt i: removeApptList){
			RemoveAppt(i);
		}
		SaveApptToXml();
	}

	private int jointApptAmount (ArrayList<Appt> apptList) {
		int count = 0;
		if (!apptList.isEmpty())
			for (Appt appt : apptList) {
				if (appt.isJoint())
					count++;
			}
		return count;
	}


	@Override
	public void SaveFeedbacksToXml() {
		try {
			xstream.toXML(ts2DList, new FileWriter("TimeSlotFeedback.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void LoadFeedbacksFromXml() {
		// TODO Auto-generated method stub
		try{
			xmlFile = new File("TimeSlotFeedback.xml");
			if(xmlFile.exists() && xmlFile.isFile()){
				ts2DList = (ArrayList<ArrayList<TimeSlotFeedback>>)xstream.fromXML(xmlFile);
			}
		}catch(Exception e){
			System.out.println("loadTimeSlotFeedbackFailed");
		}
	}


	@Override
	public void addFeedback(TimeSlotFeedback feedback) {
		int index = feedback.getfeedBackID() - 1;
		ArrayList<TimeSlotFeedback> tsFeedbackList = ts2DList.get(index);
		if (tsFeedbackList.isEmpty()) {
			tsFeedbackList = new ArrayList<TimeSlotFeedback>();
		}
		tsFeedbackList.add(feedback);
	}
	
	@Override
	public int getFeedBacksListCapacity() {
		return ts2DList.size();
	}
}
