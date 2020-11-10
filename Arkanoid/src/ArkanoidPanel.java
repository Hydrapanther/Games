import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class ArkanoidPanel extends JPanel
{
	private ABoard board;
	private int rows = 8, cols = 7;
	
	private final int size = 600;
	private final Dimension SIZE_PANEL = new Dimension(size, size);
	private static Timer timer = new Timer(3, null);
	private int speed;
	private boolean firstClick = true;
	private boolean mouseInBounds = true;
	
	private Bumper b = new Bumper();
	private Ball ball = new Ball();
	ScorePanel s = new ScorePanel();
	
	public static void main(String[]args)
	{
		JFrame frame = new JFrame("Arkanoid -Lucas Yang AP Computer Science Final Project");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new ArkanoidPanel());
		frame.pack();
		frame.setVisible(true);
		timer.start();
	}
	
	public ArkanoidPanel()
	{
		this.setPreferredSize(SIZE_PANEL);
		this.setBackground(Color.black);
		board = new ABoard(rows, cols, b, ball);
		
		timer.addActionListener(new ActionListener()
		{		
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(firstClick == false)
				{
					ball.move();
					board.deconstructComponent(ball.getX(), ball.getY()); //this collision detection isn't 100% especially if the ball picks up speed, but it does alright
					
					if(ball.getY() > 570)
					{
						s.lostLife();
						if(s.getLives() > 0)
						{
							ball.revert();
							firstClick = true;
							s.stopTimer();
						}
						else
						{
							timer.stop();
							s.stopTimer();
							System.out.println("You lost!  Score: " + ScorePanel.getScore());
						}
					}
				}
				repaint();
			}
				
		});
		
		this.addMouseListener(new MouseListener()
		{
			@Override
			public void mouseEntered(MouseEvent e) 
			{
				mouseInBounds = true;
			}

			@Override
			public void mouseExited(MouseEvent e) 
			{
				mouseInBounds = false;
			}

			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(firstClick == true)
				{
					firstClick = false;
					s.start();
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {}

			@Override
			public void mouseReleased(MouseEvent e) {}		
			
		});
		
		this.addMouseMotionListener(new MouseMotionListener()
		{

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent e) 
			{
				b.moveTo(e.getX() - b.getLength()/2);
				if(firstClick == true)
				{
					ball.changeX(e.getX());
				}
			}
			
		});
	}
	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		if(!this.hasFocus())
			this.requestFocusInWindow();
		
		Block.resetCounter();
		board.draw(g);
		if(Block.counter == 0)
		{
			board.reset();
			board.draw(g);
		}
	}
}
