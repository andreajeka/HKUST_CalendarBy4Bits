package hkust.cse.calendar.unit;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javafx.scene.Parent;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.gui.Utility;
import hkust.cse.calendar.notification.MessageNoti;
import hkust.cse.calendar.notification.Notification;
import hkust.cse.calendar.notification.OptionNoti;
import hkust.cse.calendar.notification.OptionTimeSlot;
import hkust.cse.calendar.users.User;


public class RequestChecker {
	private static RequestChecker instance = null;
	private ArrayList<ArrayList<Request>> _rq2DList; // local request list
	private ApptStorageControllerImpl _controller;
	private User _user;
	
	private enum notiReturnCode
	{
		NOTI_LOGIN_FAIL, NOTI_REFUSE, NOTI_OK // tidy up if one of the initiator refuse to delete user/location
	};
	
	// Constructor
	private RequestChecker(){};
	
	public static RequestChecker getInstance() 
	{
		if (instance == null)
			instance = new RequestChecker();
		return instance;
    }
	
	// Carry out checking and pop out noti if needed
	public boolean Check(ApptStorageControllerImpl controller, User user)
	{
		_user = user;
		_controller = controller;
		_rq2DList = controller.getRequestList();
		
		notiReturnCode code;
		// Check for deletion request first
		for(int i = 0; i < _rq2DList.size(); i++)
		{
			ArrayList<Request> rqList = _rq2DList.get(i);
			for(Request rq : rqList)
			{
				if(rq._receiver.equals(_user))
				{
					switch(rq.TYPE)
					{
						default:
						case DELETE_USER:
							code = userRemoval(rq);
							break;
						case DELETE_LOCATION:
							code = locationRemoval(rq);
							break;
						case INVITE:
							code = apptInvitation(rq);
					}
					
					// Follow up work
					switch(code)
					{
						case NOTI_LOGIN_FAIL:
							cleanRequests(rqList);
							return false;
						case NOTI_REFUSE:
							if (rq.TYPE == Request.type.INVITE)
								_rq2DList.remove(rqList);
							else cleanRequests(rqList);
							break;
						case NOTI_OK:
							confirmRequests(rqList, rq);
					}
				}
				if(rqList.isEmpty()) break;
			}
			if(_rq2DList.isEmpty()) break;
		}
		_controller.setRequestList(_rq2DList);
		return true;
	}
	
	notiReturnCode apptInvitation(Request rq)
	{
		// TODO instance of Option Time Slot
		if (rq.datesChosen != null) {
			OptionTimeSlot ots = new OptionTimeSlot(_controller, _user, rq.datesChosen, rq.duration, rq._sender);
			
			int result = JOptionPane.showConfirmDialog(null, ots, "Please select your available timeslots", JOptionPane.OK_CANCEL_OPTION);
			
			if (result == JOptionPane.OK_OPTION) {
				TimeSlotFeedback feedback = new TimeSlotFeedback(rq._sender, rq._receiver, rq.feedbackID);
				for (int i = 0; i < ots.getUserFeedback().size(); i++) {
					/*System.out.println(ots.getUserFeedback().get(i).StartTime().getHours() + " " + 
							ots.getUserFeedback().get(i).StartTime().getMinutes());
			*/	}
				feedback.setlistOfTimeSlots(ots.getUserFeedback());
				_controller.addFeedback(feedback);
				_controller.SaveFeedbacksToXml();
				return notiReturnCode.NOTI_OK;
			}
			
		} else {
			// For timeslot invitation, if dateschosen list is not empty, we create new OptionTimeSlot
			if(new OptionNoti("Request from initiator", "Join event: " + ((Appt) rq._obj).getTitle() + "?").popUp())
			{
				return notiReturnCode.NOTI_OK;
			}
		}
			// If uncomment everything above, you should comment the following else and just return notiReturnCode.NOTI_REFUSE
			return notiReturnCode.NOTI_REFUSE;
	}
	
	notiReturnCode locationRemoval(Request rq)
	{		
		if(new OptionNoti("Request from admin", "Allow delete of location: " + ((Location) rq._obj).getName() + "?").popUp())
		{
			return notiReturnCode.NOTI_OK;
		}
		else return notiReturnCode.NOTI_REFUSE;
	}
	
	notiReturnCode userRemoval(Request rq)
	{
		// CASE 1: this user is an initiator of one of the appts the deleted user participate
		if(!_user.equals(rq._obj))
		{
			if(new OptionNoti("Request from admin", "Allow delete of user: " + _user.getUsername() + "?").popUp())
			{
				return notiReturnCode.NOTI_OK;
			}
			else return notiReturnCode.NOTI_REFUSE;
		}
		
		// CASE 2: this user is the deleted user
		else
		{
			_controller.removeUser(_user.getUserId());
			_controller.removeUserAppts(_user.getUserId());
			// Because returning to the login dialog
			_controller.SaveRequestToXml();
			_controller.SaveUserToXml();
			// use noti service to notice
			new MessageNoti("Request from admin", "You are removed.").popUp();
			return notiReturnCode.NOTI_LOGIN_FAIL;
		}
	}
		
	// Remove deletion request if one of the initiator refused
	void cleanRequests(ArrayList<Request> rqList)
	{
		// For display purpose in management users dialog
		if(rqList.get(0).TYPE == Request.type.DELETE_USER)
			_controller.searchUser(((User) rqList.get(0)._obj).getUserId()).setTobeRemoved(false);
		// For display purpose in location dialog
		if(rqList.get(0).TYPE == Request.type.DELETE_LOCATION)
		{
			ArrayList<Location> locationList = _controller.getLocationList();
			for (Location lt : locationList)
				if(lt.getName().equals(((Location) rqList.get(0)._obj).getName()))
					lt.setRemovalBool(false);
			_controller.setLocationList(locationList);
		}
		
		_rq2DList.remove(rqList);
	};
	
	// Create user removal to user if all initiator confirmed
	// OR create appt if all users confirmed
	void confirmRequests(ArrayList<Request> rqList, Request rq)
	{
		rqList.remove(rq);
		if(rqList.isEmpty())
		{
			switch(rq.TYPE)
			{
				default:
				case DELETE_USER:
					rqList.add(new Request(rq._sender, (User) rq._obj, Request.type.DELETE_USER, rq._obj));
					break;
				case DELETE_LOCATION:
					_controller.removeLocation((Location) rq._obj);
					_rq2DList.remove(rqList);
					break;
				case INVITE:
					if (rq.feedbackID != 0) {
						
						if (!stillWaitingForFB(rq.feedbackID)) {
							ArrayList<TimeSlotFeedback> feedbackList = _controller.getFeedbackList().get(rq.feedbackID - 1);
							ArrayList<TimeSpan> timeSlotList = new ArrayList<TimeSpan>();
							
							// Condense list to get available time for all users
							for (TimeSlotFeedback feedback : feedbackList) {
								ArrayList<TimeSpan> oneListOfTimeSlot = feedback.getlistOfTimeSlots();
								ArrayList<TimeSpan> newAvailability = new ArrayList<TimeSpan>();
								// If first time adding get a list as the base list of comparison
								if (timeSlotList.isEmpty()) {
									timeSlotList.addAll(oneListOfTimeSlot);
								} else {
									for (TimeSpan ts : oneListOfTimeSlot) {
										for (TimeSpan tsInList : timeSlotList) {
											
											// If all attributes are the same, continue looping
											if ((ts.StartTime().getYear() == tsInList.StartTime().getYear()) &&
												(ts.StartTime().getMonth() == tsInList.StartTime().getMonth()) &&
												(ts.StartTime().getDate() == tsInList.StartTime().getDate())) {
													if ((ts.StartTime().getHours() == tsInList.StartTime().getHours()) && 
														(ts.StartTime().getMinutes() == tsInList.StartTime().getMinutes())) {
															if ((ts.EndTime().getHours() == tsInList.EndTime().getHours()) && 
																(ts.EndTime().getMinutes() == tsInList.EndTime().getMinutes())) {
																newAvailability.add(tsInList);
																break; // Break inner loop
															}
													}
											} 
										}
									}

									timeSlotList.clear();
									// Reassign
									timeSlotList = newAvailability;
								}
							}
							//System.out.println(timeSlotList);
							// After condensing, find the earliest time slot from all available time with corresponding duration
							ArrayList<TimeSpan> aTimeSpan = Utility.getEarliestTimeSlot(timeSlotList, rq.duration);
							/*
							
							// Base case a) array empty or duration is 0
							if (timeSlotList.isEmpty() || rq.duration == 0) aTimeSpan = null;
							
							// We break down the duration(in mins) to number of slots
							int numOfSlots = rq.duration / 15;
							
							// Base case b) array only contain one list
							if (timeSlotList.size() == numOfSlots) 
								aTimeSpan = timeSlotList;
							
							else if (numOfSlots == 1) {
								ArrayList<TimeSpan> earliestTS = new ArrayList<TimeSpan>();
								earliestTS.add(timeSlotList.get(0));
								aTimeSpan = earliestTS;
							} else {
								int countingSlot = 1;
								int index = -1;
								// Get the first timeslot as the basis
								TimeSpan pointerSlot = timeSlotList.get(0);
							
								// Loop through the list
								for (int i = 1; i < timeSlotList.size(); i++) {
							
									System.out.println(Utility.AfterBeforeEqual(pointerSlot.EndTime(), timeSlotList.get(i).StartTime()));
									if (Utility.AfterBeforeEqual(pointerSlot.EndTime(), timeSlotList.get(i).StartTime()) == 0) {
											System.out.println("Got in");
											countingSlot++;
											// Satisfy slot requirement
											if (countingSlot == numOfSlots) {
												index = i + 1 - numOfSlots;
												break;
											}
									} else countingSlot = 1;
										// if not satisfy slot req yet, set pointerSlot to the next one
										pointerSlot = timeSlotList.get(i);
									
								}
							
								// Not found
								if (index == -1) aTimeSpan = null;
								else {
									int i = index;
									ArrayList<TimeSpan> earliestTS = new ArrayList<TimeSpan>();
									while (numOfSlots > 0) {
										earliestTS.add(timeSlotList.get(i));
										i++;
										numOfSlots--;
									}
									aTimeSpan = earliestTS;
								}
							}*/
							
							//System.out.println(aTimeSpan);
							if (aTimeSpan != null) {
								TimeSpan first = aTimeSpan.get(0);
								TimeSpan last = aTimeSpan.get(aTimeSpan.size() - 1);
							
								Timestamp start = new Timestamp(0);
								start.setYear(first.StartTime().getYear());
								start.setMonth(first.StartTime().getMonth());
								start.setDate(first.StartTime().getDate());
								start.setHours(first.StartTime().getHours());
								start.setMinutes(first.StartTime().getMinutes());
								//System.out.println(first.StartTime().getHours() + ":" + first.StartTime().getMinutes());
							
								Timestamp end = new Timestamp(0);
								end.setYear(last.EndTime().getYear());
								end.setMonth(last.EndTime().getMonth());
								end.setDate(last.EndTime().getDate());
								end.setHours(last.EndTime().getHours());
								end.setMinutes(last.EndTime().getMinutes());
								//System.out.println(last.EndTime().getHours() + ":" + last.EndTime().getMinutes());
								TimeSpan merged = new TimeSpan(start, end);
								
								Appt NewAppt = new Appt();
								NewAppt.setTitle(rq.getTitle());
								NewAppt.setInfo(rq.getDesc());
								NewAppt.setLocation(rq.getLocation().getName(), rq.getLocation().getCapacity());
								NewAppt.setTimeSpan(merged);
								NewAppt.setJoint(true);
								NewAppt.setInitiator(rq._sender);
								for (UUID id : rq.getParticipants()) {
									NewAppt.addAttendant(id);
								}
								NewAppt.addAttendant(rq._sender.getUserId());
								_controller.ManageAppt(NewAppt, ApptStorageControllerImpl.NEW);
								_controller.getFeedbackList().remove(rq.feedbackID - 1);
							}
						}
					} else {
						((Appt) rq._obj).moveFromWaitToAttend(rq._receiver.getUserId());
						_controller.ManageAppt(((Appt) rq._obj), ApptStorageControllerImpl.MODIFY);
					}
					_rq2DList.remove(rqList);
			}
				
		}
	}
	
	private boolean stillWaitingForFB(int id) {
		for (ArrayList<Request> rqList : _rq2DList) {
			for (Request rq : rqList) {
				if (rq.feedbackID == id) 
					return true;
				continue;
			}
		}
		return false;
	}
}
