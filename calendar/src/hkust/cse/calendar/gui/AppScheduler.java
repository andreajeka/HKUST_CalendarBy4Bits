package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;


public class AppScheduler extends JDialog implements ActionListener,
		ComponentListener {

	private static final int MODE_ONCE = 0;
	private static final int MODE_DAILY = 1;
	private static final int MODE_WEEKLY = 2;
	private static final int MODE_MONTHLY = 3;
	
	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JTextField monthF;
	private JLabel dayL;
	private JTextField dayF;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;
	
	private JToggleButton reminderToggle;

	private JTextField titleField;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;
	
	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;
	private Vector<String> items;
	private String[] FreqStrings;
	
	private JSplitPane pDes;
	private JPanel detailPanel;
	private DefaultComboBoxModel<String> listModelString;
	private DefaultComboBoxModel<Integer> listModelInt;
	private JLabel titleLoc;
	private JComboBox<String> locField;
	private JLabel titleFreq;
	private JComboBox<String> FreqField;
	private JLabel titleFreqAmount;
	private JComboBox<Integer> FreqAmountField;
	private JLabel labelEnd;
	private JPanel pRemind;

//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;
	private int freqAmount = 0;
	private boolean isReminderToggled = false;
	

	@SuppressWarnings("deprecation")
	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();
		
		JPanel pDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		pDate.setBorder(dateBorder);

		yearL = new JLabel("YEAR: ");
		pDate.add(yearL);
		yearF = new JTextField(6);
		pDate.add(yearF);
		monthL = new JLabel("MONTH: ");
		pDate.add(monthL);
		monthF = new JTextField(4);
		pDate.add(monthF);
		dayL = new JLabel("DAY: ");
		pDate.add(dayL);
		dayF = new JTextField(4);
		pDate.add(dayF);

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		psTime.add(sTimeM);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		peTime.add(eTimeM);

		JPanel FnR = new JPanel();
		FnR.setLayout(new BorderLayout());
				
		JPanel pFreq = new JPanel();
		Border freqBorder = new TitledBorder(null, "FREQUENCY");
		pFreq.setBorder(freqBorder);

		//Add two combo box
		titleFreq = new JLabel("Frequency");
		
		FreqStrings = new String[] {"Once","Daily","Weekly","Monthly"};
		listModelString = new DefaultComboBoxModel<String>(FreqStrings);
		FreqField = new JComboBox<String>(listModelString);
		FreqField.setSelectedItem(FreqStrings[0]);
		
		FreqField.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				if (FreqField.getSelectedItem().equals(FreqStrings[0])) {
					titleFreqAmount.setEnabled(false);
					FreqAmountField.setEnabled(false);
					labelEnd.setText("");
				} else {
					titleFreqAmount.setEnabled(true);
					FreqAmountField.setEnabled(true);
					if (FreqField.getSelectedItem().equals(FreqStrings[1])) {
						titleFreqAmount.setText("For");
						labelEnd.setText("day(s)");
						pRemind.setBorder(new EmptyBorder(15,5,5,15));
					} else if (FreqField.getSelectedItem().equals(FreqStrings[2])) {
						titleFreqAmount.setText("Every");
						labelEnd.setText("week(s)");
						pRemind.setBorder(new EmptyBorder(15,10,5,5));
					} else if (FreqField.getSelectedItem().equals(FreqStrings[3])) {
						titleFreqAmount.setText("Every");
						labelEnd.setText("month(s)");
						pRemind.setBorder(new EmptyBorder(15,20,5,3));
					}
				}
			}
		});
		
		// Set a list of numbers for frequency amount. Max is 30
		Integer[] FreqAmount = new Integer[30];
		int count = 1;
		for (int i = 0; i < 30; i++) {
			FreqAmount[i] = count;
			count++;
		}
		titleFreqAmount = new JLabel("Every");
		titleFreqAmount.setEnabled(false);
		listModelInt = new DefaultComboBoxModel<Integer>(FreqAmount);
		FreqAmountField = new JComboBox<Integer>(listModelInt);
		FreqAmountField.setSelectedItem(FreqStrings[0]);
		FreqAmountField.setEnabled(false);
		
		labelEnd = new JLabel();
		
		pFreq.add(titleFreq);
		pFreq.add(FreqField);
		pFreq.add(titleFreqAmount);
		pFreq.add(FreqAmountField);
		pFreq.add(labelEnd);

		// Create a reminder toggle button
		pRemind = new JPanel();
		pRemind.setBorder(new EmptyBorder(15,5,5,25));
		
		reminderToggle = new JToggleButton("REMINDER OFF");
		reminderToggle.setBorder(new BevelBorder(BevelBorder.RAISED));
		reminderToggle.setForeground(Color.BLUE);
		reminderToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (reminderToggle.isSelected()) {
					reminderToggle.setText("REMINDER ON");
					reminderToggle.setBorder(new BevelBorder(BevelBorder.LOWERED));
					reminderToggle.setForeground(Color.RED);
					isReminderToggled = true;
				} else {
					reminderToggle.setText("REMINDER OFF");
					reminderToggle.setBorder(new BevelBorder(BevelBorder.RAISED));
					reminderToggle.setForeground(Color.BLUE);
					isReminderToggled = false;
				}
			}
		});
		
		pRemind.add(reminderToggle);
		
		FnR.add(pFreq, BorderLayout.WEST);
		FnR.add(pRemind, BorderLayout.EAST);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);
		pTime.add("East", peTime);
		pTime.add("South", FnR);
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTime, BorderLayout.CENTER);

		contentPane.add("North", top);

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);
		
/********   loading elements from cal._controller to combobox   *********/
		
		titleLoc = new JLabel("LOCATION");
		titleAndTextPanel.add(titleLoc);
		items = new Vector<String>();
		Location[] locations = cal.controller.getLocationList();
		if(locations == null){
			locations = new Location[0];
			items.addElement("--EMPTY--");
		}
		else if(locations != null){
			items.clear();
			for(int i=0; i<locations.length; i++){
				items.addElement(locations[i].getName().toString());
			}
		}
		listModelString = new DefaultComboBoxModel<String>(items);
		locField = new JComboBox<String>(listModelString);
		titleAndTextPanel.add(locField);
		

		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

//		inviteBut = new JButton("Invite");
//		inviteBut.addActionListener(this);
//		panel2.add(inviteBut);
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);
		rejectBut.show(false);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();

	}
	
	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	public void actionPerformed(ActionEvent e) {
		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {

			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			saveButtonResponse();

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		}
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetTodayAppt());
		parent.repaint();
	}
	

	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

	}

	private int[] getValidDate() {

		int[] date = new int[3];
		date[0] = Utility.getNumber(yearF.getText());
		date[1] = Utility.getNumber(monthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(dayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}

	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}

	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);
		result[1] = getTime(eTimeH, eTimeM);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (!sTimeM.getText().equals("0") && !sTimeM.getText().equals("15") && !sTimeM.getText().equals("30") && !sTimeM.getText().equals("45") 
			|| !eTimeM.getText().equals("0") && !eTimeM.getText().equals("15") && !eTimeM.getText().equals("30") && !eTimeM.getText().equals("45")){
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (result[1] <= result[0]) {
			JOptionPane.showMessageDialog(this,
					"End time should be bigger than \nstart time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}

	private void saveButtonResponse() {
		// Save the appointment to the hard disk
		/* Assign information to the newly created appointment. */
		int[] date = getValidDate(); // date[0] refers to year, date[1] refers to month, date[2] refers to day
		int[] time = getValidTimeInterval(); // time[0] refers to start time, time[1] refers to end time
		
		freqAmount = FreqAmountField.getSelectedIndex() + 1;
		
		// Check if there is no appointment selected in the appointment list
		if (selectedApptId == -1) {
			/* Save the appointment to the hard disk (AppStorageController to ApptStorage) */
			if (FreqField.getSelectedItem().equals("Once")) {
				addAppt(date, time, MODE_ONCE, ApptStorageControllerImpl.NEW);
			} else if (FreqField.getSelectedItem().equals("Daily")) {
				addAppt(date, time, MODE_DAILY, ApptStorageControllerImpl.NEW);
			} else if (FreqField.getSelectedItem().equals("Weekly")) {
				addAppt(date, time, MODE_WEEKLY, ApptStorageControllerImpl.NEW);
			} else if (FreqField.getSelectedItem().equals("Monthly")) {
				addAppt(date, time, MODE_MONTHLY, ApptStorageControllerImpl.NEW);
			}
		} else {
			if (FreqField.getSelectedItem().equals("Once")) {
				addAppt(date, time, MODE_ONCE, ApptStorageControllerImpl.MODIFY);
			} else if (FreqField.getSelectedItem().equals("Daily")) {
				addAppt(date, time, MODE_DAILY, ApptStorageControllerImpl.MODIFY);
			} else if (FreqField.getSelectedItem().equals("Weekly")) {
				addAppt(date, time, MODE_WEEKLY, ApptStorageControllerImpl.MODIFY);
			} else if (FreqField.getSelectedItem().equals("Monthly")) {
				addAppt(date, time, MODE_MONTHLY, ApptStorageControllerImpl.MODIFY);
			}
			
			selectedApptId = -1;
		}
		
		
		setVisible(false);
		dispose();
	}
	
	private void addAppt(int[] date, int[] time, int mode, int action) {
		String title = titleField.getText();
		String info = detailArea.getText();
		String location = (String) locField.getSelectedItem();
		
		if (mode == MODE_ONCE) {
			Timestamp stampStart = CreateTimeStamp(date,time[0]);
			Timestamp stampEnd = CreateTimeStamp(date, time[1]);
			TimeSpan timeSpan = new TimeSpan(stampStart, stampEnd);
			
			if (selectedApptId != -1) {
				NewAppt.setID(selectedApptId);
			}
			NewAppt.setTimeSpan(timeSpan);
			NewAppt.setTitle(title);
			NewAppt.setInfo(info);
			NewAppt.setLocation(location);
			NewAppt.setFrequency("Once");
			NewAppt.setFrequencyAmount(freqAmount);
			NewAppt.reminderOn(reminderToggle.isSelected());
			
			parent.controller.ManageAppt(NewAppt, action);
			
		} else if (mode == MODE_DAILY) {
			Integer initMonth = date[1];
			for (int i = 0; i < freqAmount; i++) {
				Timestamp stampStart = CreateTimeStamp(date,time[0]);
				Timestamp stampEnd = CreateTimeStamp(date, time[1]);
				TimeSpan timeSpan = new TimeSpan(stampStart, stampEnd);
				
				Appt appt = new Appt();
				if (selectedApptId != -1) {
					NewAppt.setID(selectedApptId);
				}
				appt.setTimeSpan(timeSpan);
				appt.setTitle(title);
				appt.setInfo(info);
				appt.setLocation(location);
				appt.setFrequency("Daily");
				appt.setFrequencyAmount(freqAmount);
				appt.reminderOn(reminderToggle.isSelected());
				
				parent.controller.ManageAppt(appt, action);
				
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				try {
					Date tempDate;
					if (date[1] < 10) {
						tempDate = dateFormat.parse(String.valueOf(date[0] + "0" + String.valueOf(date[1]) + String.valueOf(date[2])));
					} else {
						tempDate = dateFormat.parse(String.valueOf(date[0] + String.valueOf(date[1]) + String.valueOf(date[2])));
					}
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(tempDate);
					calendar.add(Calendar.DATE, 1);
					tempDate = calendar.getTime();
		
					date[0] = calendar.get(Calendar.YEAR);
					date[1] = calendar.get(Calendar.MONTH) + 1;
					date[2] = calendar.get(Calendar.DAY_OF_MONTH);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else if (mode == MODE_WEEKLY) {
			for (int i = 0; i < freqAmount; i++) {
				Timestamp stampStart = CreateTimeStamp(date,time[0]);
				Timestamp stampEnd = CreateTimeStamp(date, time[1]);
				TimeSpan timeSpan = new TimeSpan(stampStart, stampEnd);
				
				Appt appt = new Appt();
				appt.setTimeSpan(timeSpan);
				appt.setTitle(title);
				appt.setInfo(info);
				appt.setLocation(location);
				appt.setFrequency("Weekly");
				appt.setFrequencyAmount(freqAmount);
				appt.reminderOn(reminderToggle.isSelected());
				
				parent.controller.ManageAppt(appt, action);
				
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				try {
					Date tempDate;
					if (date[1] < 10) {
						tempDate = dateFormat.parse(String.valueOf(date[0] + "0" + String.valueOf(date[1]) + String.valueOf(date[2])));
					} else {
						tempDate = dateFormat.parse(String.valueOf(date[0] + String.valueOf(date[1]) + String.valueOf(date[2])));
					}
					
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(tempDate);
					calendar.add(Calendar.WEEK_OF_MONTH, 1);
					tempDate = calendar.getTime();
					date[0] = calendar.get(Calendar.YEAR);
					date[1] = calendar.get(Calendar.MONTH) + 1;
					date[2] = calendar.get(Calendar.DAY_OF_MONTH);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		} else if (mode == MODE_MONTHLY) {
			Integer initDay = date[2];
			
			for (int i = 0; i < freqAmount; i++) {
				if (date[2] != 0) {
					// Appointment with non-existing date skipped
					Timestamp stampStart = CreateTimeStamp(date,time[0]);
					Timestamp stampEnd = CreateTimeStamp(date, time[1]);
					TimeSpan timeSpan = new TimeSpan(stampStart, stampEnd);
					
					Appt appt = new Appt();
					appt.setTimeSpan(timeSpan);
					appt.setTitle(title);
					appt.setInfo(info);
					appt.setLocation(location);
					appt.setFrequency("Monthly");
					appt.setFrequencyAmount(freqAmount);
					appt.reminderOn(reminderToggle.isSelected());
					
					parent.controller.ManageAppt(appt, action);
				} else {
					// Reset the day to the initial value for further computation
					date[2] = initDay;
				}
				
				date[1]++;
				
				if (date[1] == 13) {
					date[0]++;
					date[1] = 1;
				}
				
				// Used for checking date validity
				// e.g. Not every month has day 31, day of month is marked as 0 when it does not exist
				if (date[2] > CalGrid.monthDays[date[1] - 1]) {
					date[2] = 0;
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1); // it is (x-1) because Timespan's month is from 0 to 11
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	@SuppressWarnings("deprecation")
	public void updateSetApp(Appt appt) {
		
		// Update Appt Scheduler fields with the appropriate content 
		TimeSpan time = appt.TimeSpan();
		
		// Use Calendar to fix year issue 
		// Because using timestamp.getYear() returns 115 instead of 2015
		Calendar start = new GregorianCalendar();
		start.setTimeInMillis(time.StartTime().getTime());
		
		String timeY = Integer.toString(start.get(Calendar.YEAR)); 
		int monthSync = start.get(Calendar.MONTH) + 1; 
		String timeM = Integer.toString(monthSync);
		String timeD = Integer.toString(start.get(Calendar.DATE));
		String timeHourS = Integer.toString(time.StartTime().getHours());
		String timeHourE = Integer.toString(time.EndTime().getHours());
		String timeMinS = Integer.toString(time.StartTime().getMinutes());
		String timeMinE = Integer.toString(time.EndTime().getMinutes());
		
	    yearF.setText(timeY); 
	    monthF.setText(timeM);
	    dayF.setText(timeD);
	    sTimeH.setText(timeHourS);
	    sTimeM.setText(timeMinS);
	    eTimeH.setText(timeHourE);
	    eTimeM.setText(timeMinE);
	    String locationText = appt.getLocation();
	    for (int i = 0; i < parent.controller.getLocationCapacity(); i++) {
	    	if (locationText.equals(locField.getItemAt(i)))
	    		locField.setSelectedIndex(i);		
	    }
	  
	    reminderToggle.setSelected(appt.reminder());
     	// Action listener does not change the gui state, so retype the following
	    if (reminderToggle.isSelected()) {
			reminderToggle.setText("REMINDER ON");
			reminderToggle.setBorder(new BevelBorder(BevelBorder.LOWERED));
			reminderToggle.setForeground(Color.RED);
			isReminderToggled = true;
		} else {
			reminderToggle.setText("REMINDER OFF");
			reminderToggle.setBorder(new BevelBorder(BevelBorder.RAISED));
			reminderToggle.setForeground(Color.BLUE);
			isReminderToggled = false;
		}
	    
	    titleField.setText(appt.getTitle());
	    detailArea.setText(appt.getInfo());
	    
	    
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		detailPanel.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}
	
	public String getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser.getUsername();
	}
	
	private void allDisableEdit(){
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
}