package hkust.cse.calendar.unit;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.ArrayList;

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
		for(ArrayList<Request> rqList : _rq2DList)
		{
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
							cleanRequests(rqList);
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
			OptionTimeSlot ots = new OptionTimeSlot("Request from initiator", "Please select your available timeslots with duration " 
													+ rq.duration, _controller, _user, rq.datesChosen, rq.duration);
			if (ots.isConfirm()) {
				TimeSlotFeedback feedback = new TimeSlotFeedback(rq._sender, rq._receiver, rq.feedbackID);
				feedback.setlistOfTimeSlots(ots.getUserFeedback());
				_controller.addFeedback(feedback);
				return notiReturnCode.NOTI_OK;
			}
		} else {
			// For timeslot invitation, if dateschosen list is not empty, we create new OptionTimeSlot
			if(new OptionNoti("Request from initiator", "Join event: " + ((Appt) rq._obj).getTitle() + "?").popUp())
			{
				System.out.println("OKAY");
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
		if(rqList.get(0)._obj.equals(User.class))
			((User) rqList.get(0)._obj).setTobeRemoved(false);
		
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
							// After condensing, find the earliest time slot from all available time with corresponding duration
							ArrayList<TimeSpan> aTimeSpan = Utility.getEarliestTimeSlot(timeSlotList, rq.duration);
							
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
								_controller.ManageAppt(NewAppt, ApptStorageControllerImpl.NEW);
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
