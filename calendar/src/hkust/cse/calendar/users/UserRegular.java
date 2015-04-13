package hkust.cse.calendar.users;

public class UserRegular extends User{

	public UserRegular(String username, String password) {
		super(username, password);
		setAdmin(false);
	}

}
