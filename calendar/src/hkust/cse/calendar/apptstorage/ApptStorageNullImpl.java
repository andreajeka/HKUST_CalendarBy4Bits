package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.Utility;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.thoughtworks.xstream.XStream;

public class ApptStorageNullImpl extends ApptStorage {

	private User defaultUser = null;
	private XStream xstream;
	private Location[] _locations;
	private File xmlFile;
	private File locFile;
	private String overlapMessage="";
	private boolean isOverlap = false;
	private boolean checkDuplicateUser;
	private ArrayList<User> userList = new ArrayList<User>();

	/************* MY TASKS ************/
	// public getUserList
	// public addUser (check the userList(authenticate) before adding a new user)
	// public update(change the info of user, eg. username, password etc)
	/************* MY TASKS ************/

	// private(should be public) (boolean) authenticate
	// public boolean/User getUser(User user);



	public ApptStorageNullImpl( User user )
	{
		defaultUser = user;
		mAppts = new HashMap<Integer, Appt>();
		xstream = new XStream();
	}


	@Override
	public void SaveAppt(Appt appt) {
		ArrayList<Appt> apptList = new ArrayList<Appt>(mAppts.values());
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
			// The below code returns false, which is weird.
			// System.out.println(anAppt.TimeSpan().EndTime().before(d.EndTime()));
			// The below code returns true, which is weird.
			// System.out.println(anAppt.TimeSpan().EndTime().after(d.EndTime()));

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
		// TODO Retrieve Appointments by time and user (for now its default)
		// Call RetrieveAPpts with time parameter
		if (entity.equals(defaultUser)) {
			return RetrieveAppts(time);
		}
		return null;
	}		 	

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// RetrieveAppts with joinApptID
		return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		int apptID = appt.getID();
		// According to Java Doc, If the map previously contained a mapping for this key, 
		// the old value is replaced by the specified value.
		mAppts.put(apptID, appt);
	}

	@Override
	public void RemoveAppt(Appt appt) {
		mAppts.remove(appt.getID(), appt);
	}

	@Override
	public User getDefaultUser() {
		return defaultUser;
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
			System.out.println("loadApptError");
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
				_locations = (Location[])xstream.fromXML(locFile);
				//System.out.println("enter loadLocFromXml" + _locations.length);
			} 
		}catch (Exception e){
			System.out.println("loadLocationError");
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
	public Location[] getLocationList(){
		return _locations;
	}

	@Override
	public void setLocationList(Location[] locations){
		this._locations = locations;
	}

	@Override
	public int getLocationCapacity(){
		return this._locations.length;
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
		checkDuplicateUser = true;
		if(!userList.isEmpty()){
			for(int i=0; i<userList.size(); i++){
				if(user.getUsername() == userList.get(i).getUsername()){
					checkDuplicateUser = false;
					break;
				}
			}
		}
		if(checkDuplicateUser)
			userList.add(user);
		else
			JOptionPane.showMessageDialog(null, "Duplicate User Name", "invalid input", JOptionPane.WARNING_MESSAGE);
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
}