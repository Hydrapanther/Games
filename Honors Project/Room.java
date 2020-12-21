// Assignment #: Honors CSE 205
//         Name: Lucas Yang
//    StudentID: 1219127442
//      Lecture: MWF 8:35am-9:25am
//  Description: This is the class that represents a singular "room" on the game map, and has some of its own
//				 properties (walls, enemies) that it can use.

import java.util.ArrayList;
import javafx.scene.paint.Color;

public class Room 
{
	private boolean[] walls; //north, east, south, west
	private int numEnemies; //between 0 and 6
	private Color roomColor;
	private ArrayList<Enemy> enemies;
	
	public Room(int ne)
	{
		enemies = new ArrayList<Enemy>();
		walls = new boolean[4];
		for(int x = 0; x < walls.length; x++)
		{
			walls[x] = true;
		}
		numEnemies = ne; //passes in the number of enemies
		roomColor = randomColor(); //gets the random room color
		
		for(int x = 0; x < numEnemies; x++) //creates all the new enemies and adds it to the arraylist
		{
			enemies.add(new Enemy());
		}
	}
	
	private Color randomColor() //randomizes the color of the room
	{
		int random = (int)(Math.random() * 10);
		
		switch(random)
		{
		case 0: return Color.RED;
		case 1: return Color.ORANGE;
		case 2: return Color.YELLOW;
		case 3: return Color.BLUE;
		case 4: return Color.LIMEGREEN;
		case 5: return Color.BLUEVIOLET;
		case 6: return Color.AQUAMARINE;
		case 7: return Color.CHOCOLATE;
		case 8: return Color.HOTPINK;
		case 9: return Color.LIGHTGRAY;
		default: return Color.STEELBLUE; //not going to be used but better return a color than null
		}
	}
	
	//getters
	public boolean[] getWallStatus() //returns which walls are present and which are not
	{
		return walls;
	}
	public int getNumEnemies()
	{
		return numEnemies;
	}
	public Color getColor()
	{
		return roomColor;
	}
	public ArrayList<Enemy> getEnemies()
	{
		return enemies;
	}
	
	//setters
	public void removeWall(int index) //removes the wall at that index (0 - north, 1 - east etc.)
	{
		walls[index] = false;
	}
	public void removeAllEnemies()
	{
		enemies.clear();
	}
	public void removeEnemy(int index) //removes the enemy at that index (UNUSED)
	{
		enemies.remove(index);
	}
}
