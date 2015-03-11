package hkust.cse.calendar.unit;

/* creating a Location class */
public class Location {

	private String location;
	
	public Location(String location){
		this.location = location;
	}
	
	public String getName(){
		return location;
	}
	
	public void setName(String location){
		this.location = location;
	}
}