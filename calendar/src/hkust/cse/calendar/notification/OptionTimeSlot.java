package hkust.cse.calendar.notification;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.gui.CreateGroupEvent;
import hkust.cse.calendar.gui.Utility;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

public class OptionTimeSlot extends Notification {
	
	private ApptStorageControllerImpl controller;
	private ArrayList<TimeSpan> datesChosenList;
	private ArrayList<TimeSpan> timeInTheList;
	private ArrayList<TimeSpan> userFeedbackDates;
	private int durationMins;
	private User currUser;
	private boolean confirm;
	
	public OptionTimeSlot(String title, String msg, ApptStorageControllerImpl controller, User entity,
			ArrayList<TimeSpan> datesChosenList, int durationMins) {
		super(title, msg);
		this.datesChosenList = datesChosenList;
		this.controller = controller;
		this.currUser = entity;
		this.durationMins = durationMins;
		timeInTheList = new ArrayList<TimeSpan>();
		userFeedbackDates = new ArrayList<TimeSpan>();
	}
	
	@Override
	public boolean popUp() {
		return true;
	}

	public void setWindow() {
		JFrame frame = new JFrame();
		Container pane = frame.getContentPane();
		frame.setTitle(_title);
		frame.setPreferredSize(new Dimension(400, 390));

		pane.add(_panel);
		_panel.setLayout(new BoxLayout(_panel,BoxLayout.Y_AXIS));
		TitledBorder titledBorder = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Choose Time Slot:");
		_panel.setBorder(titledBorder);
		
		JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel desc = new JLabel(_message);
		labelPanel.add(desc);
		_panel.add(labelPanel);
		
		DefaultListModel<String> timeListModel = new DefaultListModel<String>();
		JList<String> timeList = new JList<String>(timeListModel);
		timeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		timeList.setLayoutOrientation(JList.VERTICAL);
		timeList.setVisibleRowCount(8);
		JScrollPane timeListPane = new JScrollPane(timeList);
		timeListPane.setPreferredSize(new Dimension(300,380));
		for (int i = 0; i < datesChosenList.size(); i++) {
			ArrayList<TimeSpan> timeSlots = controller.RetrieveAvailTimeSpans(currUser, datesChosenList.get(i));
			if (!timeSlots.isEmpty()) {
			for (int j = 0; j < timeSlots.size(); j++) {
				// Display date
				int date = datesChosenList.get(i).StartTime().getDate();
				int year = datesChosenList.get(i).StartTime().getYear() + 1900;
				int Sminutes = timeSlots.get(j).StartTime().getMinutes();
				int Eminutes = timeSlots.get(j).EndTime().getMinutes();
				String Smins = Sminutes + "";
				String Emins = Eminutes + "";
				if (Sminutes < 10) 
					Smins = "0" + Sminutes;
				
				if (Eminutes < 10) 
					Emins = "0" + Eminutes;
				
				// Add the information of each timeslot to the timeList combo box
				timeListModel.addElement( date + " " + CalGrid.months[datesChosenList.get(i).StartTime().getMonth()] + " " + year  
						+ "  " +
						// Display time
						timeSlots.get(j).StartTime().getHours() + ":" + Smins + " to " + 
						timeSlots.get(j).EndTime().getHours() + ":" +  Emins);
	
				// Also add the corresponding TimeSpan element to the timeInTheList array to ease mapping
				timeInTheList.add(timeSlots.get(j));
			}
			}
		}
		_panel.add(timeListPane);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Collect the selected indices
				int[] selectedIndices = timeList.getSelectedIndices();
				
				if (selectedIndices.length == 0) {
					JOptionPane.showMessageDialog(frame, "Please select at least one from the time list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				} 
				
				// Sort first
				Arrays.sort(selectedIndices);
				
				// Check if the options will fulfill the required duration
				int numOfSlots = durationMins / 15;
				if (numOfSlots > Utility.longestConsecutive(selectedIndices)) {
					JOptionPane.showMessageDialog(frame, "Please select time slots according to the required duration!",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// Yes No Confirmation upon successful creation
				int result = JOptionPane.showConfirmDialog(frame, "Confirm the following time slots?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					//process selected;
					for (int i = 0; i < selectedIndices.length; i++) { 
						userFeedbackDates.add(timeInTheList.get(i));
						confirm = true;
					}
				} else {
					return;
				}
			}
		});
		
		buttonsPanel.add(confirmButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				confirm = false;
				frame.setVisible(false);
				frame.dispose();
			}	
		});
		
		buttonsPanel.add(cancelButton);
		
		_panel.add(buttonsPanel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public ArrayList<TimeSpan> getUserFeedback() {
		return userFeedbackDates;
	}
	
	public boolean isConfirm() {
		return confirm;
	}
}
