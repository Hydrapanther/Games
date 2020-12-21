// Assignment #: Honors CSE 205
//         Name: Lucas Yang
//    StudentID: 1219127442
//      Lecture: MWF 8:35am-9:25am
//  Description: This is the central class used to handle most of the player input and game movement,
//				 including rooms, enemies, the player themselves, and almost all game interactions.
//				 It represents a sort of "game board", which would hold the map and all the pieces.

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Board extends StackPane
{
	private final int BOARDR = 7, BOARDC = 10; //board dimensions
	private final boolean DEMO = false; 
	//demo makes the goal (1, 1) and the player invincible
	private final int SQUARESIZE = 20; //square size
	private final int PLAYERSPEED = 35; //higher number = slower
	
	private Room[][] board; //7 rows, 10 cols
	private int goalr, goalc; //goal location
	private int startr, startc; //start location
	private int currentr, currentc; //running totals, current location
	
	private Pane canvas; //canvas
	private Rectangle northWall, eastWall, southWall, westWall; //walls
	
	private Timeline timeline; //thread to constantly make everything move
		
	private Rectangle player; //player rectangle
	private Entity ePlayer; //player entity
	private int playerX, playerY; //player location
	
	private Line aimtracer; //line that traces where the shot will go
	private int pewCounter; //causes the line pew to disappear after a few seconds
	private Line pew; //line that indicates a shot
	
	private ArrayList<Enemy> enemies; //lots of enemy entities
	private ArrayList<Rectangle> enemySquares; //one enemy square for each
	
	private ArrayList<Entity> bullets;
	private ArrayList<Circle> bulletCircles;
	
	private boolean invincible; //win condition has been met
	private int attackTimer;
	private boolean lost;
	
	private int score;
	
	public Board() //constructor
	{
		board = new Room[7][10]; //70 spaces
		invincible = DEMO;
		attackTimer = 0;
		lost = false;
		score = 0;
	
		this.mapSetup(); //sets up the map
		this.setup(); //sets up everything else
		
		canvas = new Pane(); //initializes the canvas pane
		
		//sets up the room
		newRoom(currentr, currentc); //shows the current room
		
		//adds all objects to the pane
		this.getChildren().add(canvas);

		canvas.getChildren().add(aimtracer);
		canvas.getChildren().add(pew);
		canvas.getChildren().add(player);
		
		//turns on mouse handlers for the pane
		canvas.setOnMouseClicked(new MouseHandler()); //League of Legends movement 
		canvas.setOnMouseMoved(new MouseHandler()); //aim line
		
		//begins the timer process that moves everything around
		timerProcess();
	}
	
	private void setup()
	{
		//creates walls and sets their fill color to black
		northWall = new Rectangle(0, 0, 400, 10); northWall.setFill(Color.BLACK); 
		eastWall = new Rectangle(390, 0, 10, 400); eastWall.setFill(Color.BLACK);
		southWall = new Rectangle(0, 390, 400, 10); southWall.setFill(Color.BLACK);
		westWall = new Rectangle(0, 0, 10, 400); westWall.setFill(Color.BLACK);
		
		//sets the starting player location to the middle of the screen
		playerX = (400 - SQUARESIZE)/2; 
		playerY = (400 - SQUARESIZE)/2;
		
		//creates all player objects needed
		ePlayer = new Entity(PLAYERSPEED, playerX, playerY);
		player = new Rectangle(playerX, playerY, SQUARESIZE, SQUARESIZE); player.setFill(Color.BLACK);
			player.setStroke(Color.BLACK);
			
		//creates aim tracer and actual fire line
		aimtracer = new Line(playerX +10, playerY + 10, playerX, playerY);
		aimtracer.getStrokeDashArray().addAll(10d, 10d);
		pewCounter = 0;
		pew = new Line(-30, -30, -30, -30); //fire line is stored offscreen until needed
		pew.setStroke(Color.CYAN);
		pew.setStrokeWidth(5);
		
		bullets = new ArrayList<Entity>();
		bulletCircles = new ArrayList<Circle>();
	}
	
	private void timerProcess() //sets up and starts the timer
	{
		timeline = new Timeline(
				new KeyFrame(Duration.ZERO, actionEvent -> moveEverything()),
				new KeyFrame(Duration.millis(20))
				); //moves everything every 20 milliseconds (~50 frames per second)
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	private void moveEverything() //moves everything
	{
		attackTimer++;
		movePlayer(); //moves the player
		pew(); //removes the shot after a short while
		moveEnemies(); //moves all enemies
		allEnemiesAttack();
		moveBullets();
	}
	
	private void allEnemiesAttack()
	{
		for(int x = 0; x < enemies.size(); x++)
		{
			enemies.get(x).setTarget(playerX + 10, playerY + 10); //cycles through all enemies
			
			Entity potentialAdd = enemies.get(x).attack(attackTimer); //enemies attack
			if(potentialAdd != null) //the attack() method returns null if it's not the right time to attack
			{
				//creates a new bullet and a new circle to represent said bullet
				bullets.add(potentialAdd);
				Circle bulletCircle = new Circle(potentialAdd.getLocX(), potentialAdd.getLocY(), 5);
				bulletCircle.setFill(Color.BROWN);
				bulletCircles.add(bulletCircle); //radius 5 bullets
				canvas.getChildren().add(bulletCircle);
			}
		}
	}
	
	private void moveBullets() //moves the bullets towards their desired locations
	{
		for(int x = 0; x < bullets.size(); x++)
		{
			bullets.get(x).move();
			bulletCircles.get(x).setCenterX(bullets.get(x).getLocX()); //shifts the circles to reflect the movement
			bulletCircles.get(x).setCenterY(bullets.get(x).getLocY());
			
			double distanceX = Math.abs(bullets.get(x).getLocX() - (playerX + 10)); //gets distance from player
			double distanceY = Math.abs(bullets.get(x).getLocY() - (playerY + 10));
			
			if(distanceX <= 10 && distanceY <= 10 && !invincible) //bullet has hit the player
				lose();
		}
	}
	
	private void pew() //this is the timer that makes the line disappear a split second after the player shoots
	{
		if(pewCounter <= 10)
			pewCounter++;
		if(pewCounter == 10)
		{
			pew.setStartX(-30); pew.setEndX(-30); //stores the pew thing offscreen
			pew.setStartY(-30); pew.setEndY(-30); 
		}
	}
	
	private void moveEnemies()
	{
		for(int x = 0; x < enemies.size(); x++)
		{
			//changes the destination, then moves all enemies
			enemies.get(x).updateDestination(playerX, playerY); 
			enemies.get(x).move();
			
			//changes the squares to reflect where the enemies are
			enemySquares.get(x).setX(enemies.get(x).getLocX());
			enemySquares.get(x).setY(enemies.get(x).getLocY());
			
			double distanceX = Math.abs(enemies.get(x).getLocX() + 10 - (playerX + 10));
			double distanceY = Math.abs(enemies.get(x).getLocY() + 10 - (playerY + 10));
			
			if(distanceX <= 20 && distanceY <= 20 && !enemies.get(x).getDead() && !invincible)
				lose();
		}
	}
	
	private void movePlayer() //moves the player
	{
		//moves the player entity
		ePlayer.move();
		movePlayerTo(ePlayer.getLocX(), ePlayer.getLocY()); //moves all the things
		
		if(playerY <= 12 && board[currentr][currentc].getWallStatus()[0] == false) //want to move north & no wall
		{
			//the room shown becomes the room directly to the north
			this.newRoom(currentr - 1, currentc);
			movePlayerTo(ePlayer.getLocX(), 400 - SQUARESIZE - 15); //keeps player in the same spot it was before
			//														  but on the other side
			ePlayer.setDestination(ePlayer.getLocX(), ePlayer.getLocY()); //stops movement
		}
		//repeat for the remaining directions
		if(playerX >= 400 - SQUARESIZE - 12 && board[currentr][currentc].getWallStatus()[1] == false) //move east
		{
			movePlayerTo(15, ePlayer.getLocY());
			ePlayer.setDestination(ePlayer.getLocX(), ePlayer.getLocY());
			this.newRoom(currentr, currentc + 1);
		}
		if(playerY >= 400 - SQUARESIZE - 12 && board[currentr][currentc].getWallStatus()[2] == false) //move south
		{
			movePlayerTo(ePlayer.getLocX(), 15);
			ePlayer.setDestination(ePlayer.getLocX(), ePlayer.getLocY());
			this.newRoom(currentr + 1, currentc);
		}
		if(playerX <= 12 && board[currentr][currentc].getWallStatus()[3] == false) //move west
		{
			this.newRoom(currentr, currentc - 1);
			movePlayerTo(400 - SQUARESIZE - 15, ePlayer.getLocY());
			ePlayer.setDestination(ePlayer.getLocX(), ePlayer.getLocY()); //stops movement
		}
	}
	
	private void movePlayerTo(double x, double y)
	{
		//moves the player entity, sets the player location variables, moves the player square, moves the aim tracer
		ePlayer.setLocation(x, y); //moves player entity
		playerX = (int)x; //sets the player reference location
		playerY = (int)y;
		player.setX(x); //changes the location of the player square
		player.setY(y);
		aimtracer.setStartX(playerX + 10); //changes the location of the aim tracer
		aimtracer.setStartY(playerY + 10);
	}
	
	private void mapSetup() //sets up the map & maze of the board
	{
		//assign all rooms a number of enemies
		for(int r = 0; r < board.length; r++)
		{
			for(int c = 0; c < board[0].length; c++)
			{		
				board[r][c] = new Room((int)(Math.random() * 7)); //each room gets between 0 and 6 enemies
			}		
		}
		
//		System.out.print("creating maze\n");
		//create maze
		startr = 0; startc = 0;
		currentr = startr; currentc = startc;
		
		if(DEMO) //demo mode automatically sets goal to (1, 1) 
		{
			goalr = 1; goalc = 1;
		}
		else //randomizes the location of the goal to somewhere beyond at least (3, 6) in row/col or (6, 3) x/y
		{
			goalr = (int)(Math.random()*4) + 3;
			goalc = (int)(Math.random()*4) + 6;
		}
		
		//carves a path from the start to the goal
		while(currentr != goalr || currentc != goalc) //until the map creation reaches goal
		{
			int move = (int)(Math.random()*4);
			if(move == 0 && currentr > 0) //move north
			{
				board[currentr][currentc].removeWall(0); //remove north wall of room
				currentr--;
				board[currentr][currentc].removeWall(2); //remove south wall of next room
			}
			else if(move == 1 && currentc < BOARDC-1) //move east
			{
				board[currentr][currentc].removeWall(1); //remove wall east of room
				currentc++;
				board[currentr][currentc].removeWall(3); //remove west wall of next room
			}
			else if(move == 2 && currentr < BOARDR-1) //move south
			{				
				board[currentr][currentc].removeWall(2); //remove south wall of room
				currentr++;
				board[currentr][currentc].removeWall(0); //remove north wall of next room
			}
			else if(move == 3 && currentc > 0) //move west
			{
				board[currentr][currentc].removeWall(3); //remove wall west of room
				currentc--;;
				board[currentr][currentc].removeWall(1); //remove east wall of next room
			}
		}
		
		//resets currentr and currentc
		currentr = startr; currentc = startc;
		
		board[currentr][currentc].removeAllEnemies(); //removes all the enemies from starting room
		
		enemies = new ArrayList<Enemy>(); //resets the enemy attributes
		enemySquares = new ArrayList<Rectangle>();
		
		newRoomEnemies();
		
//		System.out.print("maze created - go!\n");
	} //end setup method
	
	private void newRoom(int r, int c) //player has entered a new room, display the new room
	{
		attackTimer = 0;
		currentr = r; currentc = c; //update current location values
		
		canvas.getChildren().removeAll(northWall, eastWall, southWall, westWall); //removes all walls

		//changes the color of all walls to reflect the current room color
		northWall.setFill(board[r][c].getColor());
		eastWall.setFill(board[r][c].getColor());
		southWall.setFill(board[r][c].getColor());
		westWall.setFill(board[r][c].getColor());
		
		//places back any walls that the room has
		boolean[] walls = board[r][c].getWallStatus();
		if(walls[0] == true) {canvas.getChildren().add(northWall);}
		if(walls[1] == true) {canvas.getChildren().add(eastWall);}
		if(walls[2] == true) {canvas.getChildren().add(southWall);}
		if(walls[3] == true) {canvas.getChildren().add(westWall);}
		
		//player has reached the goal location
		if(currentr == goalr && currentc == goalc)
		{
			//sets background color to green
			canvas.setBackground(new Background(new BackgroundFill(Color.LAWNGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
			
			//creates a new variable saying you won
			Label youWon = new Label("YOU WIN!!");
			youWon.setFont(new Font("Arial", 48));
			canvas.getChildren().add(youWon);
			youWon.setAlignment(Pos.CENTER);
			
			if(DEMO == false) //not demo mode
			{
				invincible = true; //makes the player invincible and adds 2000 to their score
				score += 2000;
				Runner.updateValues(getAllCoords(), getScore());
			}
		}
		
		//the enemies from the old room should no longer be present
		for(int x = 0; x < enemySquares.size(); x++)
		{
			canvas.getChildren().remove(enemySquares.get(x)); //removes all the shapes from the canvas
		}
		for(int y = 0; y < bulletCircles.size(); y++)
		{
			canvas.getChildren().remove(bulletCircles.get(y));
		}
		
		enemySquares.clear(); //clears all the lists
		bulletCircles.clear();
		bullets.clear();
		
		newRoomEnemies(); //resets the enemies for the new room
		
		Runner.updateValues(getAllCoords(), getScore()); //updates the coordinates
	}
	
	private void newRoomEnemies()
	{
		enemies = board[currentr][currentc].getEnemies();
		for(int x = 0; x < enemies.size(); x++)
		{
			//creates a new rectangle to represent the enemy
			Rectangle r = new Rectangle(enemies.get(x).locX, enemies.get(x).locY, SQUARESIZE, SQUARESIZE);
			
			switch(enemies.get(x).getType()) //each enemy gets a different color depending on type
			{
			case 0:
				r.setFill(Color.MEDIUMBLUE);
				break;
			case 1:
				r.setFill(Color.ORANGERED);
				break;
			case 2:
				r.setFill(Color.VIOLET);
				break;
			case 3:
				r.setFill(Color.LIGHTGREEN);
			}
			enemySquares.add(r);
		}
		
		for(int x = 0; x < enemySquares.size(); x++)
		{
			if(!enemies.get(x).getDead()) //adds the enemy as long as it's not dead
				canvas.getChildren().add(enemySquares.get(x));
		}
	}
	
	private void lose()
	{
		timeline.stop(); //stops all movement of entities
		
		//changes the background and tells the player they lost
		canvas.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
		Label youLost = new Label("You lost...");
		youLost.setFont(new Font("Arial", 48));
		canvas.getChildren().add(youLost);
		youLost.setAlignment(Pos.CENTER);
		
		//sets lost to true so that the aimline doesn't move either
		lost = true;
	}
	
	private double pointLineDistance(Line l, double pointX, double pointY) //distance between the line and a point
	{
		//get difference between start point and the point
		double startDx = pointX - l.getStartX();
		double startDy = pointY - l.getStartY();
		
		double endDx = pointX - l.getEndX();
		double endDy = pointY - l.getEndY();
		
		//get line vector
		double lineDx = l.getEndX() - l.getStartX();
		double lineDy = l.getEndY() - l.getStartY();
		
		//get the lengths of each of the traced lines (start to point, point to end, start to end)
		double startLength = Math.sqrt(startDx * startDx + startDy * startDy);
		double endLength = Math.sqrt(endDx * endDx + endDy * endDy);
		double lineLength = Math.sqrt(lineDx * lineDx + lineDy * lineDy);
		
		//add start and end values
		double totLength = startLength + endLength;
		
		//get the returned difference
		return Math.abs(totLength - lineLength);
	}

	private class MouseHandler implements EventHandler<MouseEvent>
	{
		public void handle(MouseEvent event)
		{
			 if(event.getButton() == MouseButton.SECONDARY && !lost) //right click
			 {
				 ePlayer.setDestination(event.getX(), event.getY()); //sets player destination
			 }
			 if(event.getEventType() == MouseEvent.MOUSE_MOVED && !lost) //mouse is moved
			 {
				 aimtracer.setEndX(event.getX()); //sets the aimtracer aim
				 aimtracer.setEndY(event.getY());
			 }
			 if(event.getButton() == MouseButton.PRIMARY && !lost) //left click, shoots
			 {
				 pew.setStartX(aimtracer.getStartX()); pew.setStartY(aimtracer.getStartY());
				 pew.setEndX(aimtracer.getEndX()); pew.setEndY(aimtracer.getEndY());
				 
				 //see if any of the enemies have been shot
				 for(int x = 0; x < enemies.size(); x++)
				 {
					 if(pointLineDistance(pew, enemies.get(x).getLocX() + 10, enemies.get(x).getLocY() + 10) <= 3)
					 {  //3 is an arbitrary value determined by trial and error, but an enemy is hit by this line
						 if(enemies.get(x).hit() == true) //enemy is hit, and if enemy dies is true
						 {
							 canvas.getChildren().removeAll(enemySquares.get(x));
							 score += 100;
							 Runner.updateValues(getAllCoords(), getScore());
						 }
					 }
				 }
				 
				 pewCounter = 0; //resets the pewcounter so it will turn off after a brief moment
			 }
		}
	}
	
	//getters
	public int[] getAllCoords() //returns the goalr, goalc, currentr, currentc
	{
		int[] coords = new int[]{currentc, currentr, goalc, goalr};
		return coords;
	}
	public int getScore()
	{
		return score;
	}
	
}
