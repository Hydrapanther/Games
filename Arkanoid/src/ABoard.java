import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class ABoard 
{
	private int rows;
	private int cols;
	private int total;
	final static int OFFSETX = 10;
	final static int OFFSETY = 20;
	
	static Block[][] grid;
	private boolean firstClick = true;
	JPanel panel;
	
	Bumper b;
	Ball ball;
	
	public ABoard(int rows, int cols, Bumper bee, Ball bleh)
	{
		this.rows = rows;
		this.cols = cols;
		total = rows*cols;
		this.setup();
		b = bee;
		ball = bleh;
	}
	
	public void setup()
	{
		grid = new Block[rows][cols];
		
		for(int r = 0; r < grid.length; r++)
		{
			for(int c = 0; c < grid[0].length; c++)
			{
				grid[r][c] = new Block(r, c);
			}
		}
	}
	
	public int deconstructComponent(int x, int y)
	{
		//TODO goal to determine if ball is touching a block, and how the ball should ricochet (horizontal or vertical), as well as break the block touched.
		int[] coords = findCords(x,y);
		
		if(coords[0] < grid.length && coords[1] < grid[0].length && coords[0] >= 0 && coords[1] >= 0)
		{
			if(grid[coords[0]][coords[1]].getState() != 1)
			{
				grid[coords[0]][coords[1]].breakit();
				if((x - this.OFFSETX)%Block.getLength() <=5 || (x - this.OFFSETX)%Block.getLength() >= Block.getLength()-5)
				{
					ball.bounceHor();
				}
				if((y - this.OFFSETY)%Block.getHeight() <=5 || (y - this.OFFSETY)%Block.getHeight() >= Block.getHeight()-5)
				{
					ball.bounceVert();
				}
			}
		}
		return -1; 
	}
	
	public int[] findCords(int x, int y)
	{
		//finds the coordinates of the block to be hit
		
		int cCord = (x - this.OFFSETX)/Block.getLength();
		int rCord = (y - this.OFFSETY)/Block.getHeight();
		
		int[] returnval = new int[]{rCord, cCord};
		
		return returnval;
	}
	
	public void draw(Graphics g)
	{
		Block.counter = 0;
		for(int r = 0; r < grid.length; r++)
		{
			for(int c = 0; c < grid[0].length; c++)
			{
				grid[r][c].draw(g);
			}
		}
		
		b.draw(g);
		ball.draw(g);
	}
	
	public void reset()
	{
		for(int r = 0; r < grid.length; r++)
		{
			for(int c = 0; c < grid[0].length; c++)
			{
				grid[r][c].reset();
			}
		}
	}
	
	public void sayHi() //testing purposes
	{
		System.out.println("hi!");
	}
	
}
