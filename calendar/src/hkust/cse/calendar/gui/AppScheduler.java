package hkust.cse.calendar.gui;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.notification.OptionTimeSlot;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Request;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.border.TitledBorder;


public class AppScheduler extends JDialog implements ActionListener,
ComponentListener {

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
	/*	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;*/


	private JTextArea detailArea;
	private Vector<String> items, itemsPlusCapa;
	private String[] FreqStrings;

	private JSplitPane pDes;
	private JPanel detailPanel;
	private DefaultComboBoxModel<String> listModelString, capaListModelString;
	private DefaultComboBoxModel<Integer> listModelInt;
	private JLabel titleLoc;
	private JComboBox<String> locField, capaLocField;
	private JCheckBox publicCheckBox;
	private JLabel titleFreq;
	private JComboBox<String> FreqField;
	private JLabel titleFreqAmount;
	private JComboBox<Integer> FreqAmountField;
	private JLabel labelEnd;
	private JPanel pRemind;
	private JLabel remindHour;
	private JTextField remindHF;
	private JLabel remindMinute;
	private JTextField remindMF;
	private JLabel remindSecond;
	private JTextField remindSF;
	/*
    private JTextField attendField;
	private JTextField rejectField;
	private JTextField waitingField;*/
	private int selectedApptId = -1;
	private int freqAmount = 0;
	private boolean isReminderToggled = false;
	private boolean automatic;


	// Group event ready means that a manual group event scheduling has been 
	// created if it is true
	private boolean groupEventReady = false;
	private ArrayList<Location> locations;


	private ArrayList<User> userChosenList;
	private ArrayList<TimeSpan> timeSlotChosen;
	private ArrayList<TimeSpan> dateChosenList;
	private ArrayList<Integer> duration;


	@SuppressWarnings("deprecation")
	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		userChosenList = new  ArrayList<User>();
		dateChosenList = new ArrayList<TimeSpan>();
		timeSlotChosen = new ArrayList<TimeSpan>();
		automatic = false;
		duration = new ArrayList<Integer>();
		duration.add(0);
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
					} else if (FreqField.getSelectedItem().equals(FreqStrings[2])) {
						titleFreqAmount.setText("Every");
						labelEnd.setText("week(s)");
					} else if (FreqField.getSelectedItem().equals(FreqStrings[3])) {
						titleFreqAmount.setText("Every");
						labelEnd.setText("month(s)");
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
		Border reminderBorder = new TitledBorder(null, "REMINDER");
		pRemind.setBorder(reminderBorder);

		reminderToggle = new JToggleButton("OFF");
		reminderToggle.setBorder(new BevelBorder(BevelBorder.RAISED));
		reminderToggle.setForeground(Color.BLUE);
		reminderToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (reminderToggle.isSelected()) {
					reminderToggle.setText("ON");
					reminderToggle.setBorder(new BevelBorder(BevelBorder.LOWERED));
					reminderToggle.setForeground(Color.RED);
					isReminderToggled = true;
					remindHF.setEnabled(true);
					remindMF.setEnabled(true);
					remindSF.setEnabled(true);
				} else {
					reminderToggle.setText("OFF");
					reminderToggle.setBorder(new BevelBorder(BevelBorder.RAISED));
					reminderToggle.setForeground(Color.BLUE);
					isReminderToggled = false;
					remindHF.setEnabled(false);
					remindMF.setEnabled(false);
					remindSF.setEnabled(false);
				}
			}
		});

		pRemind.add(reminderToggle);

		remindHour = new JLabel("HOUR");
		remindHF = new JTextField(4);
		remindMinute = new JLabel("MINUTE");
		remindMF = new JTextField(4);
		remindSecond = new JLabel("SECOND");;
		remindSF = new JTextField(4);

		pRemind.add(remindHour);
		pRemind.add(remindHF);
		pRemind.add(remindMinute);
		pRemind.add(remindMF);
		pRemind.add(remindSecond);
		pRemind.add(remindSF);

		remindHF.setEnabled(false);
		remindMF.setEnabled(false);
		remindSF.setEnabled(false);

		JPanel pTimeFreqRem = new JPanel();
		pTimeFreqRem.setLayout(new GridLayout(2,2));
		pTimeFreqRem.add(psTime);
		pTimeFreqRem.add(peTime);
		pTimeFreqRem.add(pFreq);
		pTimeFreqRem.add(pRemind);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTimeFreqRem, BorderLayout.CENTER);

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
		itemsPlusCapa = new Vector<String>();
		locations = cal.controller.getLocationList();
		if(locations == null){
			locations = new ArrayList<Location>();
			items.addElement("--EMPTY--");
		}
		else if(locations != null){
			items.clear();
			itemsPlusCapa.clear();
			for(int i=0; i<locations.size(); i++){
				items.addElement(locations.get(i).getName().toString());
				itemsPlusCapa.addElement(locations.get(i).getName().toString() + " (" + Integer.toString(locations.get(i).getCapacity()) + " )");
			}
		}
		listModelString = new DefaultComboBoxModel<String>(items);
		capaListModelString = new DefaultComboBoxModel<String>(itemsPlusCapa);
		locField = new JComboBox<String>(listModelString);
		capaLocField = new JComboBox<String>(capaListModelString);
		locField.setSelectedIndex(capaLocField.getSelectedIndex());
		titleAndTextPanel.add(capaLocField);

		publicCheckBox = new JCheckBox("PUBLIC");
		titleAndTextPanel.add(publicCheckBox);

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

		inviteBut = new JButton("Invite");
		inviteBut.addActionListener(this);
		panel2.add(inviteBut);

		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt();
		// TODO : MODIFY!!!
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
		Appt thisAppt = parent.controller.RetrieveAppts(selectedApptId);
		if (thisAppt.isJoint()) {
			titleFreq.setEnabled(false);
			FreqField.setEnabled(false);
			yearF.setEditable(false);
			monthF.setEditable(false);
			dayF.setEditable(false);
			sTimeH.setEditable(false);
			sTimeM.setEditable(false);
			eTimeH.setEditable(false);
			eTimeM.setEditable(false);

			ArrayList<UUID> allPeople = new ArrayList<UUID>(thisAppt.getAllPeople());
			for (UUID anID : allPeople) {
				userChosenList.add(parent.controller.searchUser(anID));
			}

			int year = thisAppt.TimeSpan().StartTime().getYear() + 1900;
			int month = thisAppt.TimeSpan().StartTime().getMonth() + 1;
			int date = thisAppt.TimeSpan().StartTime().getDate();
			Timestamp start = Timestamp.valueOf(year + "-" + month + "-" + date + " 08:00:00");
			Timestamp end= Timestamp.valueOf(year + "-" + month + "-" + date + " 18:00:00");
			TimeSpan ts = new TimeSpan(start, end);
			dateChosenList.add(ts);
		}
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
		rejectBut.setEnabled(false);
	}

	AppScheduler(String title, CalGrid cal, boolean automatic) {
		commonConstructor(title, cal);
		this.automatic = automatic;
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		rejectBut.setEnabled(false);
		FreqField.setEnabled(false);

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
				NewAppt.addReject(getCurrentUserUUID());
				NewAppt.getAttendList().remove(getCurrentUserUUID());
				NewAppt.getWaitingList().remove(getCurrentUserUUID());
				this.setVisible(false);
				dispose();
			}
		} else if (e.getSource() == inviteBut) {
			yearF.setEditable(false);
			monthF.setEditable(false);
			dayF.setEditable(false);
			sTimeH.setEditable(false);
			sTimeM.setEditable(false);
			eTimeH.setEditable(false);
			eTimeM.setEditable(false);
			FreqField.setEnabled(false);

			CreateGroupEvent inviteUserD;

			if (automatic) {
				inviteUserD = new CreateGroupEvent(parent, CreateGroupEvent.AUTOMATIC,
						userChosenList, dateChosenList, timeSlotChosen, duration);
			} else if (selectedApptId != -1 ) {
				inviteUserD = new CreateGroupEvent(parent, CreateGroupEvent.MANUAL,
						userChosenList, dateChosenList, timeSlotChosen, true);
			} else {
				inviteUserD = new CreateGroupEvent(parent, CreateGroupEvent.MANUAL,
						userChosenList, dateChosenList, timeSlotChosen, duration);
			}

			inviteUserD.setAlwaysOnTop(true);
			inviteUserD.setLocationRelativeTo(this);
			inviteUserD.addWindowListener(new WindowListener() {

				@Override
				public void windowActivated(WindowEvent e) {


				}

				@Override
				public void windowClosed(WindowEvent e) {
					// Is a group event
					if (!userChosenList.isEmpty() ) {
						yearF.setEditable(false);
						monthF.setEditable(false);
						dayF.setEditable(false);
						sTimeH.setEditable(false);
						sTimeM.setEditable(false);
						eTimeH.setEditable(false);
						eTimeM.setEditable(false);
						FreqField.setEnabled(false);
					} else {
						yearF.setEditable(true);
						monthF.setEditable(true);
						dayF.setEditable(true);
						sTimeH.setEditable(true);
						sTimeM.setEditable(true);
						eTimeH.setEditable(true);
						eTimeM.setEditable(true);
						FreqField.setEnabled(true);
					}

				}

				@Override
				public void windowClosing(WindowEvent e) {
					if (!automatic) {
						yearF.setEditable(true);
						monthF.setEditable(true);
						dayF.setEditable(true);
						sTimeH.setEditable(true);
						sTimeM.setEditable(true);
						eTimeH.setEditable(true);
						eTimeM.setEditable(true);
						FreqField.setEnabled(true);
					}
				}

				@Override
				public void windowDeactivated(WindowEvent e) {

				}

				@Override
				public void windowDeiconified(WindowEvent e) {

				}

				@Override
				public void windowIconified(WindowEvent e) {

				}

				@Override
				public void windowOpened(WindowEvent e) {
					yearF.setEditable(false);
					monthF.setEditable(false);
					dayF.setEditable(false);
					sTimeH.setEditable(false);
					sTimeM.setEditable(false);
					eTimeH.setEditable(false);
					eTimeM.setEditable(false);
					FreqField.setEnabled(false);
				}

			});
		}

		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetTodayAppt());
		parent.repaint();
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

		long RemindH = Long.parseLong(remindHF.getText());
		long RemindM = Long.parseLong(remindMF.getText());
		long RemindS = Long.parseLong(remindSF.getText());
		locField.setSelectedIndex(capaLocField.getSelectedIndex());
		String location = (String) locField.getSelectedItem();
		// Manual Group scheduling
		if (!timeSlotChosen.isEmpty() || (selectedApptId != -1 && !userChosenList.isEmpty())){
			if(!parent.controller.checkDuplicateLocation(location, timeSlotChosen.get(0))){
				groupEventReady = false;
				JOptionPane.showMessageDialog(this, location + " had been chosen. Please choose another location", "Warning",  JOptionPane.WARNING_MESSAGE);
			}
			else
				groupEventReady = true;

		}
		else
			// Either it is automatic group scheduling or manual private scheduling
			groupEventReady = false;

		if (groupEventReady) {
			System.out.println("Group creation succesful");
			String title = titleField.getText();
			String desc = detailArea.getText();
			//locField.setSelectedIndex(capaLocField.getSelectedIndex());
			//String location = (String) locField.getSelectedItem();

			if (selectedApptId == -1) {
				NewAppt.setTitle(title);
				NewAppt.setInfo(desc);
				NewAppt.setJoint(true);
				NewAppt.setTimeSpan(timeSlotChosen.get(0));
				NewAppt.setLocation(location, locations.get(locField.getSelectedIndex()).getCapacity());
				NewAppt.setPublic(publicCheckBox.isSelected());

				NewAppt.addWaiting(parent.controller.getCurrentUser().getUserId());

				for (User user : userChosenList) {
					NewAppt.addWaiting(user.getUserId());
					parent.controller.addRequest(new Request(parent.controller.getCurrentUser(), user, Request.type.INVITE, NewAppt));
				}

				// TODO how about reminder
				NewAppt.reminderOn(reminderToggle.isSelected());
				NewAppt.setRemindBefore(RemindH * 3600000 + RemindM * 60000 + RemindS * 1000 );

				NewAppt.setInitiator(parent.controller.getCurrentUser());
				parent.controller.ManageAppt(NewAppt, ApptStorageControllerImpl.NEW);
			}
			else {
				Appt modifiedAppt = new Appt();
				modifiedAppt.setID(selectedApptId);

				Appt oldAppt = parent.controller.RetrieveAppts(selectedApptId);

				int joinID = oldAppt.getJoinID();
				modifiedAppt.setJoinID(joinID);
				modifiedAppt.setTitle(title);
				modifiedAppt.setInfo(desc);
				modifiedAppt.setJoint(true);
				if (timeSlotChosen.isEmpty()) {
					JOptionPane.showMessageDialog(this,"Please reselect timeslot", "Warning",  JOptionPane.WARNING_MESSAGE);
					return;
				}
				modifiedAppt.setTimeSpan(timeSlotChosen.get(0));
				modifiedAppt.setLocation(location, locations.get(locField.getSelectedIndex()).getCapacity());
				modifiedAppt.setPublic(publicCheckBox.isSelected());
				// Put current user to waiting too?
				modifiedAppt.addWaiting(parent.controller.getCurrentUser().getUserId());

				for (User user : userChosenList) {
					modifiedAppt.addWaiting(user.getUserId());
				}

				// TODO how about reminder
				modifiedAppt.reminderOn(reminderToggle.isSelected());
				modifiedAppt.setRemindBefore(RemindH * 3600000 + RemindM * 60000 + RemindS * 1000 );

				modifiedAppt.setInitiator(parent.controller.getCurrentUser());
				parent.controller.ManageAppt(modifiedAppt, ApptStorageControllerImpl.MODIFY);
			}

		} else {
			if (!dateChosenList.isEmpty()) {
				String title = titleField.getText();
				String desc = detailArea.getText();
				locField.setSelectedIndex(capaLocField.getSelectedIndex());
				location = (String) locField.getSelectedItem();

				// TODO: Send request to users with the timeslots
				System.out.println("Sending request and timeslots to user....");
				for (User user : userChosenList) {
					System.out.println("Creating a request now");
					// For this requests for a chunk of users, we have to have the same id to indicate to a same potential appt.
					Request req =  new Request(parent.controller.getCurrentUser(), user, 
							Request.type.INVITE, dateChosenList, duration.get(0), 
							parent.controller.getCountFeedbackRequest()+1);
					req.setTitle(title);
					req.setDesc(desc);
					req.addParticipant(user.getUserId());
					Location loc = new Location(location, locations.get(locField.getSelectedIndex()).getCapacity());
					req.setLocation(loc);
					parent.controller.addRequest(req);
				}
				parent.controller.setCountFeedbackRequest(parent.controller.getCountFeedbackRequest() + 1);
				parent.controller.SaveFeedbackCountToXml();
			} else {
				/** MANUAL INDIVIDUAL SCHEDULING **/
				// Save the appointment to the hard disk
				/* Assign information to the newly created appointment. */
				int[] date = getValidDate(); // date[0] refers to year, date[1] refers to month, date[2] refers to day
				int[] time = getValidTimeInterval(); // time[0] refers to start time, time[1] refers to end time


				Timestamp timeStart = CreateTimeStamp(date, time[0]);
				Timestamp timeEnd = CreateTimeStamp(date,time[1]);
				TimeSpan span = new TimeSpan(timeStart, timeEnd);




				if (reminderToggle.isSelected()) {
					if (remindHF.getText().isEmpty() || remindMF.getText().isEmpty() || remindSF.getText().isEmpty()) {
						JOptionPane.showMessageDialog(this, "Don't leave the fields in Reminder empty", "Warning",  JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				freqAmount = FreqAmountField.getSelectedIndex() + 1;
				if(parent.controller.checkDuplicateLocation(location, span)){
					// Check if there is no appointment selected in the appointment list (via Manual Scheduling)
					if (selectedApptId == -1) {
						/* Save the appointment to the hard disk (AppStorageController to ApptStorage) */
						if (FreqField.getSelectedItem().equals("Once")) {
							addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.NEW);
						} else if (FreqField.getSelectedItem().equals("Daily")) {
							addAppt(date, time, Appt.MODE_DAILY, ApptStorageControllerImpl.NEW);
						} else if (FreqField.getSelectedItem().equals("Weekly")) {
							addAppt(date, time, Appt.MODE_WEEKLY, ApptStorageControllerImpl.NEW);
						} else if (FreqField.getSelectedItem().equals("Monthly")) {
							addAppt(date, time, Appt.MODE_MONTHLY, ApptStorageControllerImpl.NEW);
						}
					} 

					// TODO FREQUENCY UPDATED, PLEASE READ THE NOTE (ESP. MICHELLE)
					/* THIS PART IS TRICKY. WE CANNOT JUST IMPLEMENT APPTSTORAGECONTROLLER.NEW BECAUSE
					 * IF WE DO THAT, THE VERY INITIAL APPOINTMENT THAT WE CHOOSE TO BE MODIFIED CANNOT BE MODIFIED AS WELL.
					 * YOU CAN TRY TO COMMENT OUT THE METHOD addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.MODIFY) 
					 * FROM ONE OF THE FREQUENCY MODE BELOW AND TRY MODIFYING AN APPT BY ITS TIME AND FREQUENCY. THE RESULT WILL
					 * SHOW THAT THE APPT WILL BE PRODUCED WITH THE APPROPRIATE FREQUENCY AND TIME BUT THE VERY FIRST APPT IS NOT MODIFIED
					 * BY ITS TIME. HENCE WE NEED TO DO addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.MODIFY) FIRST*/

					else { // if appointment tile is selected from AppList (via right click)
						if (FreqField.getSelectedItem().equals("Once")) {
							addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.MODIFY);

						} else if (FreqField.getSelectedItem().equals("Daily")) {
							// Add the following if user's modification include either: start time, end time, location, title, desc, reminder
							addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.MODIFY);

							// Add the following if user's modification only include the frequency part
							addAppt(date, time, Appt.MODE_DAILY, ApptStorageControllerImpl.NEW);

						} else if (FreqField.getSelectedItem().equals("Weekly")) {
							// Add the following if user's modification include either: start time, end time, location, title, desc, reminder
							addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.MODIFY);

							// Add the following if user's modification only include the frequency part
							addAppt(date, time, Appt.MODE_WEEKLY, ApptStorageControllerImpl.NEW);

						} else if (FreqField.getSelectedItem().equals("Monthly")) {
							// Add the following if user's modification include either: start time, end time, location, title, desc, reminder
							addAppt(date, time, Appt.MODE_ONCE, ApptStorageControllerImpl.MODIFY);

							// Add the following if user's modification only include the frequency part
							addAppt(date, time, Appt.MODE_MONTHLY, ApptStorageControllerImpl.NEW);
						}

						selectedApptId = -1;
					}
				}
				else
					JOptionPane.showMessageDialog(this, location + " had been chosen. Please choose another location", "Warning",  JOptionPane.WARNING_MESSAGE);


			}
		}
		parent.loadSearchBoxList();
		setVisible(false);
		dispose();
	}


	private void addAppt(int[] date, int[] time, int mode, int action) {
		String title = titleField.getText();
		String info = detailArea.getText();
		locField.setSelectedIndex(capaLocField.getSelectedIndex());
		String location = (String) locField.getSelectedItem();
		long RemindH = Long.parseLong(remindHF.getText());
		long RemindM = Long.parseLong(remindMF.getText());
		long RemindS = Long.parseLong(remindSF.getText());

		if (mode == Appt.MODE_ONCE) {
			Timestamp stampStart = CreateTimeStamp(date,time[0]);
			Timestamp stampEnd = CreateTimeStamp(date, time[1]);
			TimeSpan timeSpan = new TimeSpan(stampStart, stampEnd);

			if (selectedApptId != -1) {
				NewAppt.setID(selectedApptId);
			}
			NewAppt.setTimeSpan(timeSpan);
			NewAppt.setTitle(title);
			NewAppt.setInfo(info);
			NewAppt.setLocation(location, locations.get(locField.getSelectedIndex()).getCapacity());
			NewAppt.setFrequencyAmount(freqAmount);
			NewAppt.reminderOn(reminderToggle.isSelected());
			NewAppt.setPublic(publicCheckBox.isSelected());
			NewAppt.setRemindBefore(RemindH * 3600000 + RemindM * 60000 + RemindS * 1000);
			NewAppt.setInitiator(parent.controller.getCurrentUser());

			parent.controller.ManageAppt(NewAppt, action);

			if (parent.controller.isOverlap()) {
				JOptionPane.showMessageDialog(this, parent.controller.getOverlapMessage(), "Warning",  JOptionPane.WARNING_MESSAGE);
			}

		} else if (mode == Appt.MODE_DAILY) {
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
				appt.setLocation(location, locations.get(locField.getSelectedIndex()).getCapacity());
				appt.setFrequencyAmount(freqAmount);
				appt.reminderOn(reminderToggle.isSelected());
				appt.setPublic(publicCheckBox.isSelected());
				appt.setRemindBefore(RemindH * 3600000 + RemindM * 60000 + RemindS * 1000 );
				appt.setInitiator(parent.controller.getCurrentUser());

				parent.controller.ManageAppt(appt, action);

				if (parent.controller.isOverlap()) {
					JOptionPane.showMessageDialog(this, parent.controller.getOverlapMessage(), "Warning",  JOptionPane.WARNING_MESSAGE);
				}

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
		} else if (mode == Appt.MODE_WEEKLY) {
			for (int i = 0; i < freqAmount; i++) {
				Timestamp stampStart = CreateTimeStamp(date,time[0]);
				Timestamp stampEnd = CreateTimeStamp(date, time[1]);
				TimeSpan timeSpan = new TimeSpan(stampStart, stampEnd);

				Appt appt = new Appt();
				appt.setTimeSpan(timeSpan);
				appt.setTitle(title);
				appt.setInfo(info);
				appt.setLocation(location, locations.get(locField.getSelectedIndex()).getCapacity());
				appt.setFrequencyAmount(freqAmount);
				appt.reminderOn(reminderToggle.isSelected());
				appt.setPublic(publicCheckBox.isSelected());
				appt.setRemindBefore(RemindH * 3600000 + RemindM * 60000 + RemindS * 1000 );
				appt.setInitiator(parent.controller.getCurrentUser());

				parent.controller.ManageAppt(appt, action);

				if (parent.controller.isOverlap()) {
					JOptionPane.showMessageDialog(this, parent.controller.getOverlapMessage(), "Warning",  JOptionPane.WARNING_MESSAGE);
				}


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
		} else if (mode == Appt.MODE_MONTHLY) {
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
					appt.setLocation(location, locations.get(locField.getSelectedIndex()).getCapacity());
					appt.setFrequencyAmount(freqAmount);
					appt.reminderOn(reminderToggle.isSelected());
					appt.setPublic(publicCheckBox.isSelected());
					appt.setRemindBefore(RemindH * 3600000 + RemindM * 60000 + RemindS * 1000 );
					appt.setInitiator(parent.controller.getCurrentUser());

					parent.controller.ManageAppt(appt, action);

					if (parent.controller.isOverlap()) {
						JOptionPane.showMessageDialog(this, parent.controller.getOverlapMessage(), "Warning",  JOptionPane.WARNING_MESSAGE);
					}


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
		String locationText = appt.getLocationString();
		for (int i = 0; i < parent.controller.getLocationCapacity(); i++) {
			if (locationText.equals(locField.getItemAt(i)))
				locField.setSelectedIndex(i);		
		}

		reminderToggle.setSelected(appt.reminder());
		// Action listener does not change the gui state, so retype the following
		if (reminderToggle.isSelected()) {
			reminderToggle.setText("ON");
			reminderToggle.setBorder(new BevelBorder(BevelBorder.LOWERED));
			reminderToggle.setForeground(Color.RED);
			isReminderToggled = true;	
			isReminderToggled = true;
			remindHF.setEnabled(true);
			remindMF.setEnabled(true);
			remindSF.setEnabled(true);

		} else {
			reminderToggle.setText("OFF");
			reminderToggle.setBorder(new BevelBorder(BevelBorder.RAISED));
			reminderToggle.setForeground(Color.BLUE);
			isReminderToggled = false;isReminderToggled = true;
			remindHF.setEnabled(false);
			remindMF.setEnabled(false);
			remindSF.setEnabled(false);
		}

		long remindDuration = appt.getRemindBefore();
		long hour = remindDuration / 3600000;
		remindDuration %= 3600000;

		long minute = remindDuration / 60000;
		remindDuration %= 60000;

		long second = remindDuration / 1000;

		remindHF.setText(Long.toString(hour));
		remindMF.setText(Long.toString(minute));
		remindSF.setText(Long.toString(second));

		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
		publicCheckBox.setSelected(appt.isPublic());

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

	public UUID getCurrentUserUUID()		// get the id of the current user
	{
		return this.parent.mCurrUser.getUserId();
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
