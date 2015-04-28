package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class CreateGroupEvent extends JFrame{
	
	/* Basic attributes for the class */
	private CalGrid parent;
	Container pane;
	private JPanel createGroupPane;
	private JPanel browsePane;
	private JPanel navPane;
	
	/* Attributes needed for the first page of browsePane in the dialog*/
	private DefaultListModel<String> leftListModel;
	private JList<String> leftList;
	private JScrollPane leftListPane;
	private JLabel toLeft;
	private JPanel arrows;
	private JLabel toRight;
	private DefaultListModel<String> rightListModel;
	private JList<String> rightList;
	private JScrollPane rightListPane;
	
	/* Attributes needed for the second page of browsePane in the dialog*/
	private JPanel dateOption;
	private JPanel addBtnPanel;
	private JPanel instructionPanel1;
	boolean firstTimeFor2 = true;
	
	// Constant array for combo box month & day
	private final int[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, 28, 29 ,30, 31};
	private final String[] months = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	private DefaultListModel<String> dateListModel;
	private JList<String> dateList;
	private JScrollPane dateListPane;
	
	/* Attributes needed for the third page of browsePane in the dialog*/
	private DefaultListModel<String> timeListModel;
	private JList<String> timeList;
	private JScrollPane timeListPane;
	private JPanel instructionPanel2; 
	boolean firstTimeFor3 = true;
	
	/* Attributes needed to store the selections from the panes */
	private ArrayList<String> usernameChosenList;
	private ArrayList<TimeSpan> dateInTheList;
	private ArrayList<TimeSpan> dateChosenList;
	private ArrayList<TimeSpan> timeInTheList;
	private ArrayList<TimeSpan> timeChosenList;
	
	
	/* Attributes needed for the navigation buttons in navpane*/
	private JButton next1Btn;
	private JButton back1Btn;
	private JButton next2Btn;
	private JButton back2Btn;
	private JButton confirmBtn;
	
	public CreateGroupEvent(CalGrid cal, ArrayList<String> usernameChosenList, 
			ArrayList<TimeSpan> dateChosenList, ArrayList<TimeSpan> timeChosenList) {
		parent = cal;
		this.usernameChosenList = usernameChosenList;
		this.dateChosenList = dateChosenList;
		this.timeChosenList = timeChosenList;
		dateInTheList = new ArrayList<TimeSpan>();
		timeInTheList = new ArrayList<TimeSpan>();
		setSize(new Dimension(500,500));
		pane = this.getContentPane();
		setTitle("Manual Group Event Scheduling");
		
		// Load the first page
		loadUserPage();
		
		setVisible(true);
	}
	
	/* Method to load the first page of the dialog */
	private void loadUserPage() {
		
		/* Foundation panels */
		/*-------------------*/
		createGroupPane = new JPanel();
		createGroupPane.setLayout(new BorderLayout());
		
		browsePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);
		
		navPane = new JPanel();
		
		
		
		/* Browse pane contents */
		/*----------------------*/
		
		/* Create the left pane for initiator to choose users */
		leftListModel = new DefaultListModel<String>();
		leftList = new JList<String>(leftListModel);
		leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leftList.setLayoutOrientation(JList.VERTICAL);
		leftList.setVisibleRowCount(8);
		leftList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = leftList.locationToIndex(e.getPoint());
					if (index == -1) return;
					rightListModel.addElement(leftListModel.getElementAt(index));
					leftListModel.remove(index);
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
			}
			
		});
		
		leftListPane = new JScrollPane(leftList);
		leftListPane.setPreferredSize(new Dimension(210,380));
		browsePane.add(leftListPane);
		loadUserList();
		
		/* Create the labels for arrow (just some decoration */
		arrows = new JPanel();
		arrows.setLayout(new GridLayout(2,0));
		
		toRight = new JLabel(" > ");
		arrows.add(toRight);
		
		toLeft = new JLabel(" < ");
		arrows.add(toLeft);
		arrows.setPreferredSize(new Dimension(20,380));
		
		browsePane.add(arrows);
		
		/* Create the right pane for initiator to put their selection of users*/
		rightListModel = new DefaultListModel<String>();
		rightList = new JList<String>(rightListModel);
		rightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rightList.setLayoutOrientation(JList.VERTICAL);
		rightList.setVisibleRowCount(8);
		rightList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = rightList.locationToIndex(e.getPoint());
					if (index == -1) return;
					leftListModel.addElement(rightListModel.getElementAt(index));
					rightListModel.remove(index);
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {	
			}

			@Override
			public void mouseExited(MouseEvent e) {	
			}

			@Override
			public void mousePressed(MouseEvent e) {	
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
		});
		
		rightListPane = new JScrollPane(rightList);
		rightListPane.setPreferredSize(new Dimension(210,380));
		browsePane.add(rightListPane);
	
		
		/* Create the contents of navigation pane */
		/*----------------------------------------*/
		
		next1Btn = new JButton("Next >");
		next1Btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rightListModel.isEmpty()) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this, "You have to choose at least one person", 
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				usernameChosenList.clear();
				for(int i = 0; i < rightListModel.getSize(); i++) 
					usernameChosenList.add((String) rightListModel.getElementAt(i));
				
				prepareForPageTwo();
				if (firstTimeFor2) {
					loadSelectDate();
					firstTimeFor2 = false;
				} else {
					dateOption.setVisible(true);
					addBtnPanel.setVisible(true);
					dateListPane.setVisible(true);
					instructionPanel1.setVisible(true);
					next2Btn.setVisible(true);
					back1Btn.setVisible(true);
				}
			}
		});
		
		navPane.add(next1Btn);
		
		/* Add all contents into the group pane*/
		/* Add group pane into the frame's pane */
		createGroupPane.add(navPane, BorderLayout.SOUTH);
		createGroupPane.add(browsePane, BorderLayout.CENTER);
		createGroupPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		pane.add(createGroupPane);
	}

	/* All steps needed to specify page two */
	private void prepareForPageTwo() {
		arrows.setVisible(false);
		rightListPane.setVisible(false);
		leftListPane.setVisible(false);
		next1Btn.setVisible(false);
	    
		TitledBorder titledBorder2 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Choose your desired days:");
		browsePane.setBorder(titledBorder2);
		browsePane.setLayout(new BoxLayout(browsePane,BoxLayout.Y_AXIS));
	}
	
	/* Method to load the second page of the dialog */
	private void loadSelectDate() {
		
		/* Browse pane contents */
		/*----------------------*/
		
		/* Create a panel that contains options to choose date, month, year */
		dateOption = new JPanel(new FlowLayout(FlowLayout.CENTER));
	
		// Year
		JLabel yearL = new JLabel("YEAR: ");
		dateOption.add(yearL);
		JTextField yearF = new JTextField(6);
		dateOption.add(yearF);

		// Month
		JLabel monthL = new JLabel("Month: ");
		dateOption.add(monthL);
		JComboBox<String> monthB = new JComboBox<String>();
		//monthB.addActionListener(this);
		monthB.setPreferredSize(new Dimension(100, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			monthB.addItem(months[cnt]);
		dateOption.add(monthB);

		// Day
		JLabel dateL = new JLabel("DATE: ");
		dateOption.add(dateL);
		JComboBox<Integer> dateB = new JComboBox<Integer>();
		//dateB.addActionListener(this);
		dateB.setPreferredSize(new Dimension(50, 30));
		for (int cnt = 0; cnt < 31; cnt++)
			dateB.addItem(days[cnt]);
		dateOption.add(dateB);
		browsePane.add(dateOption);
		
		
		/* Create a panel to contain the button to add the selected date */
		addBtnPanel = new JPanel();
		JButton addBtn = new JButton("Add");
		addBtnPanel.add(addBtn);
		browsePane.add(addBtnPanel);
		
		addBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				/* ---------------------------- Do boundary check -------------------------------- */ 
				/*---------------------------------------------------------------------------------*/
				int year = Utility.getNumber(yearF.getText());
				if (year < 1980 || year > 2100) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this, "Please input proper year",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int monthSelected = monthB.getSelectedIndex() + 1;
				int monthIndex = Utility.getNumber("" + monthSelected);
				int dateSelected = dateB.getSelectedIndex() + 1;
				int date = Utility.getNumber("" + dateSelected);
				int monthDay = CalGrid.monthDays[monthIndex - 1];
				if (monthIndex == 2) {
					GregorianCalendar c = new GregorianCalendar();
					if (c.isLeapYear(year))
						monthDay = 29;
				}
				if (date <= 0 || date > monthDay) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this,
					"Please select proper month day", "Input Error",
					JOptionPane.WARNING_MESSAGE);
					return;
				}
				/*------------------------------------------------------------------------*/
				/* Now process the selection into string and add it to the combo box GUI */
				String month = CalGrid.months[monthIndex-1];
				String element = month + " " + date + " " + year;
				if (dateListModel.contains(element)) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this, "Duplicate date is not allowed", "Duplicate!", 
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				dateListModel.addElement(element);
				
				/* Process the selection into a timespan on that day from 8 a.m to 6 p.m and add it to the arraylist*/
				/* 8 a.m to 6 p.m is specified by considering our apptlist range */
				Timestamp start = Timestamp.valueOf(year + "-" + monthIndex + "-" + date + " 08:00:00");
				Timestamp end= Timestamp.valueOf(year + "-" + monthIndex + "-" + date + " 18:00:00");
				TimeSpan ts = new TimeSpan(start, end);
				dateInTheList.add(ts);
				//System.out.println("After adding the date...." + dateInTheList);
			}
			
		});
		
		/* Create a list to show the specified date */
		/* This is also used to allow users choose multiple selection from their specified choice*/
		dateListModel = new DefaultListModel<String>();
		dateList = new JList<String>(dateListModel);
		dateList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		dateList.setLayoutOrientation(JList.VERTICAL);
		dateList.setVisibleRowCount(8);
		dateListPane = new JScrollPane(dateList);
		dateListPane.setPreferredSize(new Dimension(300,380));
		dateList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = dateList.locationToIndex(e.getPoint());
					if (index == -1) return;
					dateListModel.remove(index);
					dateInTheList.remove(index);
					//System.out.println("After double clicking...." + dateInTheList);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {	
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {	
			}
			
		});
		
		browsePane.add(dateListPane);
		
		/* Create a panel to store instruction on how to do multiple selection */
		instructionPanel1 = new JPanel();
		JLabel ctrlClickInstr = new JLabel("To allow multiple selection, hold down the Ctrl key and drag the mouse pointer");
		instructionPanel1.add(ctrlClickInstr);
		browsePane.add(instructionPanel1);
		
		/* Create a back button. '<' indicates its the first back button */
		back1Btn = new JButton("< Back");
		back1Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dateOption.setVisible(false);
				addBtnPanel.setVisible(false);
				dateListPane.setVisible(false);
				instructionPanel1.setVisible(false);
				back1Btn.setVisible(false);
				next2Btn.setVisible(false);
				
				browsePane.setLayout(new FlowLayout(FlowLayout.CENTER));
				TitledBorder titledBorder1 = new TitledBorder(BorderFactory
						.createEtchedBorder(Color.white, new Color(178, 178, 178)),
						"Add Participant:");
				browsePane.setBorder(titledBorder1);
				
				leftListPane.setVisible(true);
				arrows.setVisible(true);
				rightListPane.setVisible(true);	
				next1Btn.setVisible(true);
			}
			
		});
		
		/* Create the second next button */
		next2Btn = new JButton("Next >>");
		next2Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {		
				int[] selectedIndices = dateList.getSelectedIndices();
				if (selectedIndices.length == 0) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this, "Please select at least one from the date list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				dateChosenList.clear();
				for (int i = 0; i < selectedIndices.length; i++) {
					dateChosenList.add(dateInTheList.get(selectedIndices[i]));
				}
				//System.out.println("Clicking next >> ...." + dateChosenList);
				
				prepareForPageThree();
				if (firstTimeFor3) {
					loadTimeSlotPage();
					firstTimeFor3 = false;
				} else {
					instructionPanel2.setVisible(true);
					timeListPane.setVisible(true);
					back2Btn.setVisible(true);
					confirmBtn.setVisible(true);
					loadTimeSlots();
				}
			}
			
		});
		
		/* Add all buttons into the navigation pane */
		navPane.add(back1Btn);
		navPane.add(next2Btn);
	}
	
	/* All steps needed to display page three */
	private void prepareForPageThree() {
		dateOption.setVisible(false);
		addBtnPanel.setVisible(false);
		dateListPane.setVisible(false);
		instructionPanel1.setVisible(false);
		next2Btn.setVisible(false);
		back1Btn.setVisible(false);
		
		browsePane.setLayout(new BoxLayout(browsePane, BoxLayout.Y_AXIS));
		TitledBorder titledBorder3 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Choose Time Slot:");
		browsePane.setBorder(titledBorder3);
	}
	
	/* Method to load page three of the dialog */
	private void loadTimeSlotPage() {
		
		/* Browse pane contents */
		/*----------------------*/
		
		/* Create a panel to store the instruction */
		instructionPanel2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel rangeTime = new JLabel("Choose timeslot(s) from the available timeslots below. Can be multiple.");
		instructionPanel2.add(rangeTime);
		browsePane.add(instructionPanel2);
		
		/* Create a list to store the available time slots */
		timeListModel = new DefaultListModel<String>();
		timeList = new JList<String>(timeListModel);
		timeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		timeList.setLayoutOrientation(JList.VERTICAL);
		timeList.setVisibleRowCount(8);
		timeListPane = new JScrollPane(timeList);
		timeListPane.setPreferredSize(new Dimension(300,380));
	    loadTimeSlots();
		browsePane.add(timeListPane);
		
		/* Create a back button in the navigation pane. '<<' indicates back button 2 */
		back2Btn = new JButton("<< Back");
		back2Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				instructionPanel2.setVisible(false);
				timeListPane.setVisible(false);
				back2Btn.setVisible(false);
				confirmBtn.setVisible(false);
				timeListModel.removeAllElements();
				timeList.removeAll();
				timeInTheList.clear();
				
				dateOption.setVisible(true);
				addBtnPanel.setVisible(true);
				dateListPane.setVisible(true);
				instructionPanel1.setVisible(true);
				next2Btn.setVisible(true);
				back1Btn.setVisible(true);
				
				TitledBorder titledBorder2 = new TitledBorder(BorderFactory
						.createEtchedBorder(Color.white, new Color(178, 178, 178)),
						"Choose your desired days:");
				browsePane.setBorder(titledBorder2);
				browsePane.setLayout(new BoxLayout(browsePane,BoxLayout.Y_AXIS));
				
			}
			
		});
		
		/* Create a confirm button to finalize all the selection */
		confirmBtn = new JButton("Confirm");
		confirmBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int[] selectedIndices = timeList.getSelectedIndices();
				if (selectedIndices.length == 0) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this, "Please select at least one from the time list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				int result = JOptionPane.showConfirmDialog(CreateGroupEvent.this, "Confirm the following event?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION){
					//TODO: DO STH
					for (int i = 0; i < selectedIndices.length; i++) {
						timeChosenList.add(timeInTheList.get(selectedIndices[i]));
					}
					closeWindow();
				} else {
					return;
				}
				
				
			}
			
		});
		
		navPane.add(back2Btn);
		navPane.add(confirmBtn);
		
	}

	private void loadUserList() {
		if (!parent.controller.getUserList().isEmpty()) {
			ArrayList<User> userList = parent.controller.getUserList();
			leftListModel.removeAllElements();
			for (User user : userList) {
				// Don't show yourself in the list. Just display other users
				if (!user.equals(parent.mCurrUser)) {
					String username = user.getUsername();
					if (user.isTobeRemoved())
						continue;
					else {
						leftListModel.addElement(username);
					}
				}
			}
			
		}
	}
	
	private void loadTimeSlots() {
		ArrayList<User> userChosenList  = new ArrayList<User>();
		for (String name : usernameChosenList) {
			userChosenList.add(parent.controller.searchUser(name));
		}
		
		// Add the current user as well because we also need to get available timeslots based on the current user
		userChosenList.add(parent.controller.getCurrentUser());
		for (int i = 0; i < dateChosenList.size(); i++) {
			ArrayList<TimeSpan> timeSlots = parent.controller.RetrieveAvailTimeSpans(userChosenList, dateChosenList.get(i));
			if (!timeSlots.isEmpty()) {
				for (int j = 0; j < timeSlots.size(); j++) {
					// Display date
					int date = dateChosenList.get(i).StartTime().getDate();
					int year = dateChosenList.get(i).StartTime().getYear() + 1900;
					int Sminutes = timeSlots.get(j).StartTime().getMinutes();
					int Eminutes = timeSlots.get(j).EndTime().getMinutes();
					String Smins = Sminutes + "";
					String Emins = Eminutes + "";
					if (Sminutes < 10) 
						Smins = "0" + Sminutes;
					
					if (Eminutes < 10) 
						Emins = "0" + Eminutes;
					
					timeListModel.addElement( date + " " + CalGrid.months[dateChosenList.get(i).StartTime().getMonth()] + " " + year  
							+ "  " +
							// Display time
							timeSlots.get(j).StartTime().getHours() + ":" + Smins + " to " + 
							timeSlots.get(j).EndTime().getHours() + ":" +  Emins);
					timeInTheList.add(timeSlots.get(j));
			}
		}
		}
	}
	
	private void closeWindow() {
		setVisible(false);
		dispose();
	}
}

