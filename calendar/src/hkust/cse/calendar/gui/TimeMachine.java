package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;

public class TimeMachine extends JDialog implements ActionListener {

	private CalGrid parent;
	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JComboBox monthB;
	private int monthInt;
	private JLabel dayL;
	private JComboBox dayB;
	private int dayInt;
	private JLabel TimeHL;
	private JTextField TimeH;
	private JLabel TimeML;
	private JTextField TimeM;

	private JButton saveBut;
	private JButton CancelBut;
	
	private final int[] days = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
			11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
			21, 22, 23, 24, 25, 26, 27, 28, 29 ,30, 31};
	private final String[] months = { "January", "Feburary", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	
	TimeMachine(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();
		
		JPanel pDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		pDate.setBorder(dateBorder);
		
		// Date and time text field
		// Year
		yearL = new JLabel("YEAR: ");
		pDate.add(yearL);
		yearF = new JTextField(6);
		pDate.add(yearF);
		// Month
		monthL = new JLabel("Month: ");
		pDate.add(monthL);
		monthB = new JComboBox();
		monthB.addActionListener(this);
		monthB.setPreferredSize(new Dimension(100, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			monthB.addItem(months[cnt]);
		pDate.add(monthB);
		// Day
		dayL = new JLabel("DAY: ");
		pDate.add(dayL);
		dayB = new JComboBox();
		dayB.addActionListener(this);
		dayB.setPreferredSize(new Dimension(100, 30));
		for (int cnt = 0; cnt < 31; cnt++)
			dayB.addItem(days[cnt]);
		pDate.add(dayB);
		
		// Set the panel
		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		TimeHL = new JLabel("Hour");
		psTime.add(TimeHL);
		TimeH = new JTextField(4);
		psTime.add(TimeH);
		TimeML = new JLabel("Minute");
		psTime.add(TimeML);
		TimeM = new JTextField(4);
		psTime.add(TimeM);

		JPanel pTime = new JPanel();
		pTime.setLayout(new BorderLayout());
		pTime.add("West", psTime);

		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pDate, BorderLayout.NORTH);
		top.add(pTime, BorderLayout.CENTER);

		contentPane.add("North", top);
		
		// Save and cancel
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);

		pack();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {
			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			saveButtonResponse();
		} else if (e.getSource() == monthB) {
			if (monthB.getSelectedItem() != null) {
				monthInt = monthB.getSelectedIndex() + 1;
			}
		} else if (e.getSource() == dayB) {
			if (dayB.getSelectedItem() != null) {
				dayInt = dayB.getSelectedIndex() + 1;
			}
		}
	}
	
	private void saveButtonResponse(){
		int year = Utility.getNumber(yearF.getText());
		int month = monthInt;
		int date = dayInt;
		int hour = Utility.getNumber(TimeH.getText());
		int min = Utility.getNumber(TimeM.getText());
		
		parent.setToday(year, month, date, hour, min);
	}
}
