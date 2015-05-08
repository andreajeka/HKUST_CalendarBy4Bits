package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.ClockListeners;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.CalendarClock;
import hkust.cse.calendar.users.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class CalGrid extends JFrame implements ActionListener, ClockListeners {
	
	// private User mNewUser;
	private static final long serialVersionUID = 1L;
	public ApptStorageControllerImpl controller;
	public User mCurrUser;
	private String mCurrTitle = "Desktop Calendar - No User - ";
	private GregorianCalendar today;
	public int currentD;
	public int currentM;
	public int currentY;
	public int previousRow;
	public int previousCol;
	public int currentRow;
	public int currentCol;
	private BasicArrowButton eButton;
	private BasicArrowButton wButton;
	private JLabel year;
	private JComboBox<String> month;
	public CalendarClock CalClock;

	private final Object[][] data = new Object[6][7];
	//private final Vector[][] apptMarker = new Vector[6][7];
	public static final String[] names = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };
	public static final String[] months = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	private JTable tableView;
	private AppList applist;
	public static final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private JTextPane note;

	private JSplitPane upper;
	private JSplitPane whole;
	private JScrollPane scrollpane;
	private StyledDocument mem_doc = null;
	private SimpleAttributeSet sab = null;
	// private boolean isLogin = false;
	private JMenu Appmenu = new JMenu("Appointment");
	private JMenu timeMenu = new JMenu("Time Machine");
	private JMenu Settings = new JMenu("Settings");
	private JMenuItem miManageUsers = new JMenuItem("Manage Users");
	private JMenuItem miManageLocs = new JMenuItem("Manage Locations");
	private JButton publicEvents = new JButton("Public Events");
	
	// Bonus Feature Declarations and Assignments
	private JTextField searchField = new JTextField(10);
	private JButton searchBut = new JButton("Search");
	private Vector<String> list = new Vector<String>();
	private JComboBox<String> searchBox = new JComboBox<String>();
	private boolean hide_flag = false;
	public Appt[] apptList;

	private final String[] holidays = {
			"New Years Day\nSpring Festival\n",
			"President's Day (US)\n",
			"",
			"Ching Ming Festival\nGood Friday\nThe day following Good Friday\nEaster Monday\n",
			"Labour Day\nThe Buddha's Birthday\nTuen Ng Festival\n",
			"",
			"Hong Kong Special Administrative Region Establishment Day\n",
			"Civic Holiday(CAN)\n",
			"",
			"National Day\nChinese Mid-Autumn Festival\nChung Yeung Festival\nThanksgiving Day\n",
			"Veterans Day(US)\nThanksgiving Day(US)\n", "Christmas\n" };

	private AppScheduler setAppDial;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public CalGrid(ApptStorageControllerImpl con) {
		super();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				controller.distributeRequest();
				controller.sortRequest();
				controller.SaveRequestToXml();
				controller.SaveLocToXml();
				controller.SaveApptToXml();
				controller.SaveUserToXml();
				controller.SaveFeedbacksToXml();
				System.exit(0);
			}
		});

		controller = con;
		mCurrUser = null;
		CalClock = new CalendarClock();
		CalClock.runningClockListener(this);
		previousRow = 0;
		previousCol = 0;
		currentRow = 0;
		currentCol = 0;

		applist = new AppList();
		applist.setParent(this);
		
		
		setJMenuBar(createMenuBar());

		today = new GregorianCalendar();
		currentY = today.get(Calendar.YEAR);
		currentD = today.get(Calendar.DAY_OF_MONTH);
		int temp = today.get(Calendar.MONTH) + 1;
		currentM = 12;

		getDateArray(data);

		JPanel leftP = new JPanel();
		leftP.setLayout(new BorderLayout());
		leftP.setPreferredSize(new Dimension(500, 300));

		JLabel textL = new JLabel("Important Days");
		textL.setForeground(Color.red);

		note = new JTextPane();
		note.setEditable(false);
		note.setBorder(new Flush3DBorder());
		mem_doc = note.getStyledDocument();
		sab = new SimpleAttributeSet();
		StyleConstants.setBold(sab, true);
		StyleConstants.setFontSize(sab, 30);

		JPanel noteP = new JPanel();
		noteP.setLayout(new BorderLayout());
		noteP.add(textL, BorderLayout.NORTH);
		noteP.add(note, BorderLayout.CENTER);

		leftP.add(noteP, BorderLayout.CENTER);

		eButton = new BasicArrowButton(SwingConstants.EAST);
		eButton.setEnabled(true);
		eButton.addActionListener(this);
		wButton = new BasicArrowButton(SwingConstants.WEST);
		wButton.setEnabled(true);
		wButton.addActionListener(this);

		year = new JLabel(new Integer(currentY).toString());
		month = new JComboBox<String>();
		month.addActionListener(this);
		month.setPreferredSize(new Dimension(200, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			month.addItem(months[cnt]);
		month.setSelectedIndex(temp - 1);

		JPanel yearGroup = new JPanel();
		yearGroup.setLayout(new FlowLayout());
		yearGroup.setBorder(new Flush3DBorder());
		yearGroup.add(wButton);
		yearGroup.add(year);
		yearGroup.add(eButton);
		yearGroup.add(month);

		leftP.add(yearGroup, BorderLayout.NORTH);

		TableModel dataModel = prepareTableModel();
		
		tableView = new JTable(dataModel) {
			@SuppressWarnings("deprecation")
			public TableCellRenderer getCellRenderer(int row, int col) {
				String tem = (String) data[row][col];
				ArrayList<dayInfo> values = new ArrayList<dayInfo>();
				if (tem.equals("") == false) {
					// Retrieve from storage
					Timestamp start = new Timestamp(0);
					start.setYear(currentY - 1900);
					start.setMonth(currentM - 1);
					start.setDate(Integer.parseInt(tem));
					start.setHours(0);
					start.setMinutes(0);

					Timestamp end = new Timestamp(0);
					end.setYear(currentY - 1900);
					end.setMonth(currentM - 1);
					end.setDate(Integer.parseInt(tem) + 1);
					end.setHours(0);
					end.setMinutes(0);
					
					Appt[] appData = controller.RetrieveAppts(mCurrUser, new TimeSpan(start, end));
					
					try {
						// Change today to red
						if (today.get(Calendar.YEAR) == currentY
								&& today.get(Calendar.MONTH) + 1 == currentM
								&& today.get(Calendar.DAY_OF_MONTH) == Integer
										.parseInt(tem)) {
							values.add(dayInfo.Today);
							
							if (appData != null) {
								values.add(dayInfo.HasEvent);
							}
								
						}
						// Change days with event to green
						else {
							if (appData != null)
								values.add(dayInfo.HasEvent);
						}
						
						return new CalCellRenderer(values);
					} catch (Throwable e) {
						System.exit(1);
					}

				}
				values.clear();
				values.add(dayInfo.Empty);
				return new CalCellRenderer(values);
			}
		};

		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(40);
		tableView.setRowSelectionAllowed(false);
		tableView.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				mousePressResponse();
			}

			public void mouseReleased(MouseEvent e) {
				mouseResponse();
			}
		});

		JTableHeader head = tableView.getTableHeader();
		head.setReorderingAllowed(false);
		head.setResizingAllowed(true);

		scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.RAISED));
		scrollpane.setPreferredSize(new Dimension(536, 260));

		upper = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftP, scrollpane);

		whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upper, applist);
		getContentPane().add(whole);

		
		initializeSystem(); // for you to add.

		Settings.setEnabled(true);
		Appmenu.setEnabled(true);
		timeMenu.setEnabled(true);

		// Loading appts to the list after initializing system.
	    loadSearchBoxList();
	 
	    setModel(new DefaultComboBoxModel(list), "");
		
		
		UpdateCal();
		pack();				// sized the window to a preferred size
		setVisible(true);	//set the window to be visible
	}
	
	public void loadSearchBoxList() {
		list.clear();
		apptList = controller.RetrieveAppts(mCurrUser);
		for (int i = 0; i < apptList.length; i++) {
	    	list.addElement(apptList[i].getTitle());
	    }
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return 6;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				System.out.println("Setting value to: " + aValue);
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	@SuppressWarnings("deprecation")
	public void getDateArray(Object[][] data) {
		GregorianCalendar c = new GregorianCalendar(currentY, currentM - 1, 1);
		int day;
		int date;
		Date d = c.getTime();
		c.setTime(d);
		day = d.getDay();
		date = d.getDate();

		if (c.isLeapYear(currentY)) {

			monthDays[1] = 29;
		} else
			monthDays[1] = 28;

		int temp = day - date % 7;
		if (temp > 0)
			day = temp + 1;
		else if (temp < 0)
			day = temp + 1 + 7;
		else
			day = date % 7;
		day %= 7;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++) {
				int temp1 = i * 7 + j - day + 1;
				if (temp1 > 0 && temp1 <= monthDays[currentM - 1])
					data[i][j] = new Integer(temp1).toString();
				else
					data[i][j] = new String("");
			}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	JMenuBar createMenuBar() {
		
		ActionListener listener = new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Manual Scheduling")) {
					if (controller.getLocationList().size() != 0) {
						AppScheduler a = new AppScheduler("New", CalGrid.this);
						a.updateSetApp(hkust.cse.calendar.gui.Utility
								.createDefaultAppt(currentY, currentM, currentD,
										mCurrUser));
						a.setLocationRelativeTo(null);
						a.show();
						TableModel t = prepareTableModel();
						tableView.setModel(t);
						tableView.repaint();
					} else {
						JOptionPane.showMessageDialog(null, "Input at least one location in 'Manage Locations'", "NO LOCATION!", JOptionPane.ERROR_MESSAGE);
					}
				}
				else if (e.getActionCommand().equals("Open")){
					TimeMachine tm = new TimeMachine("Time Machine", CalGrid.this, CalClock);
					tm.setLocationRelativeTo(null);
					tm.show();
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				else if (e.getActionCommand().equals("Manage Users")) {
					ManageUsersDialog mud = new ManageUsersDialog(controller , mCurrUser);
					mud.show();
					mud.setLocationRelativeTo(null);
					TableModel t  = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				else if(e.getActionCommand().equals("Personal Setting"))
				{	
					PersonalSettingDialog psd = new PersonalSettingDialog(controller, mCurrUser);
					psd.setLocationRelativeTo(null);
					psd.show();
				}
				else if(e.getActionCommand().equals("Public Events")) 
				{
					PublicEvents pubEvents = new PublicEvents(CalGrid.this);
					pubEvents.setLocationRelativeTo(null);
					pubEvents.show();
				} 
				else if (e.getActionCommand().equals("Automatic Group Event Scheduling")) {
					
					if (controller.getLocationList().size() != 0) {
						AppScheduler a = new AppScheduler("Automatic Group Event", CalGrid.this, true);
						a.updateSetApp(hkust.cse.calendar.gui.Utility
								.createDefaultAppt(currentY, currentM, currentD,
										mCurrUser));
						a.setLocationRelativeTo(null);
						a.show();
						TableModel t = prepareTableModel();
						tableView.setModel(t);
						tableView.repaint();
					} else {
						JOptionPane.showMessageDialog(null, "Input at least one location in 'Manage Locations'", "NO LOCATION!", JOptionPane.ERROR_MESSAGE);
					}					
				}
			
				else if(e.getActionCommand().equals("Manage Locations")){
					LocationsDialog b = new LocationsDialog(controller);
					b.show();
					b.setLocationRelativeTo(null);
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
			}
		};
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Calendar Choices");
		JMenuItem mi;

		menuBar.add(Settings);
		Settings.setMnemonic('S');
		Settings.setEnabled(false);
		Settings.getAccessibleContext().setAccessibleDescription(
				"Account Access Management");		
		// Add personal setting button to the menu
		mi = (JMenuItem) Settings.add(new JMenuItem("Personal Setting"));
		mi.setMnemonic('P');
		mi.getAccessibleContext().setAccessibleDescription("To change your infomation");
		mi.addActionListener(listener);
		
		Settings.add(miManageUsers);
		miManageUsers.setMnemonic('M');
		miManageUsers.getAccessibleContext().setAccessibleDescription("For managing other users");
		miManageUsers.addActionListener(listener);
		
		mi = (JMenuItem) Settings.add(new JMenuItem("Logout"));	//adding a Logout menu button for user to logout
		mi.setMnemonic('L');
		mi.getAccessibleContext().setAccessibleDescription("For user logout");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.distributeRequest();
				controller.sortRequest();
				controller.SaveRequestToXml();
				controller.SaveLocToXml();
				controller.SaveApptToXml();
				controller.SaveUserToXml();
				controller.SaveFeedbacksToXml();
				int n = JOptionPane.showConfirmDialog(null, "Logout?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					//controller.dumpStorageToFile();
					//System.out.println("closed");
					dispose();
					CalendarMain.logOut = true;
					return;	//return to CalendarMain()
				}
			}
		});
		
		mi = (JMenuItem) Settings.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.getAccessibleContext().setAccessibleDescription("Exit Program");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// save data from hasp map into disk
				controller.distributeRequest();
				controller.sortRequest();
				controller.SaveRequestToXml();
				controller.SaveFeedbacksToXml();
				controller.SaveLocToXml();
				controller.SaveApptToXml();
				controller.SaveUserToXml();
				int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});

		// Add "Appointment" to menuBar
		menuBar.add(Appmenu);
		Appmenu.setEnabled(false);
		Appmenu.setMnemonic('p');
		Appmenu.getAccessibleContext().setAccessibleDescription(
				"Appointment Management");
		
		// Add group event scheduling
		mi = new JMenuItem("Automatic Group Event Scheduling");
		mi.addActionListener(listener);
		Appmenu.add(mi);
	
		// Add manual scheduling to Appmenu
		mi = new JMenuItem("Manual Scheduling");
		mi.addActionListener(listener);
		Appmenu.add(mi);
		
        // Add location to Appmenu
		miManageLocs.addActionListener(listener);
		Appmenu.add(miManageLocs);
		
		// Add "Time Machine" to the menuBar
		menuBar.add(timeMenu);
		timeMenu.setEnabled(false);
		timeMenu.setMnemonic('t');
		timeMenu.getAccessibleContext().setAccessibleDescription(
				"Time Machine");
		mi = new JMenuItem("Open");
		mi.addActionListener(listener);
		
		timeMenu.add(mi);

		 // TODO BONUS listeners
		searchField = (JTextField) searchBox.getEditor().getEditorComponent();
		searchBox.setEditable(true);
		
	   
	    searchBut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String input = searchField.getText();
				if (input.isEmpty())
					return;
				else {
					if (!list.contains(input))
						JOptionPane.showMessageDialog(null, "Not Found!", "Search Result",  JOptionPane.INFORMATION_MESSAGE);
					else {
						int index = list.indexOf(input);
						Appt appt = apptList[index];
						currentY = appt.TimeSpan().StartTime().getYear() + 1900;
						currentM = appt.TimeSpan().StartTime().getMonth() + 1;
						currentD = appt.TimeSpan().StartTime().getDate();
						getDateArray(data);
						if (tableView != null) {
							TableModel t = prepareTableModel();
							tableView.setModel(t);
							tableView.repaint();
						}
						UpdateCal();
					}
						
					
				}
				getDateArray(data);
				if (tableView != null) {
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				UpdateCal();
			}
	    	
	    });
	    searchField.addKeyListener(new KeyAdapter() {
	    	public void keyPressed(KeyEvent e) {
	    		String input = searchField.getText();
	    		int code = e.getKeyCode();
	    		if (code == KeyEvent.VK_ENTER) {
	    			if (!list.contains(input)) {
	    				list.addElement(input);
	    				Collections.sort(list);
	    				setModel(getSuggestedModel(list, input), input);
	    			}
	    			hide_flag = true;
	    		} else if (code == KeyEvent.VK_ESCAPE) {
	    			hide_flag = true;
	    		} else if (code == KeyEvent.VK_DOWN) {
	    			for (int i = 0; i < list.size(); i++) {
	    				String str = list.elementAt(i);
	    				if (str.startsWith(input)) {
	    					searchBox.setSelectedIndex(-1);
	    					searchField.setText(input);
	    					return;
	    				}
	    			}
	    		}
			}
	
			public void keyReleased(KeyEvent e) {
				EventQueue.invokeLater(new Runnable() {

					@Override
					public void run() {
						String input = searchField.getText();
						if (input.length() == 0) {
							searchBox.hidePopup();
							setModel(new DefaultComboBoxModel(list), "");
						} else {
							DefaultComboBoxModel cbM = getSuggestedModel(list, input);
							if (cbM.getSize() == 0 || hide_flag) {
								searchBox.hidePopup();
								hide_flag = false;
							} else {
								setModel(cbM, input);
								searchBox.showPopup();
							}
						}
					}
				});
			}
	    });
	    
		
		// Put items after the following line to align them to the right of menu bar
	    menuBar.add(Box.createHorizontalGlue());
	    menuBar.add(searchBox);
	    menuBar.add(searchBut);
	    JLabel space = new JLabel("                          "
	    		+ "   ");
	    menuBar.add(space);
		menuBar.add(publicEvents);
		publicEvents.addActionListener(listener);
	
		
		return menuBar;
	}

	@SuppressWarnings("unchecked")
	private void setModel(@SuppressWarnings("rawtypes") DefaultComboBoxModel model, String str) {
		searchBox.setModel(model);
		searchBox.setSelectedIndex(-1);
		searchField.setText(str);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static DefaultComboBoxModel getSuggestedModel(List<String> list, String text) {
		DefaultComboBoxModel m = new DefaultComboBoxModel();
		for (String s : list) {
			if (s.startsWith(text)) m.addElement(s);
		}
		
		return m;
	}
	
	private void initializeSystem() {
		mCurrUser = controller.getCurrentUser();	//get User from controller
		
		if (!mCurrUser.isAdmin()) {
			miManageUsers.setEnabled(false);
			miManageLocs.setEnabled(false);
		} else { 
			miManageUsers.setEnabled(true);
			miManageLocs.setEnabled(true);
		}
		
		//controller.LoadApptFromXml();
		//controller.LoadLocFromXml();	
		checkUpdateJoinAppt();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == eButton) {
			if (year == null)
				return;
			currentY = currentY + 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();
			}
			UpdateCal();
			
		} else if (e.getSource() == wButton) {
			if (year == null)
				return;
			currentY = currentY - 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();
			}
			UpdateCal();
			
		} else if (e.getSource() == month) {
			if (month.getSelectedItem() != null) {
				currentM = month.getSelectedIndex() + 1;
				try {
					mem_doc.remove(0, mem_doc.getLength());
					mem_doc.insertString(0, holidays[currentM - 1], sab);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

				CalGrid.this.setTitle("Desktop Calendar - No User - ("
						+ currentY + "-" + currentM + "-" + currentD + ")");
				getDateArray(data);
				if (tableView != null) {
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}
				UpdateCal();
			}
		}
	}

	// update the appointment list on gui
	public void updateAppList() {
		applist.clear();
		applist.repaint();
		applist.setTodayAppt(GetTodayAppt());
	}

	// Enum to set day color
	public enum dayInfo{
		Today, HasEvent, Empty
	}
	
	public void UpdateCal() {
		if (mCurrUser != null) {
			mCurrTitle = "Desktop Calendar - " + mCurrUser.getUsername() + " - ";
			this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM + "-"
					+ currentD + ")");
			Appt[] monthAppts = null;
			GetMonthAppts();

//			for (int i = 0; i < 6; i++)
//				for (int j = 0; j < 7; j++)
//					apptMarker[i][j] = new Vector(10, 1);
			
			
			TableModel t = prepareTableModel();
			this.tableView.setModel(t);
			this.tableView.repaint();
			updateAppList();
		}
	}

//	public void clear() {
//		for (int i = 0; i < 6; i++)
//			for (int j = 0; j < 7; j++)
//				apptMarker[i][j] = new Vector(10, 1);
//		TableModel t = prepareTableModel();
//		tableView.setModel(t);
//		tableView.repaint();
//		applist.clear();
//	}


	private Appt[] GetMonthAppts() {
		// This will fix issue with year
		String curYear = Integer.toString(currentY);
	    int month = currentM; //no need to minus 1 because Timestamp.valueOf automatically -1 inside it. WOW RIGHT!
	    String curMonth = Integer.toString(month);
	    Timestamp start = Timestamp.valueOf(curYear+"-"+curMonth+"-"+"1 00:00:00");

	    GregorianCalendar g = new GregorianCalendar(currentY, month, 1);
	    int dateIntMax = g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
	    String dateMax = Integer.toString(dateIntMax);
		Timestamp end = Timestamp.valueOf(curYear+"-"+curMonth+"-"+dateMax+" 23:59:59");
		TimeSpan period = new TimeSpan(start, end);
		return controller.RetrieveAppts(mCurrUser, period);
	}

	private void mousePressResponse() {
		previousRow = tableView.getSelectedRow();
		previousCol = tableView.getSelectedColumn();
	}
	
	private void mouseResponse() {
		int[] selectedRows = tableView.getSelectedRows();
		int[] selectedCols = tableView.getSelectedColumns();
		if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() != previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = tableView.getSelectedRow();
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() != previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = tableView.getSelectedColumn();
		}
		else{
			currentRow = tableView.getSelectedRow();
			currentCol = tableView.getSelectedColumn();
		}
		
		if (currentRow > 5 || currentRow < 0 || currentCol < 0
				|| currentCol > 6)
			return;

		if (!tableView.getModel().getValueAt(currentRow, currentCol).equals(""))
			try {
				currentD = new Integer((String) tableView.getModel()
						.getValueAt(currentRow, currentCol)).intValue();
			} catch (NumberFormatException n) {
				return;
			}
		CalGrid.this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM
				+ "-" + currentD + ")");
		updateAppList();
	}

	@SuppressWarnings("deprecation")
	public boolean IsTodayAppt(Appt appt) {
		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentY)
			return false;
		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentM)
			return false;
		if (appt.TimeSpan().StartTime().getDate() != currentD)
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	public boolean IsMonthAppts(Appt appt) {

		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentY)
			return false;

		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentM)
			return false;
		return true;
	}

	public Appt[] GetTodayAppt() {
		String curYear = Integer.toString(currentY);
	    int month = currentM; //no need to minus 1 because Timestamp.valueOf automatically -1 inside it. WOW RIGHT!
	    String curMonth = Integer.toString(month);
	    Integer date = new Integer(currentD);
	    String curDate= Integer.toString(date);

	    Timestamp start = Timestamp.valueOf(curYear+"-"+curMonth+"-"+curDate+" 00:00:00");
	    Timestamp end = Timestamp.valueOf(curYear+"-"+curMonth+"-"+curDate+" 23:59:59");

		TimeSpan period = new TimeSpan(start, end);
		return controller.RetrieveAppts(mCurrUser, period);
	}
	
	public AppList getAppList() {
		return applist;
	}

	public User getCurrUser() {
		return mCurrUser;
	}
	
	// check for any invite or update from join appointment
	public void checkUpdateJoinAppt(){
		// Fix Me!
	}
	
	// Change today to a custom date and time, to be called from time machine widget
	public void setToday(GregorianCalendar gc){
		// Set application time
		//today.set(yyyy, mm, dd, hour, min);
		today = gc;
		currentY = today.get(Calendar.YEAR);
		currentM = today.get(Calendar.MONTH) + 1;
		currentD = today.get(Calendar.DAY_OF_MONTH);
		// Update the display
		year.setText(new Integer(currentY).toString());
		month.setSelectedIndex(currentM - 1);
		getDateArray(data);
		TableModel t = prepareTableModel();
		tableView.setModel(t);
		tableView.repaint();
		UpdateCal();
	}
	
	public GregorianCalendar getToday(){
		return today;
	}

	// Implement the handler of clock emitter
	@SuppressWarnings("deprecation")
	@Override
	public void timeIsElapsing(CalendarClock emitter) {
		
		String message = "";
		String digitHour = "";
		Timestamp now = emitter.getCurrentTime();
		
		Timestamp start = new Timestamp(0);
		start.setYear(now.getYear());
		start.setMonth(now.getMonth());
		start.setDate(now.getDate());
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		
		Timestamp end = new Timestamp(0);
		end.setYear(now.getYear());
		end.setMonth(now.getMonth());
		end.setDate(now.getDate());
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		Appt[] appData = controller.RetrieveAppts(mCurrUser, new TimeSpan(start, end));

		// nextElapsedTime = time of clock + delay
		Timestamp nextElapsedTime = emitter.getNextElapsedTime();					
		Timestamp timeForReminder = new Timestamp(0);

		if (appData != null) {
			for (int i = 0; i < appData.length; i++) {
				timeForReminder.setTime(appData[i].TimeSpan().StartTime().getTime() - appData[i].getRemindBefore());
				
				/*System.out.println("Now " + now + "equal to time for reminder " + (Utility.AfterBeforeEqual(now, timeForReminder) == 0) );
				System.out.println("Now " + now + "less than time for reminder " + (Utility.AfterBeforeEqual(now, timeForReminder) == -1) );
				System.out.println("Next elapsed " + nextElapsedTime + "greater than time for reminder " + (Utility.AfterBeforeEqual(nextElapsedTime, timeForReminder) == 1) );*/
				
				if ((Utility.AfterBeforeEqual(now, timeForReminder) == 0) || 
						((Utility.AfterBeforeEqual(now, timeForReminder) == -1) && (Utility.AfterBeforeEqual(nextElapsedTime, timeForReminder) == 1 ))) {
					if (appData[i].reminder()) {
						if (appData[i].TimeSpan().StartTime().getMinutes() < 10 )
							digitHour = "0";
						else digitHour = "";
						
						message = "You have an appointment [" + appData[i].getTitle() + 
							"] at " + appData[i].TimeSpan().StartTime().getHours() + ":" + 
							  digitHour + appData[i].TimeSpan().StartTime().getMinutes();
						// Pop up a notification window 
						JOptionPane.showMessageDialog(null, message, "Reminder",  JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}
}
