package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.users.User;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


public class DetailsDialog extends JFrame implements ActionListener {
	private JButton exitBut;
	private JTextArea area;
	JPanel panel;
	private Component parentRef;
	

	public DetailsDialog(String msg, String title) {
		parentRef = null;
		this.setResizable(false);
		panel = new JPanel();
		Border border = new TitledBorder(null, "Information");
		panel.setBorder(border);
		
		area = new JTextArea(13, 23);
		area.setMargin(new Insets(10,10,0,0));
		area.setEditable(false);
		panel.add(area);
		paintContent(title);
		this.setPreferredSize(new Dimension(300,350));
		Display(msg);
		
		pack();
		this.setLocationRelativeTo(parentRef);
	}

	public DetailsDialog(Component component, Appt appt, String title) {
		if (component instanceof AppList)
			parentRef= component;
			
			panel = new JPanel();
			Border border = new TitledBorder(null, "Information");
			panel.setBorder(border);
			
			area = new JTextArea(25, 40);
			
			panel.add(area);
			paintContent(title);
			this.setSize(500, 350);
			Display(appt);
			pack();
	}

	public void paintContent(String title) {
		Container content = getContentPane();
		setTitle(title);
		
		exitBut = new JButton("Exit");
		exitBut.addActionListener(this);

		JPanel p2 = new JPanel();
		p2.setLayout(new FlowLayout(FlowLayout.CENTER));

		p2.add(exitBut);

		content.add("Center", panel);
		content.add("South", p2);
	}

	public void Display(String msg) {
		area.setFont(new Font("", Font.BOLD + Font.ITALIC, 14));
		area.setText(msg);
		area.setEditable(false);
	}

	@SuppressWarnings("deprecation")
	public void Display(Appt appt) {
		
		AppList appList = (AppList) parentRef;
		Timestamp sTime = appt.TimeSpan().StartTime();
		Timestamp eTime = appt.TimeSpan().EndTime();
		String time = sTime.getHours() + ":";
		if (sTime.getMinutes() == 0)
			time = time + "00" + " - " + eTime.getHours() + ":";
		else
			time = time + sTime.getMinutes() + " - " + eTime.getHours() + ":";
		if (eTime.getMinutes() == 0)
			time = time + "00";
		else
			time = time + eTime.getMinutes();

		area.setText("Appointment Information \n");
		area.append("Title: " + appt.getTitle() + "\n");
		area.append("Time: " + time + "\n");
		area.append("Location: " + appt.getLocation() + "\n"); 
		area.append("Capacity: " + appt.getCapacity() + "\n");
		area.append("\nParticipants:\n");
		area.append("  Attend:");
		LinkedList<UUID> attendList = appt.getAttendList();
		if(attendList != null)
		{
			
			for(int i = 0; i < attendList.size(); i++)
			{
				// Map user id to username
				area.append("  " + appList.parent.controller.searchUser(attendList.get(i)).getUsername());
				if (i == attendList.size() - 1) continue;
				area.append(",");
			}
		}
		area.append("\n\n  Reject:");
		LinkedList<UUID> rejectList = appt.getRejectList();
		if(rejectList != null)
		{
			for(int i = 0; i < rejectList.size(); i++)
			{
				area.append("  " + rejectList.get(i));
			}
		}
		area.append("\n\n  Waiting:");
		LinkedList<UUID> waitingList = appt.getWaitingList();
		if(waitingList != null)
		{
			for(int i = 0; i < waitingList.size(); i++)
			{
				area.append("  " + appList.parent.controller.searchUser(waitingList.get(i)).getUsername());
				if (i == waitingList.size() - 1) continue;	
				area.append(",");
			}
		}

		area.append("\n\nDescription: \n" + appt.getInfo());
		area.setEditable(false);
	}

	public void Display(Vector[] vs, User[] entities) {
		if (vs == null || entities == null)
			return;
		String temp = ((TimeSpan) vs[0].elementAt(0)).StartTime().toString();
		area.setText("Available Time For Selected participants and room ("
				+ temp.substring(0, temp.lastIndexOf(" ")) + ")\n\n\n");
		String temp1 = null;
		String temp2 = null;

		for (int i = 0; i < entities.length; i++) {
			area.append((i + 1) + ". " + entities[i].getUsername() + " :\n\n");
			for (int j = 0; j < vs[i].size(); j++) {
				temp1 = ((TimeSpan) vs[i].elementAt(j)).StartTime().toString();
				temp2 = ((TimeSpan) vs[i].elementAt(j)).EndTime().toString();
				area.append("   > From: "
						+ temp1.substring(0, temp1.lastIndexOf(':')) + "  To: "
						+ temp2.substring(0, temp2.lastIndexOf(':')) + "\n");

			}
			area.append("\n");

		}
		area.setEditable(false);

	}

	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == exitBut) {
			dispose();
		}
	}

}
