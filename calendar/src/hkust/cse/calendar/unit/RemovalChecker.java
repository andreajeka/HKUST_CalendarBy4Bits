package hkust.cse.calendar.unit;

import java.util.HashMap;

import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.users.User;

// Singleton object
public class RemovalChecker {
	private static RemovalChecker instance;
	private ApptStorageNullImpl _controller;
	private User _user;
	
	RemovalChecker(){};
	
	RemovalChecker GetInstance()
	{
		if(instance == null)
			instance = new RemovalChecker();
		return instance;
	}
	
	void Check()
	{
		CheckLocationRemoval();
		CheckUserRemoval();
	}
	
	void CheckLocationRemoval()
	{	
		// get initiator appt list from user
		// Appt[] appts = _user.getInitiatorAppts();
		// for loop: looping through the appt list
		//for(int i = 0; i < appts.getLength)
		{
			//if(appt.getLocation().getRemovalBool())
			{
				// use noti service
			}
		}
	};
	
	void CheckUserRemoval()
	{
		HashMap<Integer, Appt> appts = _controller.mAppts;
		for(Appt appt : appts.values())
		{
			
		}
	};
}
