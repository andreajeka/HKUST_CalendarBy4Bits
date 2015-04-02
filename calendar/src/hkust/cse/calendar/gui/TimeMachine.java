package hkust.cse.calendar.gui;



import hkust.cse.calendar.unit.Appt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

public class TimeMachine extends JDialog implements ActionListener {

	// Dialog items
	private CalGrid parent;
	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JComboBox monthB;
	private int monthInt;
	private JLabel dateL;
	private JComboBox dateB;
	private int dateInt;
	private JLabel startTimeHL;
	private JTextField startTimeH;
	private JLabel startTimeML;
	private JTextField startTimeM;
	private JLabel endTimeHL;
	private JTextField endTimeH;
	private JLabel endTimeML;
	private JTextField endTimeM;
	private JLabel timeStepL;
	private JTextField timeStep;
	private JLabel currTime;

	// Dialog buttons
	private JButton startBut;
	private JButton cancelBut;
	private JButton fastBut;
	private JButton rewindBut;
	private JButton stopBut;

	// Calendar object
	private GregorianCalendar currToday, todayStamp;
	private SimpleDateFormat dateFormat;

	//appt array
	private Appt[] mAppt;
	private Date tempDate;
	private GregorianCalendar tempCal;
	private GregorianCalendar diffCal;
	private Date diffDate;

	// Constant array for combobox month & day
	private final int[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, 28, 29 ,30, 31};
	private final String[] months = { "January", "Feburary", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };

	// Flags and helpers
	private boolean incrementFlag;
	private int incrementStep;
	private Timer timer;


	// Default constructor
	TimeMachine(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}

	private void commonConstructor(String title, CalGrid cal) {

		// Initialization
		parent = cal;
		this.setAlwaysOnTop(false);
		setTitle(title);
		setModal(false);
		Container contentPane;
		contentPane = getContentPane();
		currToday = new GregorianCalendar();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm");
		incrementFlag = true;

		// Date panel
		JPanel datePanel = new JPanel();
		Border dateBorder = new TitledBorder(null, "START DATE");
		datePanel.setBorder(dateBorder);

		// Year
		yearL = new JLabel("YEAR: ");
		datePanel.add(yearL);
		yearF = new JTextField(6);
		datePanel.add(yearF);

		// Month
		monthL = new JLabel("Month: ");
		datePanel.add(monthL);
		monthB = new JComboBox();
		monthB.addActionListener(this);
		monthB.setPreferredSize(new Dimension(100, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			monthB.addItem(months[cnt]);
		datePanel.add(monthB);

		// Day
		dateL = new JLabel("DAY: ");
		datePanel.add(dateL);
		dateB = new JComboBox();
		dateB.addActionListener(this);
		dateB.setPreferredSize(new Dimension(50, 30));
		for (int cnt = 0; cnt < 31; cnt++)
			dateB.addItem(days[cnt]);
		datePanel.add(dateB);

		// Time panel
		JPanel timePanel = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		timePanel.setBorder(stimeBorder);

		// Start Hour
		startTimeHL = new JLabel("Hour");
		timePanel.add(startTimeHL);
		startTimeH = new JTextField(4);
		timePanel.add(startTimeH);

		// Start Minute
		startTimeML = new JLabel("Minute");
		timePanel.add(startTimeML);
		startTimeM = new JTextField(4);
		timePanel.add(startTimeM);

		JPanel psTime = new JPanel();
		psTime.setLayout(new BorderLayout());
		psTime.add(timePanel, BorderLayout.WEST);

		// Time step panel
		JPanel timeStepPanel = new JPanel();
		Border timestepBorder = new TitledBorder(null, "TIME STEP");
		timeStepPanel.setBorder(timestepBorder);
		timeStepL = new JLabel("Minute/second: ");
		timeStepPanel.add(timeStepL);
		timeStep = new JTextField(4);
		timeStepPanel.add(timeStep);

		psTime.add(timeStepPanel);

		// Current time
		currTime = new JLabel(dateFormat.format(currToday.getTime()));
		JPanel currTimePanel = new JPanel();
		currTimePanel.add(currTime, BorderLayout.CENTER);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(datePanel, BorderLayout.NORTH);
		top.add(psTime, BorderLayout.CENTER);
		top.add(currTimePanel, BorderLayout.SOUTH);
		contentPane.add("North", top);

		// Set default values
		yearF.setText(Integer.toString(parent.currentY));
		startTimeH.setText("12");
		startTimeM.setText("0");

		// Save and cancel
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

		startBut = new JButton("Start");
		startBut.addActionListener(this);
		panel2.add(startBut);

		cancelBut = new JButton("Cancel");
		cancelBut.addActionListener(this);
		panel2.add(cancelBut);

		fastBut = new JButton("Fast Forward");
		fastBut.addActionListener(this);
		fastBut.setEnabled(false);
		panel2.add(fastBut);

		rewindBut = new JButton("Rewind");
		rewindBut.addActionListener(this);
		rewindBut.setEnabled(false);
		panel2.add(rewindBut);

		stopBut = new JButton("Stop");
		stopBut.addActionListener(this);
		stopBut.setEnabled(false);
		panel2.add(stopBut);

		contentPane.add("South", panel2);

		pack();

		// retrieve appts from cal
		mAppt = cal.controller.RetrieveAllAppts();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// distinguish which button is clicked and continue with require function
		if (e.getSource() == monthB) {
			if (monthB.getSelectedItem() != null)
				monthInt = monthB.getSelectedIndex();
		} else if (e.getSource() == dateB) {
			if (dateB.getSelectedItem() != null)
				dateInt = dateB.getSelectedIndex() + 1;
		} else if (e.getSource() == cancelBut) {

			// Close the dialog
			incrementFlag = false;
			setVisible(false);
			dispose();

		} else if (e.getSource() == startBut) {

			// Set buttons
			stopBut.setEnabled(true);
			fastBut.setEnabled(true);
			rewindBut.setEnabled(true);
			startBut.setEnabled(false);

			// Save and update
			saveButtonResponse();
			parent.setToday(currToday);
			updateDisplayTime();
			
			diffDate = new Date(Utility.getNumber(yearF.getText())-1900, monthInt, dateInt, 
					Utility.getNumber(startTimeH.getText()), Utility.getNumber(startTimeM.getText()));
			
			long diff;
			GregorianCalendar diffG, diffD;
			for(int i=0; i<mAppt.length; i++){
				diffG = new GregorianCalendar();
				diffD = new GregorianCalendar();
				diffG.setTime(mAppt[i].getReminder());
				diffD.setTime(diffDate);
				diff = diffD.getTimeInMillis() - System.currentTimeMillis();
				if(diff>0)
				diffG.add(Calendar.MINUTE, -(int)diff/(60*1000));
				else if(diff<0)
					diffG.add(Calendar.MINUTE, (int)diff/(60*1000));
				mAppt[i].setTempReminder(diffG.getTime());
				
				
			//	System.out.println(diffG.getTime());
			//	System.out.println(diffD.getTime());
				System.out.println(diff);
				
			}

		} else if (e.getSource() == stopBut){

			incrementFlag = false;

			// Set buttons
			startBut.setEnabled(true);
			fastBut.setEnabled(false);
			rewindBut.setEnabled(false);
			
			//reset all timer to original
			for(int i=0; i<mAppt.length; i++){
				mAppt[i].resetEveryTimer();
			}

		} else if (e.getSource() == fastBut){

			// Set buttons
			fastBut.setEnabled(false);
			rewindBut.setEnabled(true);

			// Set timer to increase time
			incrementStep = Utility.getNumber(timeStep.getText());
			timer = new Timer();
			timer.schedule(new incrementTimeTask(), 1000);

			// Update
			updateDisplayTime();

		} else if (e.getSource() == rewindBut){

			// Set buttons
			fastBut.setEnabled(true);
			rewindBut.setEnabled(false);

			incrementStep = - Utility.getNumber(timeStep.getText());
		}
	}

	private class incrementTimeTask extends TimerTask{
		@Override
		public void run(){

			currToday.add(Calendar.MINUTE, incrementStep);
			updateDisplayTime();
			parent.setToday(currToday);

			// Repeatedly increment until stop
			if (incrementFlag){
				Timer timer = new Timer();
				timer.schedule(new incrementTimeTask(), 1000);
			}

			// update all the timers
			
			diffCal = new GregorianCalendar();
	
			
			//System.out.println("time machine start date: " + diffDate);
			if(mAppt != null){
				tempCal = new GregorianCalendar();
				for(int i=0; i<mAppt.length; i++){
					tempCal.setTime(mAppt[i].getTempReminder());
					tempCal.add(Calendar.MINUTE, -incrementStep);
					tempDate = tempCal.getTime();
					if(mAppt[i].checkTimerLife()){
						mAppt[i].setTempReminder(tempDate);
						mAppt[i].resetTimer(tempDate);
						//System.out.println(mAppt[i].checkTimerLife());
					}
				}
			}
		}
	}

	private void updateDisplayTime(){
		// Update draw
		currTime.setText(dateFormat.format(currToday.getTime()));
	}

	private void saveButtonResponse(){
		int year = Utility.getNumber(yearF.getText());
		int month = monthInt;
		int date = dateInt;
		int hour = Utility.getNumber(startTimeH.getText());
		int min = Utility.getNumber(startTimeM.getText());

		currToday.set(year, month, date, hour, min);
	}
}
