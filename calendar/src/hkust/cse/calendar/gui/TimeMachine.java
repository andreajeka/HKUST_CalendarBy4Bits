package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
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

public class TimeMachine extends JDialog implements ActionListener {

	private CalGrid parent;
	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JTextField monthF;
	private JLabel dayL;
	private JTextField dayF;
	private JLabel TimeHL;
	private JTextField TimeH;
	private JLabel TimeML;
	private JTextField TimeM;

	private JButton saveBut;
	private JButton CancelBut;
	
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
		
		// Date and time
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
		}
	}
	
	private void saveButtonResponse(){
		int year = Utility.getNumber(yearF.getText());
		int month = Utility.getNumber(monthF.getText());
		int date = Utility.getNumber(dayF.getText());
		int hour = Utility.getNumber(TimeH.getText());
		int min = Utility.getNumber(TimeM.getText());
		
		parent.setToday(year, month, date, hour, min);
	}
}
