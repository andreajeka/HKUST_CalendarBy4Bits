package hkust.cse.calendar.unit;

import java.util.ArrayList;

import hkust.cse.calendar.users.User;

public class TimeSlotFeedback {
	private User initiator;
	private User receiver;
	private int feedBackID; 
	// For a specific feedback id (to a get one potential appointment)
	private ArrayList<TimeSpan> listOfTimeSlots;
	
	public TimeSlotFeedback(User initiator, User receiver, int feedBackID) {
		this.initiator = initiator;
		this.receiver = receiver;
		this.feedBackID = feedBackID;
	}
	
	public User getInitiator() {
		return initiator;
	}

	public int getfeedBackID() {
		return feedBackID;
	}
	
	public ArrayList<TimeSpan> getlistOfTimeSlots() {
		return listOfTimeSlots;
	}

	public void setlistOfTimeSlots(ArrayList<TimeSpan> listOfTimeSlots) {
		this.listOfTimeSlots = listOfTimeSlots;
	}

	public User getReceiver() {
		return receiver;
	}


	
	
}
