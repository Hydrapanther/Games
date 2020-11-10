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

public class Block extends Imaged
{
	private static int len = 82;
	private static int hei = 30;
	public static int counter = 0;
	
	private int row;
	private int col;
	private boolean powerup;
	private int state = 0; //0 = intact, 1 = broken (may add 2 later)
	
	private Image currentImage;
	private static Image[] blocks = new Image[10];
	
	public Block(int row, int col)
	{
		this.row = row;
		this.col = col;
		this.setUp();
	}
	
	Image openImageFromSpriteSheet(int x, int y, int w, int h) //package visibility, overrides abstract method
	{
		setUpImages();
		return ((BufferedImage)spritesheet).getSubimage(x, y, w, h).getScaledInstance(len, hei, BufferedImage.SCALE_SMOOTH);
	}
	
	void setUp() //also package visibility
	{
		blocks[0] = this.openImageFromSpriteSheet(0, 0, 200, 80);
		blocks[1] = this.openImageFromSpriteSheet(0, 80, 200, 80);
		blocks[2] = this.openImageFromSpriteSheet(0, 160, 200, 80);
		blocks[3] = this.openImageFromSpriteSheet(0, 239, 200, 80);
		blocks[4] = this.openImageFromSpriteSheet(0, 310, 200, 80);
		blocks[5] = this.openImageFromSpriteSheet(0, 380, 200, 80);
		blocks[6] = this.openImageFromSpriteSheet(0, 455, 200, 80);
		blocks[7] = this.openImageFromSpriteSheet(0, 530, 200, 80);
		blocks[8] = this.openImageFromSpriteSheet(0, 605, 200, 80);
		blocks[9] = this.openImageFromSpriteSheet(0, 680, 200, 80);

		int rando = (int)(Math.random() * 10);
		currentImage = blocks[rando];
	}
	
	public void breakit()
	{
		state++;
		ScorePanel.addScore(200);
	}
	
	public void draw(Graphics g)
	{
		if(state == 0)
		{
			g.drawImage(currentImage, col * len + ABoard.OFFSETX, row * hei + ABoard.OFFSETY, len, hei, null);
			counter++;
		}
	}
	
	public void reset()
	{
		int rando = (int)(Math.random() * 10);
		currentImage = blocks[rando];
		state = 0;
	}
	
	public int getState()
	{
		return state;
	}
	
	public static int getLength()
	{
		return len;
	}
	
	public static int getHeight()
	{
		return hei;
	}
	
	public static void resetCounter()
	{
		counter = 0;
	}
	
	public static int getCounter()
	{
		return counter;
	}
}
