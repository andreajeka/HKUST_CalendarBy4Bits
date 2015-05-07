package hkust.cse.calendar.gui;

import hkust.cse.calendar.users.User;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;


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




import java.awt.Color;

import java.awt.Window;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;

import javax.swing.JDialog;

import javax.swing.border.TitledBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class SignUpDialog extends JDialog implements ActionListener{

	private ApptStorageControllerImpl controller;
	
	private JLabel nameL;
	private JTextField nameInput;

	private JLabel passwordL;
	private JPasswordField passwordInput;


	private JLabel msgL;

	private JButton saveButton;
	private JButton cancelButton;
	
	private JPanel inputPanel;
	private JPanel detailsPanel;
	private JPanel butPanel;
	
	private JPanel usernamePanel;
	private JPanel passwordPanel;
	
	
	private JPanel namePanel;
	private JPanel emailPanel;
	
	private JLabel lastNameL;
	private JLabel firstNameL;
	private JLabel emailL;
	
	private JTextField lastNameInput;
	private JTextField firstNameInput;
	private JTextField emailInput;
	


	public JRadioButton adminButton;
	public JRadioButton userButton;
	private ButtonGroup userbg;

	
	public SignUpDialog(ApptStorageControllerImpl con){

		adminButton = new JRadioButton("Admin");
		userButton = new JRadioButton("Regular user");

		userbg = new ButtonGroup();

		adminButton.setActionCommand("Admin");
		userButton.setActionCommand("Regular User");

		userbg.add(adminButton);
		userbg.add(userButton);

		JPanel userTypePanel = new JPanel();
		userTypePanel.add(new JLabel("User Type:"));
		userTypePanel.add(adminButton);
		userTypePanel.add(userButton);

		userButton.setSelected(true);
		adminButton.addActionListener(this);
		userButton.addActionListener(this);



		
		controller = con;
		
		setTitle("Sign Up Dialog");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		Container contentPane;
		contentPane = getContentPane();
		
		msgL = new JLabel("Please input your user name and password to sign up");
		nameL = new JLabel("Username: ");
		passwordL = new JLabel("Password: ");
		

		nameInput = new JTextField(20);
		nameInput.setText("UserName");
		passwordInput = new JPasswordField(20);
		passwordInput.setText("123456");


		// input
		inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Security");
		inputPanel.setBorder(titledBorder1);

		inputPanel.add(msgL);
		// username input
		usernamePanel = new JPanel();
		usernamePanel.add(nameL);
		usernamePanel.add(nameInput);

		inputPanel.add(usernamePanel);
		
		passwordPanel = new JPanel();
		passwordPanel.add(passwordL);
		passwordPanel.add(passwordInput);

		inputPanel.add(passwordPanel);

		

		// personal information
		detailsPanel = new JPanel();
		detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
		TitledBorder titledBorder2 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Personal Information");
		detailsPanel.setBorder(titledBorder2);
		
		// Last Name
		namePanel = new JPanel();
		lastNameL = new JLabel("Last Name: ");
		namePanel.add(lastNameL);
		
		lastNameInput = new JTextField(10);
		lastNameInput.setText("LastName");
		namePanel.add(lastNameInput);
		
		firstNameL = new JLabel("First Name: ");
		namePanel.add(firstNameL);

		firstNameInput = new JTextField(15);
		firstNameInput.setText("FirstName");
		namePanel.add(firstNameInput);

		detailsPanel.add(namePanel);
		
		emailPanel = new JPanel();
		emailL = new JLabel("Email address: ");
		emailInput = new JTextField(30);
		emailInput.setText("email@email.com");
		
		emailPanel.add(emailL);
		emailPanel.add(emailInput);

		detailsPanel.add(emailPanel);
		detailsPanel.add(userTypePanel);
		
		// button Panel
		butPanel = new JPanel();
		
		saveButton = new JButton("Save");
		saveButton.addActionListener(this);

		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);

		butPanel.add(saveButton);
		butPanel.add(cancelButton);

		contentPane.add("North", inputPanel);
		contentPane.add("Center", detailsPanel);
		contentPane.add("South", butPanel);
		
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		if(e.getSource() == saveButton){
			if(nameInput.getText().isEmpty() || passwordInput.getText().isEmpty()){
				JOptionPane.showMessageDialog(this, "Please input your username and password!", "Input Error",
						JOptionPane.ERROR_MESSAGE);
				return; //this part ok!!!!
			}
			
			//
//			else if(!passwordInput.getText().equals(repasswordInput.getText())){
//				JOptionPane.showMessageDialog(this, "Password inputs do not match!", "Input Error",
//						JOptionPane.ERROR_MESSAGE);
//			}

			else if(lastNameInput.getText().isEmpty() || firstNameInput.getText().isEmpty()){
				JOptionPane.showMessageDialog(this,	"Please input first name and last name!", "Input Error",
						JOptionPane.ERROR_MESSAGE);
					return;
			}

			else if(emailInput.getText().isEmpty()){
				JOptionPane.showMessageDialog(this,	"Please input your email!", "Input Error",
					JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			else if(!emailInput.getText().contains("@")){
				JOptionPane.showMessageDialog(this,	"Please input correct email!", "Input Error",
						JOptionPane.ERROR_MESSAGE);
					return;
			}
			 else {
				User user = new User(nameInput.getText().trim(),passwordInput.getText().trim());
				
				if(adminButton.isSelected()){
					user.setAdmin(true);
				}
				else
				{
					user.setAdmin(false);
				}
				
				controller.addUser(user);
				
				JOptionPane.showMessageDialog(this, "Sign Up successful", "Registered!", JOptionPane.INFORMATION_MESSAGE);
			}



			}
			//start from here
			//
		

		
		if(e.getSource() == cancelButton){
			this.setVisible(false);
			dispose();
		}
	}

}
