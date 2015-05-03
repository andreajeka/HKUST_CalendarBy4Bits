package hkust.cse.calendar.unit;


import hkust.cse.calendar.users.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;


public class Appt implements Serializable {

	// Static variables for frequencies
	public static final int MODE_ONCE = 0;
	public static final int MODE_DAILY = 1;
	public static final int MODE_WEEKLY = 2;
	public static final int MODE_MONTHLY = 3;

	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments

	private String mTitle;						// The Title of the appointments

	private String mInfo;						// Store the content of the appointments description

	private int mApptID;						// The appointment id

	private int joinApptID;						// The join appointment id

	private boolean isjoint;					// The appointment is a joint appointment

	private LinkedList<UUID> attend;			// The Attendant list

	private LinkedList<UUID> reject;			// The reject list

	private LinkedList<UUID> waiting;			// The waiting list

	private Location location;					// The location

	private int frequencyAmount;				// The amount of frequency

	private boolean reminder;					// Reminder flag. True if it is turned on

	private long remindBefore;				    // Store how long should the reminder be activated before start time

	private boolean isPublic;					// Public flag. True if the creator of this event make this public

	private User initiator;

	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
		isjoint = false;
		attend = new LinkedList<UUID>();
		reject = new LinkedList<UUID>();
		waiting = new LinkedList<UUID>();
		joinApptID = -1;
		location = new Location("", -1);
		reminder = false;
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() {
		return mTimeSpan;
	}

	// Getter of the appointment title
	public String getTitle() {
		return mTitle;
	}

	// Getter of appointment description
	public String getInfo() {
		return mInfo;
	}

	// Getter of the appointment id
	public int getID() {
		return mApptID;
	}

	// Getter of the location string
	public String getLocationString() {
		return location.getName();
	}
	
	// Getter of the location
	public Location getLocation() {
		return location;
	}

	// Getter of the capacity
	public int getCapacity(){
		return location.getCapacity();
	}

	public int getFrequencyAmount() {
		return frequencyAmount;
	}

	// Getter of the join appointment id
	public int getJoinID(){
		return joinApptID;
	}

	public void setJoinID(int joinID){
		this.joinApptID = joinID;
	}
	// Getter of the attend LinkedList<String>
	public LinkedList<UUID> getAttendList(){
		return attend;
	}

	// Getter of the reject LinkedList<String>
	public LinkedList<UUID> getRejectList(){
		return reject;
	}

	// Getter of the waiting LinkedList<String>
	public LinkedList<UUID> getWaitingList(){
		return waiting;
	}

	public LinkedList<UUID> getAllPeople(){
		LinkedList<UUID> allList = new LinkedList<UUID>();
		allList.addAll(attend);
		allList.addAll(reject);
		allList.addAll(waiting);
		return allList;
	}

	public void addAttendant(UUID addID){
		if (attend == null)
			attend = new LinkedList<UUID>();
		attend.add(addID);
	}

	public void addReject(UUID addID){
		if (reject == null)
			reject = new LinkedList<UUID>();
		reject.add(addID);
	}

	public void addWaiting(UUID addID){
		if (waiting == null)
			waiting = new LinkedList<UUID>();
		waiting.add(addID);
	}

	public void setWaitingList(LinkedList<UUID> waitingList){
		waiting = waitingList;
	}

	public void setWaitingList(UUID[] waitingList){
		LinkedList<UUID> tempLinkedList = new LinkedList<UUID>();
		if (waitingList !=null){
			for (int a = 0; a < waitingList.length; a++){
				tempLinkedList.add(waitingList[a]);
			}
		}
		waiting = tempLinkedList;
	}

	public void setRejectList(LinkedList<UUID> rejectLinkedList) {
		reject = rejectLinkedList;
	}

	public void setRejectList(UUID[] rejectList){
		LinkedList<UUID> tempLinkedList = new LinkedList<UUID>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a]);
			}
		}
		reject = tempLinkedList;
	}

	public void setAttendList(LinkedList<UUID> attendLinkedList) {
		attend = attendLinkedList;
	}

	public void setAttendList(UUID[] attendList){
		LinkedList<UUID> tempLinkedList = new LinkedList<UUID>();
		if (attendList !=null){
			for (int a = 0; a < attendList.length; a++){
				tempLinkedList.add(attendList[a]);
			}
		}
		attend = tempLinkedList;
	}
	// Getter of the appointment title
	public String toString() {
		return mTitle;
	}

	// Setter of the appointment title
	public void setTitle(String t) {
		mTitle = t;
	}

	// Setter of the appointment description
	public void setInfo(String in) {
		mInfo = in;
	}

	// Setter of the mTimeSpan
	public void setTimeSpan(TimeSpan d) {
		mTimeSpan = d;
	}

	// Setter if the appointment id
	public void setID(int id) {
		mApptID = id;
	}

	// Setter of the location
	public void setLocation(String location, int capacity) {		
		this.location.setName(location);
		this.location.setCapacity(capacity);
	}
	


	// Setter of frequency amount
	public void setFrequencyAmount(int frequencyAmount) {
		this.frequencyAmount = frequencyAmount;
	}

	// check whether this is a joint appointment
	public boolean isJoint(){
		return isjoint;
	}

	// setter of the isJoint
	public void setJoint(boolean isjoint){
		this.isjoint = isjoint;
	}

	public boolean reminder() {
		return reminder;
	}

	public void reminderOn(boolean logic) {
		reminder = logic;
	}

	public boolean isPublic() {
		return isPublic;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
	}

	public long getRemindBefore() {
		return remindBefore;
	}

	public void setRemindBefore(long remindBefore) {
		this.remindBefore = remindBefore;
	}

	// Initiator related methods
	public void setInitiator(User initiator)
	{
		this.initiator = initiator;
	}

	// Initiator related methods
	public User getInitiator()
	{
		return initiator;
	}
}
