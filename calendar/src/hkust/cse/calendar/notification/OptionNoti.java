package hkust.cse.calendar.notification;

import javax.swing.JOptionPane;

public class OptionNoti extends Notification {

	public OptionNoti(String title, String msg) {
		super(title, msg);
	}

	@Override
	public boolean popUp() {
		return (JOptionPane.showConfirmDialog(_panel, _message, _title, JOptionPane.YES_NO_OPTION) == 1);
	}

}
