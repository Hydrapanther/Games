// Assignment #: Honors CSE 205
//         Name: Lucas Yang
//    StudentID: 1219127442
//      Lecture: MWF 8:35am-9:25am
//  Description: This represents all the enemies in the game, that attempt to shoot down the player

public class Enemy extends Entity
{
	private double targetX;
	private double targetY;
	private int type; //0 = standard, 1 = rushdown, 2 = fast, 3 = midspeed 5hp
	private int hp;
	private int attackSpeed;
	private boolean dead;
	
	public Enemy()
	{		
		super(0, 200, 200); //it's an entity
		
		targetX = 0; targetY = 0;
		attackSpeed = 1000; //default one/second
		randomizeType();
		dead = false;
	}
	
	public Entity attack(int attackTimer) //makes the enemy attack, returns a projectile entity
	{
		if(dead) 
			return null;
		if(attackTimer % attackSpeed == 0 && attackTimer != 0) //but only attacks every few milliseconds
		{
			Entity bullet = new Entity(150, locX + 10, locY + 10); //sets the location to originate from the enemy
			bullet.setDestination(targetX, targetY); //the destination is wherever it's targeting
			return bullet;
		}
		return null;
	}
	
	public boolean hit() //the player has hit the enemy
	{
		if(dead) return false;
		
		hp--; //removes one hp
		if(hp == 0) //player is dead
		{
			dead = true;
			return true; //dead
		}
		return false;
	}
	
	public void setTarget(double x, double y) //sets the target to where the player is
	{
		if(dead) return;
		
		double dx = locX - x;
		double dy = locY - y;
		
		targetX = x;
		targetY = y;
		
		while((targetX >= 0 && targetX <= 400) && (targetY >= 0 && targetY <= 400))
		{
			targetX -= dx;
			targetY -= dy;
		} //repeatedly changes the target by the slope until it hits the edge of the screen so the bullet will
			//keep going
		
		targetX -= dx;
		targetY -= dy;
		
		//randomizes the trajectory of the bullets for some spread
		targetX += (int)(Math.random() * 100);
		targetX -= (int)(Math.random() * 100);
		targetY += (int)(Math.random() * 100);
		targetY -= (int)(Math.random() * 100);
	}
	
	private void randomizeType() //randomizes enemy type
	{
		int choice = (int)(Math.random()*10);
		
		if(choice < 5) //0 to 4, standard, 50% chance, 2hp
		{
			type = 0;
			hp = 2;
			setSpeed(50);
			attackSpeed = 90 + (int)(Math.random() * 150); //attack speed is randomized, lower numbers = faster attacks
		}
		else if(choice < 7) //5 to 6, rushdown, 20% chance
		{
			type = 1;
			hp = 1;
			setSpeed(40);
			attackSpeed = 48 + (int)(Math.random() * 84);
		}
		else if(choice < 9) //7 and 8, fast, 20% chance
		{
			type = 2;
			hp = 1;
			setSpeed(10);
			attackSpeed = 40 + (int)(Math.random() * 80);
		}
		else if(choice < 10) //9, 5hp, moderate speed, 10% chance
		{
			type = 3;
			hp = 5;
			setSpeed(20);
			attackSpeed = 24 + (int)(Math.random() * 96);
		}
	}
	
	public void updateDestination(double playerX, double playerY) //change destination 
	{
		if(dead) return;
		
		if(type == 1) //rushdown always follows the player
		{
			destX = playerX;
			destY = playerY;
		}
		else //if it's at the former destination, then change its destination
		{
			if(atDestination())
			{
				destX = Math.random() * 370 + 10;
				destY = Math.random() * 370 + 10;
			}
		}
	}
	
	//getters
	public int getHp()
	{
		return hp;
	}
	public int getType()
	{
		return type;
	}
	public boolean getDead()
	{
		return dead;
	}
}
