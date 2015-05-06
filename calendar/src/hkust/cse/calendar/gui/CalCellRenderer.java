package hkust.cse.calendar.gui;

import hkust.cse.calendar.gui.CalGrid.dayInfo;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

class CalCellRenderer extends DefaultTableCellRenderer

{

	private int r;

	private int c;

	public CalCellRenderer(ArrayList<dayInfo> values) {
		if (values.size() == 1) {
			dayInfo value = values.get(0);
			if (value == CalGrid.dayInfo.Empty) {
				setForeground(Color.black);
				setBackground(Color.white);
			}
			else if (value == CalGrid.dayInfo.Today) {
				setForeground(Color.red);
			} else if (value == CalGrid.dayInfo.HasEvent){
				setBackground(Color.green);
			}
		}
		else if (values.size() > 1){
			for (dayInfo value : values) {
				if (value == CalGrid.dayInfo.Today)
					setForeground(Color.red);
				if (value == CalGrid.dayInfo.HasEvent)
					setBackground(Color.green);	
			}
		}

		setHorizontalAlignment(SwingConstants.RIGHT);
		setVerticalAlignment(SwingConstants.TOP);
	}

	public int row() {
		return r;
	}

	public int col() {
		return c;
	}

}
