=== README for Bonus Competition ===
Contributor: Andrea Juliati Kurniasari (20063498)

//Note: Please open in editor like Sublime Text to get better format of this README.txt//

Title: Search Appointment and Auto-Complete Function

== Description ==
This function serves as a new way of navigation for the users. 
Users now can simply search the desired appointment to be displayed in the ApptList by simply typing the title of the appointment in the search box. Not only the new implementation can help user to navigate right to the target, the search box is extensive enough to do auto-completion if the corresponding input matches one of the valid appointments in the program.

== Implementation ==
1. GUI
	GUI for this function will include several Java components such as JTextField to store the input string of the user, JComboBox to store a list of the valid appointments in the program in which is useful for auto-completion function, and finally JButton that will navigate the user to the corresponding appointment once it is clicked. All of these components will be located in JMenuBar in CalGrid, a decision made simply because it is accessible and easily noticeable.

2. Behind the Scene
	To implement the functions, key listener and action listener will be needed. Key listener will handle events when user presses or releases any key in the keyboard while action listener handle the button clicking events. 
	a) When the user types a letter (press or releases the keyboard) in the 
	   search box, the system simply check if it is a valid appointment. If it is, pop up the combo box to suggest auto complete. If not valid, simply don't pop up anything. If the user indeed presses enter when there is no combo box popping out, we simply add the item into the combobox (it means its a search history)

	b) When the search button is clicked, we get the input of the search 	field (in string format) and check whether it is a valid appointment. If it is a valid appointment, the program retrieve the date of the appointment and triggers CalGrid to navigate to the corresponding date.

	Another interesting implementation is the usage of EventQueue.invokeLater() which is processing the key release in a different thread, so our Calendar GUI stays responsive (GUI process is done within the EDT). When EventQueue.invokeLater comes into play, it posts an event (the Runnable) at the end of Swings event list and is processed after all other GUI events are processed.