package hkust.cse.calendar.users;

public class UserAdmin extends User {

	public UserAdmin(String username, String password) {
		super(username, password);
		setAdmin(true);
	}

}
