package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.users.User;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class PersonalSettingDialog extends JFrame {
	// Logic
	private ApptStorageControllerImpl _controller;
	private User _user;
	
	// GUI
	private JTextField usernameTF = new JTextField(10);
	private JLabel usernameL = new JLabel("Username: ");
	private JPasswordField passwordTF = new JPasswordField(10);
	private JLabel passwordL = new JLabel("Passsword: ");
	private JTextField lastnameTF = new JTextField(10);
	private JLabel lastnameL =  new JLabel("Last name: ");
	private JTextField firstnameTF = new JTextField(10);
	private JLabel firstnameL =  new JLabel("First name: ");
	private JPanel infoPanel = new JPanel();
	
	private JButton saveBut = new JButton("Save");
	private JButton cancelBut = new JButton("Cancel");
	private JPanel buttonPanel = new JPanel();
	
	// Default constructor
	PersonalSettingDialog(ApptStorageControllerImpl controller, User user)
	{
		_controller = controller;
		_user = user;
		
		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(1000, 100);
		
		// Default text
		usernameTF.setText(_user.getUsername());
		lastnameTF.setText(_user.getLastName());
		firstnameTF.setText(_user.getFirstName());
		
		// Set up layout
		infoPanel.add(usernameL);
		infoPanel.add(usernameTF);
		infoPanel.add(passwordL);
		infoPanel.add(passwordTF);
		infoPanel.add(lastnameL);
		infoPanel.add(lastnameTF);
		infoPanel.add(firstnameL);
		infoPanel.add(firstnameTF);
		this.add(infoPanel, BorderLayout.PAGE_START);
		buttonPanel.add(saveBut, BorderLayout.CENTER);
		buttonPanel.add(cancelBut, BorderLayout.EAST);
		this.add(buttonPanel, BorderLayout.PAGE_END);
		
		this.pack();
		
		// Save Button
		saveBut.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Since there can't be duplicate in usernames, we have to do checking
					String username = usernameTF.getText();
					if (controller.searchUser(username) == null) {
						_user.setUsername(usernameTF.getText());
					} else {
						if (!_user.getUsername().equals(username)) {
							JOptionPane.showMessageDialog(null, "Username already exists", "Username not available", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					_user.setFirstName(firstnameTF.getText());
					_user.setLastName(lastnameTF.getText());
					
					// Don't allow empty password field
					String pw = new String(passwordTF.getPassword()).trim();
					if (!pw.equals(""))
						_user.setPassword(pw);
					else {
						JOptionPane.showMessageDialog(null, "Password field cannot be empty", "Empty password", JOptionPane.WARNING_MESSAGE);
						return;
					}
						
					
					// Close
					setVisible(false);
					dispose();
				}
			}
		);
		
		// Cancel Button
		cancelBut.addActionListener
		(
			new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					setVisible(false);
					dispose();
				}
			}
		);
	}
	
}
