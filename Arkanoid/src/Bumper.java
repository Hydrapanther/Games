import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;

public class Bumper extends Imaged
{
	private static int bumpLength = 80;
	private static int bumpHeight = 35;
	private static int state = 1; //0 = mini, 1 = normal, 2 = elongated
	
	private static int xLoc = 260; //using static to make the bumper accessible by the ball method, as there will only be one bumper
	private static int yLoc = 550;
	
	private Image currentImage;
	private int currentNum = 0;
	private Image[] versions = new Image[3];
	
	public Bumper()
	{
		setUp();
	}
	
	@Override
	Image openImageFromSpriteSheet(int x, int y, int w, int h) 
	{
		setUpImages();
		return ((BufferedImage)spritesheet).getSubimage(x, y, w, h).getScaledInstance(bumpLength, bumpHeight, BufferedImage.SCALE_SMOOTH);
	}

	@Override
	void setUp() 
	{
		versions[0] = this.openImageFromSpriteSheet(400, 0, 150, 71);
		
		currentImage = versions[currentNum]; //the sprites from this sheet turned out inconsistent in width, so I couldn't animate the electricity.
	}
	
	public void moveTo(int xVal)
	{
		xLoc = xVal;
	}

	public void draw(Graphics g)
	{
		g.drawImage(currentImage, xLoc, yLoc, bumpLength, bumpHeight, null);
	}
	
	public static int getX()
	{
		return xLoc;
	}
	
	public static int getY()
	{
		return yLoc;
	}
	
	public static int getLength()
	{
		return bumpLength;
	}
	
	public static int getHeight()
	{
		return bumpHeight;
	}
}
