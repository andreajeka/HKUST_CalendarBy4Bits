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
import java.util.Arrays;
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

	public final static int MANUAL = 1;

	public final static int AUTOMATIC = 2;
	
	private boolean modify;
	
	/* Basic attributes for the class */
	private CalGrid parent;
	Container pane;
	private int action;
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
	private JPanel instructionOrDurationPanel;
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
	private JPanel instructionPanel; 
	boolean firstTimeFor3 = true;
	
	/* Attributes needed to store the selections from the panes */
	private ArrayList<String> usernameChosenList;
	private ArrayList<User> userChosenList;
	private ArrayList<TimeSpan> dateInTheList;
	private ArrayList<TimeSpan> dateChosenList;
	private ArrayList<TimeSpan> timeInTheList;
	private int duration;
	
	// The reason why I use array list instead of a mere TimeSpan class is because
	// I need to get the value as pass by reference. If you have better idea, you can change it
	private ArrayList<TimeSpan> timeSlotChosen;
	
	/* Attributes needed for the navigation buttons in navpane*/
	private JButton next1Btn;
	private JButton back1Btn;
	private JButton next2Btn;
	private JButton back2Btn;
	private JButton confirmBtn;
	
	public CreateGroupEvent(CalGrid cal,  int action, ArrayList<User> userChosenList, 
			ArrayList<TimeSpan> dateChosenList, ArrayList<TimeSpan> timeSlotChosen, int duration) {
		parent = cal;
		this.userChosenList = userChosenList;
		this.dateChosenList = dateChosenList;
		this.timeSlotChosen = timeSlotChosen;
		this.action = action;
		this.duration = duration;
		usernameChosenList = new ArrayList<String>();
		dateInTheList = new ArrayList<TimeSpan>();
		timeInTheList = new ArrayList<TimeSpan>();
		setSize(new Dimension(500,500));
		pane = this.getContentPane();
		setTitle("Manual Group Event Scheduling");
		modify = false;
		// Load the first page
		loadUserPage();
		
		setVisible(true);
	}
	
	public CreateGroupEvent(CalGrid cal,  int action, ArrayList<User> userChosenList, 
			ArrayList<TimeSpan> dateChosenList, ArrayList<TimeSpan> timeSlotChosen, boolean modify) {
		parent = cal;
		this.userChosenList = userChosenList;
		this.dateChosenList = dateChosenList;
		this.timeSlotChosen = timeSlotChosen;
		this.action = action;
		this.modify = modify;
		usernameChosenList = new ArrayList<String>();
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
		loadUserList();
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
					instructionOrDurationPanel.setVisible(true);
					if (action == MANUAL)
						next2Btn.setVisible(true);
					else
						confirmBtn.setVisible(true);
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
		monthB.setPreferredSize(new Dimension(100, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			monthB.addItem(months[cnt]);
		dateOption.add(monthB);

		// Day
		JLabel dateL = new JLabel("DATE: ");
		dateOption.add(dateL);
		JComboBox<Integer> dateB = new JComboBox<Integer>();
		dateB.setPreferredSize(new Dimension(50, 30));
		for (int cnt = 0; cnt < 31; cnt++)
			dateB.addItem(days[cnt]);
		dateOption.add(dateB);
		browsePane.add(dateOption);
		
		if (modify) {
			int year = dateChosenList.get(0).StartTime().getYear() + 1900;
			yearF.setText("" + year);
			int month = dateChosenList.get(0).StartTime().getMonth();
			monthB.setSelectedIndex(month);
			int date = dateChosenList.get(0).StartTime().getDate() - 1;
			dateB.setSelectedIndex(date);
		}
		
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
				String element = date + " " + month + " " + year;
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
		
		JTextField durationHTF = new JTextField(4);
		JTextField durationMTF = new JTextField(4);	
		
		/* Create a panel to store instruction on how to do multiple selection */
		instructionOrDurationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		if (action == MANUAL) {
			JLabel ctrlClickInstr = new JLabel("To allow multiple selection, hold down the Ctrl key and click with the mouse pointer");
			instructionOrDurationPanel.add(ctrlClickInstr);
		} else {
			JLabel desiredDuration = new JLabel("Your desired duration: ");
			JLabel durationH = new JLabel("Hour");
			JLabel durationM = new JLabel("Minute");
			
			instructionOrDurationPanel.add(desiredDuration);
			instructionOrDurationPanel.add(durationH);
			instructionOrDurationPanel.add(durationHTF);
			instructionOrDurationPanel.add(durationM);
			instructionOrDurationPanel.add(durationMTF);
		}

		browsePane.add(instructionOrDurationPanel);
		
		
		/* Create a back button. '<' indicates its the first back button */
		back1Btn = new JButton("< Back");
		back1Btn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dateOption.setVisible(false);
				addBtnPanel.setVisible(false);
				dateListPane.setVisible(false);
				instructionOrDurationPanel.setVisible(false);
				back1Btn.setVisible(false);
				if (action == MANUAL)
					next2Btn.setVisible(false);
				else 
					confirmBtn.setVisible(false);
				
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
		
		if (action == MANUAL) {
			/* Create the second next button */
			next2Btn = new JButton("Next >>");
			next2Btn.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {	
					// Collect the selected indices
					int[] selectedIndices = dateList.getSelectedIndices();
				
					// Warn user if at least one index is not selected
					if (selectedIndices.length == 0) {
						JOptionPane.showMessageDialog(CreateGroupEvent.this, "Please select at least one from the date list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
				
					// Clear the dateChosenList
					dateChosenList.clear();
				
					// Add the newest selection
					for (int i = 0; i < selectedIndices.length; i++) {
						dateChosenList.add(dateInTheList.get(selectedIndices[i]));
					}
					//System.out.println("Clicking next >> ...." + dateChosenList);
				
					prepareForPageThree();
					if (firstTimeFor3) {
						loadTimeSlotPage();
						firstTimeFor3 = false;
					} else {
						instructionPanel.setVisible(true);
						timeListPane.setVisible(true);
						back2Btn.setVisible(true);
						confirmBtn.setVisible(true);
						loadTimeSlots();
					}
				}
			
			});
			navPane.add(back1Btn);
			navPane.add(next2Btn);
		} else {
			confirmBtn = new JButton("Confirm");
			navPane.add(back1Btn);
			navPane.add(confirmBtn);
			confirmBtn.addActionListener(new ActionListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void actionPerformed(ActionEvent e) {
					
					int[] selectedIndices = dateList.getSelectedIndices();
					
					// Warn user if at least one index is not selected
					if (selectedIndices.length == 0) {
						JOptionPane.showMessageDialog(CreateGroupEvent.this, "Please select at least one from the date list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
					// Yes No Confirmation upon successful creation
					int result = JOptionPane.showConfirmDialog(CreateGroupEvent.this, "Confirm the following selection?",
							"Confirm", JOptionPane.YES_NO_OPTION);
					if (result == JOptionPane.YES_OPTION) {
						// Add the newest selection
						for (int i = 0; i < selectedIndices.length; i++) {
							dateChosenList.add(dateInTheList.get(selectedIndices[i]));
						}
						
						duration = Integer.parseInt(durationHTF.getText()) * 60 + Integer.parseInt(durationMTF.getText());
						
						closeWindow();
					} else {
						return;
					}
				}
				
			});
			
		}
	}
	
	/* All steps needed to display page three */
	private void prepareForPageThree() {
		dateOption.setVisible(false);
		addBtnPanel.setVisible(false);
		dateListPane.setVisible(false);
		instructionOrDurationPanel.setVisible(false);
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
		instructionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel rangeTime = new JLabel("Choose timeslot(s) from the available timeslots below. Can be multiple.");
		instructionPanel.add(rangeTime);
		browsePane.add(instructionPanel);
		
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
				instructionPanel.setVisible(false);
				timeListPane.setVisible(false);
				back2Btn.setVisible(false);
				confirmBtn.setVisible(false);
				timeListModel.removeAllElements();
				timeList.removeAll();
				timeInTheList.clear();
				
				dateOption.setVisible(true);
				addBtnPanel.setVisible(true);
				dateListPane.setVisible(true);
				instructionOrDurationPanel.setVisible(true);
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

			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// Collect the selected indices
				int[] selectedIndices = timeList.getSelectedIndices();
				// Warn user if there is no index selected
				if (selectedIndices.length == 0) {
					JOptionPane.showMessageDialog(CreateGroupEvent.this, "Please select at least one from the time list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				} 
				
				// Sort first
				Arrays.sort(selectedIndices);
			
				// Make a copy from our original indices array
				int[] selectedIndicesIncr = new int[selectedIndices.length];
				// Because our Utility.ArrayisConsecutive does not work on element with 0 
				if (selectedIndices[0] == 0) {
					
					// Increment every element by one
					for(int i = 0; i < selectedIndices.length; i++) {
						selectedIndicesIncr[i] = selectedIndices[i] + 1;
					}
					
					if (!Utility.ArrayIsConsecutive(selectedIndicesIncr, selectedIndicesIncr.length)) {
						JOptionPane.showMessageDialog(CreateGroupEvent.this, "You cannot select inconsecutive slots",
								"Input Error", JOptionPane.WARNING_MESSAGE);
						return;
					}
					
				} else {
					if (selectedIndices.length > 1) {
						
						int[] selectedIndicesMod = new int[selectedIndices.length];
						
						for (int i = 0; i < selectedIndices.length; i++) 
							selectedIndicesMod[i] = selectedIndices[i];
						
						if (!Utility.ArrayIsConsecutive(selectedIndicesMod, selectedIndicesMod.length)) {
							JOptionPane.showMessageDialog(CreateGroupEvent.this, "You cannot select inconsecutive slots",
									"Input Error", JOptionPane.WARNING_MESSAGE);
							return;
						}
					}
				}
				// Yes No Confirmation upon successful creation
				int result = JOptionPane.showConfirmDialog(CreateGroupEvent.this, "Confirm the following event?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					// If answer is yes, we loop through the selected indices, 
					// use timeInTheList array to get the timespan element and add it to timeChosenList
					// (Map user selection to our array)

					if (selectedIndices.length == 1) {
						timeSlotChosen.add(timeInTheList.get(selectedIndices[0]));
					} else {
						TimeSpan first = timeInTheList.get(selectedIndices[0]);
						TimeSpan last = timeInTheList.get(selectedIndices[selectedIndices.length - 1]);
							
						Timestamp start = new Timestamp(0);
						start.setYear(first.StartTime().getYear());
						start.setMonth(first.StartTime().getMonth());
						start.setDate(first.StartTime().getDate());
						start.setHours(first.StartTime().getHours());
						start.setMinutes(first.StartTime().getMinutes());
						//System.out.println(first.StartTime().getHours() + ":" + first.StartTime().getMinutes());
						
						Timestamp end = new Timestamp(0);
						end.setYear(last.EndTime().getYear());
						end.setMonth(last.EndTime().getMonth());
						end.setDate(last.EndTime().getDate());
						end.setHours(last.EndTime().getHours());
						end.setMinutes(last.EndTime().getMinutes());
						//System.out.println(last.EndTime().getHours() + ":" + last.EndTime().getMinutes());
						TimeSpan merged = new TimeSpan(start, end);
						timeSlotChosen.add(merged);
					} 
					
					if (modify)
						userChosenList.clear();
					
					for (int i = 0; i < usernameChosenList.size(); i++) {
						userChosenList.add(parent.controller.searchUser(usernameChosenList.get(i)));
					}
					
					// Close window when we are done
					closeWindow();

				} else {
					return;
				}
				
				
			}
			
		});
		
		navPane.add(back2Btn);
		navPane.add(confirmBtn);
		
	}

	// Method to load the user list into the combobox leftList
	private void loadUserList() {
		// If user list is not empty
		if (!parent.controller.getUserList().isEmpty()) {
			ArrayList<User> userList = new ArrayList<User>(parent.controller.getUserList());
			
			leftListModel.removeAllElements();
			
			if (modify) {
				rightListModel.removeAllElements();
				for (User user : userChosenList) {
					if (!user.equals(parent.getCurrUser())) {
						rightListModel.addElement(user.getUsername());
						userList.remove(user);
					}
				}
				
				for (User userInTheList : userList) {
					if (!userInTheList.equals(parent.getCurrUser())) {
						leftListModel.addElement(userInTheList.getUsername());
					}
				}
				
			} else {
				for (User user : userList) {
					// Don't show yourself in the list. Just display other users
					if (!user.equals(parent.getCurrUser())) {
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
	}
	
	// Method to load the timeslots into the combobox timeList
	private void loadTimeSlots() {

		// Map the chosen user names into its corresponding user instance in apptstorage
		ArrayList<User> userAndCurrChosenList  = new ArrayList<User>();
		for (String name : usernameChosenList) {
			userAndCurrChosenList.add(parent.controller.searchUser(name));
		}
		
		// Add the current user as well because we also need to get available timeslots based on the current user
		userAndCurrChosenList.add(parent.controller.getCurrentUser());
		for (int i = 0; i < dateChosenList.size(); i++) {
			// Retrieve the available time slots
			ArrayList<TimeSpan> timeSlots = parent.controller.RetrieveAvailTimeSpans(userAndCurrChosenList, dateChosenList.get(i));
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
					
					// Add the information of each timeslot to the timeList combo box
					timeListModel.addElement( date + " " + CalGrid.months[dateChosenList.get(i).StartTime().getMonth()] + " " + year  
							+ "  " +
							// Display time
							timeSlots.get(j).StartTime().getHours() + ":" + Smins + " to " + 
							timeSlots.get(j).EndTime().getHours() + ":" +  Emins);
		
					// Also add the corresponding TimeSpan element to the timeInTheList array to ease mapping to the timeChosenList
					timeInTheList.add(timeSlots.get(j));
			}
		}
		}
	}
	
	// Close this window
	private void closeWindow() {
		setVisible(false);
		dispose();
	}
}

