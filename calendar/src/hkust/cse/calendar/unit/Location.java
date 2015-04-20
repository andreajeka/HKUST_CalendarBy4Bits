package hkust.cse.calendar.unit;

/* creating a Location class */
public class Location {

	private String location;
	private boolean bRemoval;
	
	public Location(String location){
		this.location = location;
		this.bRemoval = false;
	}
	
	public String getName(){
		return location;
	}
	
	public void setName(String location){
		this.location = location;
	}
	
	public boolean getRemovalBool()
	{
		return bRemoval;
	}
}