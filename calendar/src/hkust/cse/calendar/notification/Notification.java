package hkust.cse.calendar.notification;

import javax.swing.JPanel;

public abstract class Notification {
	// GUI element
	String _title;
	String _message;
	JPanel _panel;
	
	// Constructor
	Notification(String title, String msg)
	{
		_message = msg;
		_title = title;
		_panel = new JPanel();
	};
	
	public abstract boolean popUp();
}
