package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Request;
import hkust.cse.calendar.users.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ManageUsersDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private ApptStorageControllerImpl controller;
	private User currentUser;
	private DefaultListModel<String> userListModel;
	private JList<String> userJList;
	private JScrollPane scroll;
	private JPopupMenu pop;
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

		// Create a pop up menu when user right clicks
		pop = new JPopupMenu();
		Font f1 = new Font("Helvetica", Font.ITALIC, 11);
		pop.setFont(f1);
		
		JMenuItem mi;
		mi = (JMenuItem) pop.add(new JMenuItem("Details"));
		
		// Listen to any mouse events on jList
		userJList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				// If event is right click
				if (SwingUtilities.isRightMouseButton(me)) {
					// Get the index where the click happens in the area
					int index = userJList.locationToIndex(me.getPoint());
					// If the list is selected and the bounding rectangle 
					// in the list's coordinate system doesn't contain the index, ignore
					if (index != -1 && !userJList.getCellBounds(index, index).contains(me.getPoint()))
						return;
					else {	
						// Else set the selected index 
						userJList.setSelectedIndex(index);
						// Trigger the pop up
						pop.show(userJList, me.getX(), me.getY());
					}
				}
			}
		});

		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				User user = null;
				String userType = "";
				String FirstName = "";
				String LastName = "";
				String Address = "";
				String Email = "";
				String Phone = "";
				
				String username = userJList.getSelectedValue();
				if (controller.searchUser(username) != null) // Not necessary to check, but safety comes first
					user = controller.searchUser(username);
				
				if (user.isAdmin()) userType = "Admin";
				else userType = "Regular"; 
				
				if (user.getFirstName() != null) FirstName = user.getFirstName();
				if (user.getLastName() != null) LastName = user.getLastName();
				if (user.getAddress() != null) Address = user.getAddress();
				if (user.getEmail() != null) Email = user.getEmail();
				if (user.getPhone() != null) Phone = user.getPhone();
				
				String msg = "User Name" + "\t=  " + user.getUsername() + "\n" +
							 "User Type" + "\t=  " + userType + "\n" +
							 "First Name" + "\t=  " + FirstName + "\n" +
							 "Last Name" + "\t=  " + LastName + "\n" +
							 "Address" + "\t=  " + Address + "\n" +
							 "Email" + "\t=  " + Email + "\n" +
							 "Phone" + "\t=  " + Phone;
				DetailsDialog info = new DetailsDialog(msg, "User Details");
				info.setVisible(true);
			}
		});
		
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
						controller.addRequest(new Request(currentUser, null, Request.type.DELETE_USER, user));// Testing
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
