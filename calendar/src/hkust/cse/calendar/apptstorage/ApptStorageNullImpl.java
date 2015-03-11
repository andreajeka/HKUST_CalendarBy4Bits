package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;
import java.util.HashMap;

public class ApptStorageNullImpl extends ApptStorage {

	private User defaultUser = null;
	
	public ApptStorageNullImpl( User user )
	{
		defaultUser = user;
		mAppts = new HashMap();
	}
	
	@Override
	public void SaveAppt(Appt appt) {
		mAssignedApptID = appt.getID();
		mAppts.put(appt,appt.getID());
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void UpdateAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public void RemoveAppt(Appt appt) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub

	}
	
	/* creating a Location array */
	Location[] _locations;
	
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
}