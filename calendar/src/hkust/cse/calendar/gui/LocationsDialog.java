package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Location;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class LocationsDialog extends JFrame {
	private static final long serialVersionUID = 1L;

	private ApptStorageControllerImpl _controller;

	private DefaultListModel<String> listModel;
	private DefaultListModel<String> groupListModel;
	private DefaultListModel<String> capacityListModel;
	private DefaultTableModel tableModel;
	private JList<String> list;
	private JList<String> capacityList;
	private JList<String> groupList;
	private JTextField locNameText;
	private JTextField capacityText;
	private JScrollPane scroll, groupScroll;
	private JPanel left;
	private JPanel buttonPane, groupButtonPane;
	private JPanel groupLocationPane;
	private JButton groupBt, groupRemoveBt;
	private JButton addBt = new JButton("Add");
	private JButton removeBt = new JButton("Remove");
	private int temp1, selectedIndex, selectedIndexCapa, capacity;
	private ArrayList<Location> checkLocations;
	private ArrayList<Location> locations;
	private int index;
	private JFrame groupFrame;

	public LocationsDialog(ApptStorageControllerImpl controller){
		_controller = controller;

		/**set the border layout***/

		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(480, 300);
		left = new JPanel();

		listModel = new DefaultListModel<String>();
		capacityListModel = new DefaultListModel<String>();
		list = new JList<String>(listModel);
		capacityList = new JList<String>(capacityListModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);

		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					index = list.locationToIndex(e.getPoint());
					if(Integer.parseInt(capacityListModel.get(index)) == 0){
						JOptionPane.showMessageDialog(null, listModel.get(index) + " is not a group location.", "Information", JOptionPane.INFORMATION_MESSAGE);
					}
					else if(Integer.parseInt(capacityListModel.get(index)) > 0){
						JOptionPane.showMessageDialog(null, "[Group Location] The Capacity of " + listModel.get(index) + " : " + capacityListModel.get(index), "Information", JOptionPane.INFORMATION_MESSAGE);
					}
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

		capacityList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		capacityList.setLayoutOrientation(JList.VERTICAL);
		capacityList.setVisibleRowCount(5);

		TitledBorder title = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(200, 200, 200)),
				" Location:                Capacity:  ");

		scroll = new JScrollPane(list);
		scroll.setPreferredSize(new Dimension(470,225));

		left.add(scroll, BorderLayout.WEST);

		this.add(left, BorderLayout.WEST);

		locNameText = new JTextField(11);
		capacityText = new JTextField(5);
		buttonPane = new JPanel();
		buttonPane.setBorder(title);
		buttonPane.add(locNameText);
		buttonPane.add(capacityText);
		buttonPane.add(addBt);
		buttonPane.add(removeBt);
		

		groupBt = new JButton("Group");

		buttonPane.add(groupBt);
		this.add(buttonPane, BorderLayout.PAGE_END);

		/******   loading items to JList   ******/		
		loadLocation();


		/******   add item to JList and Location[]   ******/
		addBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(locNameText.getText().trim().isEmpty()){
					JOptionPane.showMessageDialog(null, "Invalid Input! Please Input Again", "INPUT ERROR", JOptionPane.WARNING_MESSAGE);
				}
				else{
					if(_controller.getLocationList() != null){
						checkLocations = _controller.getLocationList();
						for(int i=0; i<checkLocations.size(); i++){
							if(checkLocations.get(i).getName().equals(locNameText.getText().trim())){
								JOptionPane.showMessageDialog(null, "Duplicate Location", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
								locNameText.setText("");
								capacityText.setText("");
								return;
							}
						}
					}
					if(Integer.parseInt(capacityText.getText().trim()) < 0){
						JOptionPane.showMessageDialog(null, "Capacity cannot be a negative number", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
						locNameText.setText("");
						capacityText.setText("");
					}
					else if(!locNameText.getText().equals("") && !capacityText.getText().equals("")){
						try{
							capacity = Integer.parseInt(capacityText.getText().trim());
							listModel.addElement(locNameText.getText().trim());
							capacityListModel.addElement(capacityText.getText().trim());
							locNameText.setText("");
							capacityText.setText("");
							updateLocation();
						} catch (NumberFormatException a) {
							JOptionPane.showMessageDialog(null, "capacity should be an integer number", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
							locNameText.setText("");
							capacityText.setText("");
						}
					}
					else{
						JOptionPane.showMessageDialog(null, "please input the name and capacity of the location.\nIf there is no capacity, please input 0.", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
						locNameText.setText("");
						capacityText.setText("");
					}
				}
			}
		});

		/******   remove item from JList and Location[]   ******/
		removeBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				selectedIndex = list.getSelectedIndex();
				selectedIndexCapa = capacityList.getSelectedIndex();
				if(selectedIndex != -1){
					listModel.remove(selectedIndex);
					capacityListModel.remove(selectedIndex);
				}
				else if(selectedIndex == -1) {
					JOptionPane.showMessageDialog(null, "Please Select a Location", "REMOVE ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}


				if(_controller.getLocationList() != null){
					locations = _controller.getLocationList();
					locations.remove(selectedIndex);
					_controller.setLocationList(locations);
				}

			}
		});
/*		
		groupRemoveBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				selectedIndex = groupList.getSelectedIndex();
				selectedIndexCapa = capacityList.getSelectedIndex();
				if(selectedIndex != -1){
					listModel.remove(selectedIndex);
					capacityListModel.remove(selectedIndex);
				}
				else if(selectedIndex == -1) {
					JOptionPane.showMessageDialog(null, "Please Select a Location", "REMOVE ERROR", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(_controller.getLocationList() != null){
					locations = _controller.getLocationList();
					locations.remove(selectedIndex);
					_controller.setLocationList(locations);
				}
			}
		});
*/		
		groupBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				groupListModel = new DefaultListModel<String>();
				for(int i=0; i<_controller.getLocationList().size(); i++){
					if(_controller.getLocationList().get(i).getCapacity() > 0)
						groupListModel.addElement(_controller.getLocationList().get(i).getName() + "  (" + Integer.toString(_controller.getLocationList().get(i).getCapacity()) + ")");
				}
				groupList = new JList<String>(groupListModel);
				groupLocationPane = new JPanel();
				groupRemoveBt = new JButton("remove");
				groupScroll = new JScrollPane(groupList);
				groupScroll.setPreferredSize(new Dimension(470,225));
				groupLocationPane.add(groupScroll, BorderLayout.PAGE_START);
				groupFrame = new JFrame("Group Location Management");
				groupFrame.setLayout(new BorderLayout());
				groupFrame.setLocationByPlatform(false);
				groupFrame.setSize(480, 300);	
				groupFrame.add(groupLocationPane, BorderLayout.PAGE_START);
				groupButtonPane = new JPanel();
				groupButtonPane.add(groupRemoveBt);
				groupFrame.add(groupButtonPane, BorderLayout.PAGE_END);
				groupFrame.setVisible(true);
				
			}
		});

	}

	/******   loading the locations from _controller to JList   ******/
	private void loadLocation(){
		if(_controller.getLocationList() != null){
			locations = _controller.getLocationList();
			//System.out.println("enter loadLocation " + locations.length);
			if(locations.size() != 0){
				listModel.clear();
				for(int i=0; i<locations.size(); i++){
					listModel.addElement(locations.get(i).getName().toString());
					capacityListModel.addElement(Integer.toString(locations.get(i).getCapacity()));
				}
			}
		}
	}

	/******   save the locations from JList to _controller   ******/
	private void updateLocation(){
		temp1 = listModel.getSize();
		locations = new ArrayList<Location>();
		if(temp1 != 0){
			for(int i=0; i<temp1; i++){
				locations.add(i, new Location(listModel.getElementAt(i).toString(), Integer.parseInt(capacityListModel.getElementAt(i).toString())));
			}
		}
		_controller.setLocationList(locations);
	}
}
