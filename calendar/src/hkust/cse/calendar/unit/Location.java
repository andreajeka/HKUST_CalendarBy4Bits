package hkust.cse.calendar.unit;

/* creating a Location class */
public class Location {

	private String location;
	private boolean bRemoval;
	private int capacity;
	
	public Location(String location, int capacity){
		this.location = location;
		this.bRemoval = false;
		this.capacity = capacity;
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
	
 	public void setCapacity(int capacity){
 		this.capacity = capacity;
 	}
 	
 	public int getCapacity(){
 		return this.capacity;
 	}
}