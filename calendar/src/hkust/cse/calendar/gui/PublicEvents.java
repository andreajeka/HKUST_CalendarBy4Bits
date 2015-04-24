package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PublicEvents  extends JFrame implements ActionListener{
	
	private JPanel panelContainer;
	private JLabel label;
	private JScrollPane scrollPane;
	private JButton exitButton;
	private JTextArea eventFeed;
	private SimpleDateFormat dateFormat;
	private SimpleDateFormat timeFormat;

	CalGrid parent;
	
	public PublicEvents(CalGrid cal) {
		Container pane = this.getContentPane();
		setTitle("Public Events");
		setSize(500,500);
		setLocationRelativeTo(null);
		parent = cal;
		
		panelContainer = new JPanel();
		panelContainer.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10,10,10,10),
				BorderFactory.createEtchedBorder()));
		panelContainer.setLayout(new BorderLayout());
		
		label = new JLabel("Event Feeds: \n");
		label.setFont(new Font("", Font.BOLD, 18));
		panelContainer.add(label, BorderLayout.NORTH);
		
		scrollPane = new JScrollPane();
		eventFeed = new JTextArea(20,32);
		eventFeed.setEditable(false);
		eventFeed.setBackground(new Color(176,196,222));
		eventFeed.setFont(new Font("", Font.BOLD+Font.ITALIC, 14));
		scrollPane.getViewport().add(eventFeed);
		panelContainer.add(scrollPane, BorderLayout.CENTER);
			
		exitButton = new JButton("Exit");
		exitButton.addActionListener(this);
		panelContainer.add(exitButton, BorderLayout.SOUTH);
		
		pane.add(panelContainer);
		
		loadEventFeed();
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == exitButton) {
			setVisible(false);
			this.dispose();
		}
		
	}
	
	private void loadEventFeed() {
		Appt[] apptArray = parent.controller.RetrieveAllAppts();
		dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
		timeFormat = new SimpleDateFormat("kk:mm");
		for (int i = 0; i < apptArray.length; i++) {
			if (apptArray[i].isPublic()) {
				eventFeed.append("Title: " + apptArray[i].getTitle()+ "\n");
				eventFeed.append("Date: " + dateFormat.format(apptArray[i].TimeSpan().StartTime()) + "\n");
				eventFeed.append("Time: " + timeFormat.format(apptArray[i].TimeSpan().StartTime()) + " to " 
								+ timeFormat.format(apptArray[i].TimeSpan().EndTime()) + "\n");
				eventFeed.append("Location: " + apptArray[i].getLocation()+ "\n");
				eventFeed.append("Attending: ");
				ArrayList<UUID> attendantList = new ArrayList<UUID>(apptArray[i].getAttendList());
				for (int j = 0; j < attendantList.size(); j++) {
					eventFeed.append(parent.controller.searchUser(attendantList.get(j)).getUsername());
					if (j != attendantList.size() - 1)
						eventFeed.append(", ");
				}
				eventFeed.append("\n\n");
				
				
				
			}
		}
	}
	
	
}
