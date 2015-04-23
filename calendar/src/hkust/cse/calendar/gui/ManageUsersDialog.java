package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.users.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class ManageUsersDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private ApptStorageControllerImpl controller;
	private User currentUser;
	private DefaultListModel<String> userListModel;
	private JList<String> userJList;
	private JScrollPane scroll;
	private JPanel buttonPane = new JPanel();		
	private JButton removeButton = new JButton("Remove");
	private JButton modifyButton = new JButton("Modify");
	
	public ManageUsersDialog(ApptStorageControllerImpl controller, User user) {
		
		this.controller = controller;
		currentUser = user;
		this.setTitle("Manage Users");
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 300);

		userListModel = new DefaultListModel<String>();
		userJList = new JList<String>(userListModel);
		userJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userJList.setLayoutOrientation(JList.VERTICAL);
		userJList.setVisibleRowCount(5);
		userJList.setFont(new Font("", Font.BOLD, 18));

		scroll = new JScrollPane(userJList);
		this.add(scroll, BorderLayout.CENTER);

		buttonPane.add(modifyButton);
		buttonPane.add(removeButton);
		this.add(buttonPane, BorderLayout.PAGE_END);
		
		// Response to Modify button being clicked
		modifyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = userJList.getSelectedIndex();
				if (selectedIndex != -1) {
					User user = null;
					String username = userJList.getSelectedValue();
					
					if (controller.searchUser(username) != null) // Not necessary to check, but safety comes first
						user = controller.searchUser(username);
					
					if (user != null) {
						PersonalSettingDialog psd = new PersonalSettingDialog(controller, user);
						psd.addWindowListener(new WindowListener() {
							
							@Override
							public void windowClosed(WindowEvent arg0) {
								// Load user list when window is closed
								loadUserList();
							}
							
							/** ALL UNUSED METHODS **/
							@Override
							public void windowActivated(WindowEvent arg0) {
							}

							@Override
							public void windowClosing(WindowEvent arg0) {
							}

							@Override
							public void windowDeactivated(WindowEvent arg0) {
							}

							@Override
							public void windowDeiconified(WindowEvent arg0) {	
							}

							@Override
							public void windowIconified(WindowEvent arg0) {
							}

							@Override
							public void windowOpened(WindowEvent arg0) {
							}
							/*******************************************/
							
						});
						psd.setLocationRelativeTo(null);
						psd.show();
						
					}
					
			
					
					
				} else
					JOptionPane.showMessageDialog(null, "Please Select a User to be Modified", "MODIFY ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		});
		
		removeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = userJList.getSelectedIndex();
				if (selectedIndex != -1) {
					// User to be removed
					User user = null;
					String username = userJList.getSelectedValue();
					if (controller.searchUser(username) != null) // Not necessary to check, but safety comes first
						user = controller.searchUser(username);
					
					if (user != null) { // Not necessary to check, but safety comes first
						user.setTobeRemoved(true);
					}
					loadUserList();
				}
				else
					JOptionPane.showMessageDialog(null, "Please Select a User to be Removed", "REMOVE ERROR", JOptionPane.ERROR_MESSAGE);
			}
			
		});

		loadUserList();

	}
	
	private void loadUserList() {
		if (!controller.getUserList().isEmpty()) {
			ArrayList<User> userList = controller.getUserList();
			userListModel.removeAllElements();
			for (User user : userList) {
				// Don't show yourself in the list. Just display other users
				if (!user.equals(currentUser)) {
					String username = user.getUsername();
					if (user.isTobeRemoved())
						continue;
					else userListModel.addElement(username);
				}
			}
			
		}
	}
}
