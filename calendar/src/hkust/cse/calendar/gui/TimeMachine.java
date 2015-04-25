package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.CalendarClock;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import hkust.cse.calendar.unit.ClockListeners;

public class TimeMachine extends JFrame implements ActionListener, ClockListeners {

	// Dialog items
	private CalGrid parent;
	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JComboBox <String> monthB;
	private int monthInt;
	private JLabel dateL;
	private JComboBox <Integer> dateB;
	private int dateInt;
	private JLabel startTimeHL;
	private JTextField startTimeH;
	private JLabel startTimeML;
	private JTextField startTimeM;
	private JLabel startTimeSL;
	private JTextField startTimeS;
	private JLabel timeStepHourL;
	private JTextField timeStepHour;
	private JLabel timeStepMinuteL;
	private JTextField timeStepMinute;
	private JLabel timeStepSecondL;
	private JTextField timeStepSecond;
	private JLabel currTime;

	// Dialog buttons
	private JButton fastFBut;
	private JButton rewindBut;
	private JButton resumeBut;
	private JButton stopBut;
	private JButton resetBut;

	// Calendar object
	private GregorianCalendar currToday;
	private SimpleDateFormat dateFormat;

	// Constant array for combo box month & day
	private final int[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, 28, 29 ,30, 31};
	private final String[] months = { "January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	
	private CalendarClock CalClock;

	// Default constructor
	TimeMachine(String title, CalGrid cal, CalendarClock clock) {
		commonConstructor(title, cal, clock);
	}

	private void commonConstructor(String title, CalGrid cal, CalendarClock clock) {

		// Initialization
		CalClock = clock;
		clock.runningClockListener(this);
		
		parent = cal;
		this.setAlwaysOnTop(false);
		setTitle(title);
		Container contentPane;
		contentPane = getContentPane();
		currToday = new GregorianCalendar();
		dateFormat = new SimpleDateFormat("yyyy-MM-dd    kk:mm:ss");

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
		monthB = new JComboBox<String>();
		monthB.addActionListener(this);
		monthB.setPreferredSize(new Dimension(100, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			monthB.addItem(months[cnt]);
		datePanel.add(monthB);

		// Day
		dateL = new JLabel("DAY: ");
		datePanel.add(dateL);
		dateB = new JComboBox<Integer>();
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
		
		// Start Second
		startTimeSL = new JLabel("Second");
		timePanel.add(startTimeSL);
		startTimeS = new JTextField(4);
		timePanel.add(startTimeS);

		JPanel psTime = new JPanel();
		psTime.setLayout(new BorderLayout());
		psTime.add(timePanel, BorderLayout.WEST);

		// Time step panel
		JPanel timeStepPanel = new JPanel();
		Border timestepBorder = new TitledBorder(null, "TIME STEP");
		timeStepPanel.setBorder(timestepBorder);
		
		timeStepHourL = new JLabel("Hour: ");
		timeStepPanel.add(timeStepHourL);
		timeStepHour = new JTextField(4);
		timeStepPanel.add(timeStepHour);
		
		timeStepMinuteL = new JLabel("Minute: ");
		timeStepPanel.add(timeStepMinuteL);
		timeStepMinute = new JTextField(4);
		timeStepPanel.add(timeStepMinute);

		timeStepSecondL = new JLabel("Second: ");
		timeStepPanel.add(timeStepSecondL);
		timeStepSecond = new JTextField(4);
		timeStepPanel.add(timeStepSecond);

		psTime.add(timeStepPanel);

		// Current time
		currTime = new JLabel(dateFormat.format(currToday.getTime()));
		currTime.setFont(new Font("", Font.BOLD, 48));
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
		monthB.setSelectedIndex(parent.currentM - 1);
		dateB.setSelectedIndex(parent.currentD - 1);
		startTimeH.setText("8");
		startTimeM.setText("0");
		startTimeS.setText("0");
		timeStepHour.setText("0");
		timeStepMinute.setText("0");
		timeStepSecond.setText("1");

		// Save and cancel
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.CENTER));

		fastFBut = new JButton("Fast Forward");
		fastFBut.addActionListener(this);
		panel2.add(fastFBut);

		rewindBut = new JButton("Rewind");
		rewindBut.addActionListener(this);
		rewindBut.setEnabled(false);
		panel2.add(rewindBut);

		resumeBut = new JButton("Resume");
		resumeBut.addActionListener(this);
		resumeBut.setEnabled(false);
		panel2.add(resumeBut);

		stopBut = new JButton("Stop");
		stopBut.addActionListener(this);
		stopBut.setEnabled(false);
		panel2.add(stopBut);

		resetBut = new JButton("Reset");
		resetBut.addActionListener(this);
		resetBut.setEnabled(false);
		panel2.add(resetBut);

		if (CalClock.isStart()) {
			fastFBut.setEnabled(false);
			rewindBut.setEnabled(true);
			stopBut.setEnabled(true);
			resumeBut.setEnabled(false);
			resetBut.setEnabled(true);
		}
		
		contentPane.add("South", panel2);
		pack();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// distinguish which button is clicked and continue with require function
		if (e.getSource() == monthB) {
			if (monthB.getSelectedItem() != null)
				monthInt = monthB.getSelectedIndex();
		} else if (e.getSource() == dateB) {
			if (dateB.getSelectedItem() != null)
				dateInt = dateB.getSelectedIndex() + 1;
			
		} else if (e.getSource() == fastFBut) {
		
			// Run functions
			CalClock.setStartTime(new Timestamp(Integer.parseInt(yearF.getText()) - 1900, monthInt, dateInt, Integer.parseInt(startTimeH.getText()), Integer.parseInt(startTimeM.getText()), Integer.parseInt(startTimeS.getText()), 0));
			if (timeStepHour.getText().isEmpty() || timeStepMinute.getText().isEmpty() || timeStepSecond.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Empty fields for time steps. Set 0 to unused field", "Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else {
				CalClock.setDelay(Integer.parseInt(timeStepHour.getText())* 3600000 + 
						Integer.parseInt(timeStepMinute.getText()) * 60000 + 
						Integer.parseInt(timeStepSecond.getText()) * 1000);
			}
			// Start clock
			CalClock.start();
			// Set buttons
			fastFBut.setEnabled(false);
			rewindBut.setEnabled(true);
			stopBut.setEnabled(true);
			resumeBut.setEnabled(false);
			resetBut.setEnabled(true);
			
		} else if (e.getSource() == rewindBut){

		    // If field for delay is empty, just increment 1 minute
			if (timeStepHour.getText().isEmpty() || timeStepMinute.getText().isEmpty() || timeStepSecond.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Empty fields for time steps. Set 0 to unused field", "Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else {
				CalClock.setDelay(Integer.parseInt(timeStepHour.getText())* 3600000 + 
						Integer.parseInt(timeStepMinute.getText()) * 60000 + 
						Integer.parseInt(timeStepSecond.getText()) * 1000);
			}
			// Rewind clock
			CalClock.rewind();
			
			// Set buttons
			resumeBut.setEnabled(false);
			rewindBut.setEnabled(false);
			resetBut.setEnabled(true);
			stopBut.setEnabled(true);
			fastFBut.setEnabled(false);
		
		} else if (e.getSource() == resumeBut){
			
			// If field for delay is empty, just increment 1 minute
			if (timeStepHour.getText().isEmpty() || timeStepMinute.getText().isEmpty() || timeStepSecond.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Empty fields for time steps. Set 0 to unused field", "Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			else {
				CalClock.setDelay(Integer.parseInt(timeStepHour.getText())* 3600000 + 
						Integer.parseInt(timeStepMinute.getText()) * 60000 + 
						Integer.parseInt(timeStepSecond.getText()) * 1000);
			}
			
			// Resume clock
			CalClock.resume();
			

			// Set buttons
			resumeBut.setEnabled(false);
			rewindBut.setEnabled(false);
			resetBut.setEnabled(false);
			fastFBut.setEnabled(false);
			stopBut.setEnabled(true);

		} else if (e.getSource() == stopBut){

			// Set buttons
			fastFBut.setEnabled(true);
			resumeBut.setEnabled(true);
			rewindBut.setEnabled(true);
			resetBut.setEnabled(true);
			
			// Stop the clock
			CalClock.stop();

		} else if (e.getSource() == resetBut){
			// Set buttons
			resumeBut.setEnabled(false);
			rewindBut.setEnabled(false);
			resetBut.setEnabled(false);
			stopBut.setEnabled(true);
			
			// Stop and reset clock
			CalClock.stop();
			CalClock.reset();
			
			// Update display
			setTimeFromField();
			updateDisplayTime();
		}
	}


	private void updateDisplayTime(){
		// Update display
		currTime.setText(dateFormat.format(currToday.getTime()));
	}

	// Set the time retrieved from the text fields
	private void setTimeFromField(){
		int year = Utility.getNumber(yearF.getText());
		int month = monthInt;
		int date = dateInt;
		int hour = Utility.getNumber(startTimeH.getText());
		int min = Utility.getNumber(startTimeM.getText());
		int sec = Utility.getNumber(startTimeS.getText());

		currToday.set(year, month, date, hour, min, sec);
	}

	// Handle the tick for each clock by displaying it in the display
	@Override
	public void timeIsElapsing(CalendarClock emitter) {
		currTime.setText(emitter.toString());
	}	
}
