package hkust.cse.calendar.users;

import java.io.Serializable;

public class User implements Serializable {

	// User name and password is used for login and sign up credentials
	private String mUsername;
	private String mPassword;				
			
	// Extra info to be used in Personal Settings
	private String mFirstName;
	private String mLastName;
	private String mEmail;
	private String mAddress;
	private String mPhone;
	
	// To indicate which use is an admin or a regular
	private boolean isAdmin;
	
	/**NOTE**/
	// I'm not convince we should put a isInitiator boolean here.
	// Suppose a user initiate an event, then he/she is the initiator
	// Suppose it has another event in which he is not the initiator, then he is not the initiator
	// CONFLICT: isInitiator cannot have two values.
	// Suggestion: Every appointment specifies its initiator
	
	// Constructor of class 'User' which set up the user id and password
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	// Getter of user name
	public String getUsername() {
		return mUsername;
	}


	// Setter of user name
	public void setUsername(String mUsername) {
		this.mUsername = mUsername;
	}

	// Getter of user name
	public String getPassword() {
		return mPassword;
	}

	// Setter of user name
	public void setPassword(String mPassword) {
		this.mPassword = mPassword;
	}
	
	// Getter of first name
	public String getFirstName() {
		return mFirstName;
	}

	// Setter of first name
	public void setFirstName(String mFirstName) {
		this.mFirstName = mFirstName;
	}

	// Getter of first name
	public String getLastName() {
		return mLastName;
	}

	// Setter of last name
	public void setLastName(String mLastName) {
		this.mLastName = mLastName;
	}

	// Getter of email
	public String getEmail() {
		return mEmail;
	}

	// Setter of email
	public void setEmail(String mEmail) {
		this.mEmail = mEmail;
	}

	// Getter of user address
	public String getAddress() {
		return mAddress;
	}

	// Setter of user address
	public void setAddress(String mAddress) {
		this.mAddress = mAddress;
	}

	// Getter of user phone number
	public String getPhone() {
		return mPhone;
	}

	// Setter of user phone number
	public void setPhone(String mPhone) {
		this.mPhone = mPhone;
	}

	// Return the admin status of a user
	public boolean isAdmin() {
		return isAdmin;
	}

	// Specify which user is an admin or a regular
	// true --> user is admin
	// false --> user is regular
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	// Use this method to compare different users 
	@Override
	public boolean equals(Object u) {
		if (this == u) return true;
		if (u == null) return false;
		if (getClass() != u.getClass()) return false;
		User temp = (User) u;
		if (mUsername == temp.getUsername() && mPassword == temp.getPassword()) {
			return true;
		}
		return false;
	}

}
