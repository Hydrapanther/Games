	import java.awt.Color;
	import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
	import java.awt.Image;
	import java.awt.event.*;
	import java.awt.image.BufferedImage;
	import java.util.*;
	import javax.swing.*;
	import javax.swing.Timer;
	
public class ScorePanel extends JPanel
{
	private static Timer timer = new Timer(100, null); //10 points/second = 1 point/100 milliseconds
	private int counter = 0;
	private static int score = 0;
	private static int time = 0;
	private static JLabel lives = new JLabel();
	private static int lifes = 3;
	private static JLabel scorer = new JLabel();
	private static JLabel timeCount = new JLabel();
	private JLabel blankSpace = new JLabel("                 "); //to circumvent problems I was having
	
	private int timersElapsed;
	
	private final Dimension SIZE_PANEL = new Dimension(100, 40);
	
	public void start()
	{
		JFrame frame = new JFrame("Scoreboard");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new ScorePanel());
		
		frame.setLayout(new FlowLayout());
		frame.setLocation(600, 0);
		
		scorer.setText("Score: " + score);
		timeCount.setText("Time: " + time);
		lives.setText("Lives: " + lifes);
		frame.add(scorer);
		frame.add(timeCount);		
		frame.add(lives);
		frame.add(blankSpace);
		
		frame.pack();
		frame.setVisible(true);
		timer.start();
	}
	
	public ScorePanel()
	{
		this.setPreferredSize(SIZE_PANEL);
		
		timer.addActionListener(new ActionListener()
		{		
			@Override
			public void actionPerformed(ActionEvent e)
			{
				score++;
				counter++;
				if (counter%10 == 0)
				{
					time++;
				}
				
				scorer.setText("Score: " + score);
				timeCount.setText("Time: " + time);
				lives.setText("Lives: " + lifes);
				repaint();
			}
				
		});
	}
	
	public void lostLife()
	{
		lifes--;
	}
	
	public void startTimer()
	{
		timer.start();
	}
	
	public void stopTimer()
	{
		timer.stop();
		scorer.setText("Score: " + score);
		timeCount.setText("Time: " + time);
		lives.setText("Lives: " + lifes);
		repaint();
	}
	
	public static void addScore(int x)
	{
		score += x;
	}
	
	public int getLives()
	{
		return lifes;
	}
	
	public static int getScore()
	{
		return score;
	}
}
