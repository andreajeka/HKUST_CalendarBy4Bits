package hkust.cse.calendar.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ManageUsersDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<String> userListModel;
	private JList<String> userList;
	private JScrollPane scroll;
	private JPanel buttonPane = new JPanel();		
	private JButton removeButton = new JButton("Remove");
	
	public ManageUsersDialog() {
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 200);

		userListModel = new DefaultListModel<String>();

		// Hardcode
		userListModel.addElement("user1");
		userListModel.addElement("user2");
		
		userList = new JList<String>(userListModel);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.setLayoutOrientation(JList.VERTICAL);
		userList.setVisibleRowCount(5);

		scroll = new JScrollPane(userList);
		this.add(scroll, BorderLayout.CENTER);

		buttonPane.add(removeButton, BorderLayout.EAST);
		this.add(buttonPane, BorderLayout.PAGE_END);
	}
}
