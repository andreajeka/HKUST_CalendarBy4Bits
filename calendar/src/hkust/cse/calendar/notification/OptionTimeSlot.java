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
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

public class OptionTimeSlot extends JPanel {
	
	private ApptStorageControllerImpl controller;
	private ArrayList<TimeSpan> datesChosenList;
	private ArrayList<TimeSpan> timeInTheList;
	private ArrayList<TimeSpan> userFeedbackDates;
	private int durationMins;
	private User currUser;
	private User sender;
	private boolean confirm;
	private JPanel _panel;
	
	public OptionTimeSlot(ApptStorageControllerImpl controller, User receiver,
			ArrayList<TimeSpan> datesChosenList, int durationMins, User sender) {
		this.datesChosenList = datesChosenList;
		this.controller = controller;
		this.currUser = receiver;
		this.sender = sender;
		this.durationMins = durationMins;
		timeInTheList = new ArrayList<TimeSpan>();
		userFeedbackDates = new ArrayList<TimeSpan>();
		setWindow();
	}
	
	public void setWindow() {
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		TitledBorder titledBorder = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Choose Time Slot with duration: " + durationMins + " minutes" );
		setBorder(titledBorder);
		
		DefaultListModel<String> timeListModel = new DefaultListModel<String>();
		JList<String> timeList = new JList<String>(timeListModel);
		timeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		timeList.setLayoutOrientation(JList.VERTICAL);
		timeList.setVisibleRowCount(8);
		JScrollPane timeListPane = new JScrollPane(timeList);
		timeListPane.setPreferredSize(new Dimension(300,380));
		for (int i = 0; i < datesChosenList.size(); i++) {
			ArrayList<User> entities = new ArrayList<User>();
			entities.add(currUser);
			entities.add(sender);
			ArrayList<TimeSpan> timeSlots = controller.RetrieveAvailTimeSpans(entities, datesChosenList.get(i));
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
		add(timeListPane);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton OKButton = new JButton("Confirm");
		OKButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Collect the selected indices
				int[] selectedIndices = timeList.getSelectedIndices();

				if (selectedIndices.length == 0) {
					JOptionPane.showMessageDialog(null, "Please select at least one from the time list",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				} 
				
				// Sort first
				Arrays.sort(selectedIndices);
				
				
				// Check if the options will fulfill the required duration
				int numOfSlots = durationMins / 15;
				if (numOfSlots > Utility.longestConsecutive(selectedIndices)) {
					JOptionPane.showMessageDialog(null, "Please select time slots according to the required duration!",
							"Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				// Yes No Confirmation upon successful creation
				int result = JOptionPane.showConfirmDialog(null, "Confirm the following time slots?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION) {
					//process selected;
					for (int i = 0; i < selectedIndices.length; i++) { 
						userFeedbackDates.add(timeInTheList.get(selectedIndices[i]));
						confirm = true;
					}
				} else {
					return;
				}
			}
		});
		
		buttonsPanel.add(OKButton);
		
		add(buttonsPanel);
	}
	
	public ArrayList<TimeSpan> getUserFeedback() {
		return userFeedbackDates;
	}
	
	public boolean isConfirm() {
		return confirm;
	}
	
}
