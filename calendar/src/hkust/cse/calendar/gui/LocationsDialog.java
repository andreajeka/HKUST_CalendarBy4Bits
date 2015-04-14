package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Location;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class LocationsDialog extends JFrame {
	private static final long serialVersionUID = 1L;

	private ApptStorageControllerImpl _controller;

	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JTextField locNameText;
	private JScrollPane scroll;
	private JPanel buttonPane = new JPanel();		
	private JButton addBt = new JButton("Add");
	private JButton removeBt = new JButton("Remove");
	private int temp1, selectedIndex, capacity;
	
	public LocationsDialog(ApptStorageControllerImpl controller){
		_controller = controller;

		/**set the border layout***/

		this.setLayout(new BorderLayout());
		this.setLocationByPlatform(true);
		this.setSize(300, 200);

		listModel = new DefaultListModel<String>();

		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(5);

		scroll = new JScrollPane(list);
		this.add(scroll, BorderLayout.CENTER);

		locNameText = new JTextField(11);
		buttonPane.add(locNameText);
		buttonPane.add(addBt, BorderLayout.CENTER);
		buttonPane.add(removeBt, BorderLayout.EAST);
		this.add(buttonPane, BorderLayout.PAGE_END);

		/******   loading items to JList   ******/		
		loadLocation();

		
		/******   add item to JList and Location[]   ******/
		addBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(locNameText.getText().equals("")){
					JOptionPane.showMessageDialog(null, "Invalid Input! Please Input Again", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
				}
				else{
					if(_controller.getLocationList() != null){
						Location[] checkLocations = _controller.getLocationList();
						for(int i=0; i<checkLocations.length; i++){
							if(checkLocations[i].getName().equals(locNameText.getText().toString())){
								JOptionPane.showMessageDialog(null, "Duplicate Location", "INPUT ERROR", JOptionPane.ERROR_MESSAGE);
								locNameText.setText("");
								return;
							}
						}
					}
					listModel.addElement(locNameText.getText().toString());
								locNameText.setText("");
								updateLocation();
				}
			}
		});
		
		/******   remove item from JList and Location[]   ******/
		removeBt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				selectedIndex = list.getSelectedIndex();
				if(selectedIndex != -1)
					listModel.remove(selectedIndex);
				else if(selectedIndex == -1)
					JOptionPane.showMessageDialog(null, "Please Select a Loacation", "REMOVE ERROR", JOptionPane.ERROR_MESSAGE);
				capacity = 0;
				if(_controller.getLocationList() != null){
					capacity = _controller.getLocationCapacity();
					Location[] locations = _controller.getLocationList();
					Location[] loca = new Location[capacity-1];
					for(int i=0; i<capacity; i++){
						if(i < selectedIndex){
							loca[i] = locations[i];
						}
						else if(i > selectedIndex){
							loca[i-1] = locations[i];
						}
					}
					_controller.setLocationList(loca);
				}
				
			}
		});
		
	}

	/******   loading the locations from _controller to JList   ******/
	private void loadLocation(){
		if(_controller.getLocationList() != null){
			Location[] locations = _controller.getLocationList();
			//System.out.println("enter loadLocation " + locations.length);
			if(locations.length != 0){
				listModel.clear();
				for(int i=0; i<locations.length; i++){
					listModel.addElement(locations[i].getName().toString());
				}
			}
		}
	}

	/******   save the locations from JList to _controller   ******/
	private void updateLocation(){
		temp1 = listModel.getSize();
		Location[] locations = new Location[temp1];
		if(temp1 != 0){
			for(int i=0; i<temp1; i++){
				locations[i] = new Location(listModel.getElementAt(i).toString());
			}
		}
		_controller.setLocationList(locations);
	}

}