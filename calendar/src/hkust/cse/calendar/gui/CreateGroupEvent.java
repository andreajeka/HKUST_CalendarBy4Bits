package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.users.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class CreateGroupEvent extends JFrame{
	private CalGrid parent;
	Container pane;
	private JPanel createGroupPane;
	private JPanel browsePane;
	private JPanel navPane;
	private DefaultListModel<String> leftListModel;
	private JList<String> leftList;
	private JScrollPane lefttListPane;
	
	private JPanel labels;
	private JLabel toRight;
	private JLabel toLeft;
	
	private DefaultListModel<String> rightListModel;
	private JList<String> rightList;
	private JScrollPane rightListPane;
	
	private ArrayList<String> userChosenList;
	
	private JButton nextBtn;
	private JButton backBtn;
	private JButton confirmBtn;
	
	private JPanel time;
	private JLabel rangeTime;
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
	
	public CreateGroupEvent(CalGrid cal) {
		parent = cal;
		setSize(new Dimension(500,500));
		pane = this.getContentPane();
		setTitle("Manual Group Event Scheduling");
		
		loadUserPage();
		
		setVisible(true);
		setLocationRelativeTo(null);
	}
	
	private void loadUserPage() {
		createGroupPane = new JPanel();
		browsePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
		navPane = new JPanel();
		
		createGroupPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		
		browsePane.setBorder(titledBorder1);
		
		leftListModel = new DefaultListModel<String>();
		leftList = new JList<String>(leftListModel);
		leftList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		leftList.setLayoutOrientation(JList.VERTICAL);
		leftList.setVisibleRowCount(8);
		leftList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = leftList.locationToIndex(e.getPoint());
					if (index == -1) return;
					rightListModel.addElement(leftListModel.getElementAt(index));
					leftListModel.remove(index);
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		lefttListPane = new JScrollPane(leftList);
		lefttListPane.setPreferredSize(new Dimension(210,380));
		browsePane.add(lefttListPane);
		loadUserList();
		
		labels = new JPanel();
		labels.setLayout(new GridLayout(2,0));
		
		toRight = new JLabel(" > ");
		labels.add(toRight);
		
		toLeft = new JLabel(" < ");
		labels.add(toLeft);
		labels.setPreferredSize(new Dimension(20,380));
		
		browsePane.add(labels);
		
		rightListModel = new DefaultListModel<String>();
		rightList = new JList<String>(rightListModel);
		rightList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rightList.setLayoutOrientation(JList.VERTICAL);
		rightList.setVisibleRowCount(8);
		rightList.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = rightList.locationToIndex(e.getPoint());
					if (index == -1) return;
					leftListModel.addElement(rightListModel.getElementAt(index));
					rightListModel.remove(index);
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		
		rightListPane = new JScrollPane(rightList);
		rightListPane.setPreferredSize(new Dimension(210,380));
		browsePane.add(rightListPane);
	
		nextBtn = new JButton("Next >");
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rightListModel.isEmpty()) {
					JOptionPane.showMessageDialog(null, "You have to choose at least one person", "Input Error", JOptionPane.WARNING_MESSAGE);
					return;
				}
				userChosenList = new ArrayList<String>();
				for(int i = 0; i < rightListModel.getSize(); i++) 
					userChosenList.add((String) rightListModel.getElementAt(i));
				loadTimeSlotPage();
			}
			
		});
		
		navPane.add(nextBtn);
		createGroupPane.add(navPane, BorderLayout.SOUTH);
		createGroupPane.add(browsePane, BorderLayout.CENTER);
		createGroupPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		pane.add(createGroupPane);
	}

	private void loadUserList() {
		if (!parent.controller.getUserList().isEmpty()) {
			ArrayList<User> userList = parent.controller.getUserList();
			leftListModel.removeAllElements();
			for (User user : userList) {
				// Don't show yourself in the list. Just display other users
				if (!user.equals(parent.mCurrUser)) {
					String username = user.getUsername();
					if (user.isTobeRemoved())
						continue;
					else {
						leftListModel.addElement(username);
					}
				}
			}
			
		}
	}
	
	private void loadTimeSlotPage() {
		leftListModel.removeAllElements();
		labels.setVisible(false);
		rightListPane.setVisible(false);
		nextBtn.setVisible(false);
		
		browsePane.setLayout(new BoxLayout(browsePane, BoxLayout.Y_AXIS));
		lefttListPane.setPreferredSize(new Dimension(300,380));
		browsePane.remove(lefttListPane);
		
		TitledBorder titledBorder2 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Choose Time Slot:");
		browsePane.setBorder(titledBorder2);
		
		time = new JPanel(new BorderLayout());
		
		JPanel forLabel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		rangeTime = new JLabel("Show available timeslots by choosing the time range below:");
		forLabel.add(rangeTime);
		time.add(forLabel, BorderLayout.NORTH);
		
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
		
		time.add(pDate, BorderLayout.CENTER);
		
		JPanel startNend = new JPanel(new GridLayout(0,2));
		
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
		
		startNend.add(psTime);

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

		startNend.add(peTime);
		time.add(startNend, BorderLayout.SOUTH);
	
		browsePane.add(time);
		browsePane.add(lefttListPane);
		backBtn = new JButton("< Back");
		backBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				backBtn.setVisible(false);
				confirmBtn.setVisible(false);
				time.setVisible(false);
				labels.setVisible(true);
				rightListPane.setVisible(true);
				nextBtn.setVisible(true);
				browsePane.setLayout(new FlowLayout(FlowLayout.CENTER));
				lefttListPane.setPreferredSize(new Dimension(210,380));
				browsePane.remove(labels);
				browsePane.remove(rightListPane);
				browsePane.remove(lefttListPane);
				browsePane.add(lefttListPane);
				browsePane.add(labels);
				browsePane.add(rightListPane);
				TitledBorder titledBorder1 = new TitledBorder(BorderFactory
						.createEtchedBorder(Color.white, new Color(178, 178, 178)),
						"Add Participant:");
				
				browsePane.setBorder(titledBorder1);
				loadUserList();
				
				if (userChosenList != null) {
					for (String u : userChosenList)
						for(int i = 0; i < leftListModel.getSize(); i++) 
							if (u.equals(leftListModel.elementAt(i)))
								leftListModel.removeElementAt(i);
				}
			}
			
		});
		
		confirmBtn = new JButton("Confirm");
		confirmBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(null, "Confirm the following event?",
						"Confirm", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION){
					//TODO: DO STH
				} else {
					return;
				}
				
			}
			
		});
		
		navPane.add(backBtn);
		navPane.add(confirmBtn);
		
	}
	
}
