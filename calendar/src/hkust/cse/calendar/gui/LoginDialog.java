package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.apptstorage.ApptStorageNullImpl;
import hkust.cse.calendar.unit.RequestChecker;
import hkust.cse.calendar.users.User;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/** NOTE FOR LOGIN DIALOG **/
/*
 * Currently for this version, I only hardcode id: user and pw: user as the user admin
 * When you sign up as a new user, you won't be able to access manage users and manage locations.
 */
public class LoginDialog extends JFrame implements ActionListener
{
	private JTextField userName;
	private JPasswordField password;
	private JButton loginButton;
	private JButton closeButton;
	private JButton signupButton;

	public JRadioButton adminButton;
	public JRadioButton userButton;
	private ButtonGroup userbg;


	private ApptStorageControllerImpl controller;
	private boolean textFieldEmpty;

	public LoginDialog()		// Create a dialog to log in
	{
		setTitle("Log in");
		

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		controller = new ApptStorageControllerImpl(new ApptStorageNullImpl());
		//load userList from xml
		controller.LoadUserFromXml();
		controller.LoadRequestFromXml();
		controller.LoadApptFromXml();
		controller.LoadLocFromXml();
		controller.LoadFeedbacksFromXml();
		controller.LoadFeedbackCountFromXml();
		
		if(controller.searchUser("user") == null){
			User user = new User("user", "user");
			user.setAdmin(true);
			controller.addUser(user);
		}

		Container contentPane;
		contentPane = getContentPane();

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

		JPanel messPanel = new JPanel();
		messPanel.add(new JLabel("Please input your user name and password to log in."));
		top.add(messPanel);

		JPanel namePanel = new JPanel();
		namePanel.add(new JLabel("User Name:"));
		userName = new JTextField(15);
		
		//adding default admin userName, only for testing
		userName.setText("");
		
		namePanel.add(userName);
		top.add(namePanel);

		JPanel pwPanel = new JPanel();
		pwPanel.add(new JLabel("Password:  "));
		password = new JPasswordField(15);
		
		//adding default admin password, only for testing
		password.setText("");
		
		pwPanel.add(password);
		top.add(pwPanel);
		
		JPanel signupPanel = new JPanel();
		signupPanel.add(new JLabel("If you don't have an account, please:"));
		signupButton = new JButton("Sign up now");
		signupButton.addActionListener(this);
		signupPanel.add(signupButton);
		top.add(signupPanel);

		contentPane.add("North", top);

		JPanel butPanel = new JPanel();
		butPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

		loginButton = new JButton("Log in");
		loginButton.addActionListener(this);
		loginButton.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					// Trim to remove whitespaces
					String username = userName.getText().trim();
					String pw = new String(password.getPassword()).trim();

					areFieldsEmpty(username, pw);
					if (textFieldEmpty) {
						JOptionPane.showMessageDialog(LoginDialog.this, "Empty Username and/or Password", "Input Error", JOptionPane.WARNING_MESSAGE);
					} else if (controller.searchUser(username) != null) {
						User user = controller.searchUser(username);
						if (user.getPassword().equals(pw)) {
							controller.setCurrentUser(user);
							// Check if user is to be removed. 
							if (RequestChecker.getInstance().Check(controller, user))
							{
								CalGrid grid = new CalGrid(controller);
								setVisible(false);
							}
							else controller.SaveRequestToXml();

						} else {
							JOptionPane.showMessageDialog(LoginDialog.this, "Invalid Password", "Input Error", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(LoginDialog.this, "Invalid Username", "Input Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				
			}
			
		});
		butPanel.add(loginButton);

		this.getRootPane().setDefaultButton(loginButton);
		
		closeButton = new JButton("Close program");
		closeButton.addActionListener(this);
		butPanel.add(closeButton);
		
		contentPane.add("South", butPanel);

		pack();
		setLocationRelativeTo(null);
		setVisible(true);	

	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == loginButton)
		{
			// Trim to remove whitespaces
			String username = userName.getText().trim();
			String pw = new String(password.getPassword()).trim();

			areFieldsEmpty(username, pw);
			if (textFieldEmpty) {
				JOptionPane.showMessageDialog(this, "Empty Username and/or Password", "Input Error", JOptionPane.WARNING_MESSAGE);
			} else if (controller.searchUser(username) != null) {
				User user = controller.searchUser(username);
				if (user.getPassword().equals(pw)) {
					controller.setCurrentUser(user);
					// Check if user is to be removed. 
					if (RequestChecker.getInstance().Check(controller, user))
					{
						CalGrid grid = new CalGrid(controller);
						setVisible(false);
					}

				} else {
					JOptionPane.showMessageDialog(this, "Invalid Password", "Input Error", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Invalid Username", "Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource() == signupButton)
		{
			new SignUpDialog(controller);
			// Trim to remove whitespaces
//			String username = userName.getText().trim();
//			String pw = new String(password.getPassword()).trim();
//
//			areFieldsEmpty(username, pw);
//			if (textFieldEmpty) {
//				JOptionPane.showMessageDialog(this, "Empty Username and/or Password", "Input Error", JOptionPane.WARNING_MESSAGE);
//			} else if (controller.searchUser(username) != null) {
//				JOptionPane.showMessageDialog(this, "Username already exists", "Username not available", JOptionPane.WARNING_MESSAGE);
//			} else {
//				User user = new User(username,pw);
//				
//				if(adminButton.isSelected()){
//					user.setAdmin(true);
//				}
//				else
//				{
//					user.setAdmin(false);
//				}
//				
//				controller.addUser(user);
//				
//				JOptionPane.showMessageDialog(this, "Sign up successful", "Registered!", JOptionPane.INFORMATION_MESSAGE);
//			}
			
		}
		else if(e.getSource() == closeButton)
		{
			int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
					"Confirm", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				controller.SaveUserToXml();
				System.exit(0);			
			}
		}
	
	}
	
	// This method checks whether a string is a valid user name or password, as they can contains only letters and numbers
	public static boolean ValidString(String s)
	{
		char[] sChar = s.toCharArray();
		for(int i = 0; i < sChar.length; i++)
		{
			int sInt = (int)sChar[i];
			if(sInt < 48 || sInt > 122)
				return false;
			if(sInt > 57 && sInt < 65)
				return false;
			if(sInt > 90 && sInt < 97)
				return false;
		}
		return true;
	}

	public void areFieldsEmpty(String username, String password) {
		if (username.isEmpty() || password.isEmpty()) 
			textFieldEmpty = true;
		else 
			textFieldEmpty = false;

	}
}
