import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Ball extends Imaged
{
	private double xLoc = 300;
	private double yLoc = 527;
	private int size = 30;
	
	private double xSpd = Math.random() * 2;
	private double ySpd = (-1 * Math.random() * 1.5) - 0.5; //will always shoot somewhere off to the right
	
	Image img;
	
	public Ball()
	{
		setUp();
	}
	
	@Override
	Image openImageFromSpriteSheet(int x, int y, int w, int h) 
	{
		setUpImages();
		return ((BufferedImage)spritesheet).getSubimage(x, y, w, h).getScaledInstance(size, size, BufferedImage.SCALE_SMOOTH);
	}

	@Override
	void setUp() 
	{
		img = openImageFromSpriteSheet(800, 545, 85, 85);
	}
	
	public void changeX(int x)
	{
		xLoc = x;
	}
	
	public void move()
	{
		xLoc += xSpd;
		yLoc += ySpd;
		
		if(xLoc > 570 || xLoc < 0)
		{
			bounceHor();
			if(xLoc > 570)
				xLoc = 570; //to ensure it doesn't accidentally get stuck off to the sides when moving fast
			if(xLoc < 0)
				xLoc = 0;
		}
		if(yLoc < 0
//				|| yLoc > 525		//for testing purposes
				)
		{
			bounceVert();
			if(yLoc < 0)
				yLoc = 0;
		}
		
		if(yLoc > 525)
		{
			if(Bumper.getX()-20 < this.getX() && this.getX() < Bumper.getX() + Bumper.getLength()-20)
			{
				this.bounceVert();
				yLoc = 520;
				
				int difference = this.getX() - (Bumper.getX() + (Bumper.getLength()/2)-10); //usually in the 30s
				double changeby = difference/70.0; 
				
				if((xSpd > 0 && changeby < 0) || (xSpd < 0 && changeby > 0))
				{
					ySpd += (Math.abs(changeby/2));
					if(ySpd < 0.75)
						ySpd = 0.75;
					xSpd -= changeby;
				}
				
				xSpd += changeby * 2; //changes speed depending on where it hits the platform, if speed is reduced the change is less than if speed is increased
			}
		}
	}
	
	public void bounceHor()
	{
		xSpd *= -1;
		
		if(Math.abs(xSpd) < 4)
		{
			if(xSpd > 0)
				xSpd += 0.1; //gets progressively faster until capped value of 4
			else
				xSpd -= 0.1;
		}
	}
	
	public void bounceVert()
	{
		ySpd *= -1;
		
		if(Math.abs(ySpd) < 4)
		{
			if(ySpd > 0)
				ySpd += 0.1;
			else
				ySpd -= 0.1;
		}
	}
	
	public void revert()
	{
		xLoc = 300;
		yLoc = 527;

		
		xSpd = Math.random() * 2;
		ySpd = (-1 * Math.random() * 1.5) - 0.5; //will always shoot somewhere off to the right
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(img, (int)xLoc, (int)yLoc, size, size, null);
	}
	
	public int getX()
	{
		return (int)xLoc;
	}
	
	public int getY()
	{
		return(int)yLoc;
	}
}
