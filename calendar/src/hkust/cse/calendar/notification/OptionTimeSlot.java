package hkust.cse.calendar.notification;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

public class OptionTimeSlot extends Notification {

	private ArrayList<TimeSpan> dateChosenlist;
	public OptionTimeSlot(String title, String msg, ArrayList<TimeSpan> dateChosenList) {
		super(title, msg);
		this.dateChosenlist = dateChosenList;
		
	}

	@Override
	public boolean popUp() {
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
		JLabel desc = new JLabel("Please choose your available time slot from the following list: ");
		labelPanel.add(desc);
		_panel.add(labelPanel);
		
		DefaultListModel<String> timeListModel = new DefaultListModel<String>();
		JList<String> timeList = new JList<String>(timeListModel);
		timeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		timeList.setLayoutOrientation(JList.VERTICAL);
		timeList.setVisibleRowCount(8);
		JScrollPane timeListPane = new JScrollPane(timeList);
		timeListPane.setPreferredSize(new Dimension(300,380));
		
	
		
		_panel.add(timeListPane);
		
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton confirmButton = new JButton("Confirm");
		confirmButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		buttonsPanel.add(confirmButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}	
		});
		
		buttonsPanel.add(cancelButton);
		
		_panel.add(buttonsPanel);
		frame.pack();
		frame.setVisible(true);
		return true;
	}
	
	
}
