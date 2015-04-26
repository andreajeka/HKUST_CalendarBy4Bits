package hkust.cse.calendar.notification;

import javax.swing.JOptionPane;

public class MessageNoti extends Notification {

	public MessageNoti(String title, String msg) {
		super(title, msg);
	}

	@Override
	public boolean popUp() {
		JOptionPane.showMessageDialog(_panel, _message, _title, JOptionPane.INFORMATION_MESSAGE);
		return true;
	}

}
