// Assignment #: Honors CSE 205
//         Name: Lucas Yang
//    StudentID: 1219127442
//      Lecture: MWF 8:35am-9:25am
//  Description: This is the class that represents anything that moves, including the player and the bullets
//				 created by the enemies (who are also entities). 

//base class for enemies, player, projectiles

public class Entity 
{
	protected double speed, locX, locY;
	protected double destX, destY;
	
	public Entity(double speed, double locX, double locY) //speed, current location
	{
		this.speed = speed; //the higher the speed the slower it goes
		this.locX = locX;
		this.locY = locY;
		
		int x = (int) locX;
		int y = (int) locY;
		
		destX = x;
		destY = y;
	}
	
	public void move() //moves towards destination
	{		
		if(speed == 0)
		{
			return;
		}
		
		double dx = destX - locX;
		double dy = destY - locY;
		
		locX += dx/speed;
		locY += dy/speed;
	}
	
	public boolean atDestination()
	{
		if((int)locX == (int) destX && (int) locY == (int)destY) //all destinations add up
		{
			return true;
		}
		return false;
	}
	
	//setters
	public void setSpeed(int s) //sets the speed, possibly unused unless I did use it and forgot
	{
		speed = s;
	}
	public void setDestination(double d, double e) //sets destination
	{
		destX = d;
		destY = e;
	}
	public void setLocation(double x, double y) //sets the location (teleports)
	{
		locX = x;
		locY = y;
	}
	
	//getters
	public double getLocX()
	{
		return locX;
	}
	public double getLocY()
	{
		return locY;
	}
}
