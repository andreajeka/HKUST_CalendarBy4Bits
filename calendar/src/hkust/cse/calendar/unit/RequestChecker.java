package hkust.cse.calendar.unit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.notification.MessageNoti;
import hkust.cse.calendar.notification.Notification;
import hkust.cse.calendar.notification.OptionNoti;
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
							return false;
						case NOTI_REFUSE:
							cleanRequests(rqList);
						case NOTI_OK:
							confirmRequests(rqList, rq);
					}
				}
			}
		}
		_controller.setRequestList(_rq2DList);
		return true;
	}
	
	notiReturnCode apptInvitation(Request rq)
	{
		if(new OptionNoti("Request from initiator", "Join event: " + ((Appt) rq._obj).getTitle() + "?").popUp())
		{
			return notiReturnCode.NOTI_OK;
		}
		else return notiReturnCode.NOTI_REFUSE;
	}
	
	notiReturnCode locationRemoval(Request rq)
	{		
		if(new OptionNoti("Request from admin", "Allow delete of location: " + ((Location) rq._obj).getName() + "?").popUp())
		{
			return notiReturnCode.NOTI_OK;
		}
		else return notiReturnCode.NOTI_REFUSE;
	};
	
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
			// TODO: use noti service to notice
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
					break;
				case INVITE:
					((Appt) rq._obj).moveFromWaitToAttend(rq._receiver.getUserId());
			}
				
		}
	}
}
