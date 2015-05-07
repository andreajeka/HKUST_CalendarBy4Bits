package hkust.cse.calendar.unit;

import java.util.ArrayList;

import hkust.cse.calendar.users.User;

// 3 types of request: invitation, delete user, delete location

// Usage examples
// Using controller.addRequest.(new Request(..))
// Delete a user: Request(curr user, null, Request.type.DELETE_USER, user)
// Delete a location: Request(curr user, null, Request.type.DELETE_LOCATION, location)
// Invite a user: Request(initiator, user, Request.type.INVITE, appt)

public class Request {
	User _sender;
	public User _receiver;
	public enum type
	{
		INVITE(0), DELETE_LOCATION(1), DELETE_USER(2);
		public int _value;
		type(int value)
		{
			_value = value;
		}
	};
	public type TYPE;
	public Object _obj; // can be appt/location/user
	public ArrayList<TimeSpan> datesChosen = null;
	boolean bSeen;
	boolean bAccepted;
	public int duration = 0;
	public int feedbackID = 0;
	private String title = "";
	private String desc = "";
	private Location location = null;
	
	// Default constructor
	public Request(User sender, User receiver, type type, Object obj)
	{
		// Assertion
		assert(!sender.equals(receiver));
		assert(obj.equals(Appt.class) || obj.equals(Location.class) || obj.equals(User.class));
		
		_sender = sender;
		_receiver = receiver;
		TYPE = type;
		_obj = obj;
		bSeen = false;
		bAccepted = false;
	}
	
	// TODO New Constructor
	public Request(User sender, User receiver, type type, ArrayList<TimeSpan> datesChosen, int duration, int id) {
		// Assertion
		assert(!sender.equals(receiver));
		
		_sender = sender;
		_receiver = receiver;
		TYPE = type;
		this.datesChosen = datesChosen;
		bSeen = false;
		bAccepted = false;
		this.duration = duration;
		this.feedbackID = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}