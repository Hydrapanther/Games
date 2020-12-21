// Assignment #: Honors CSE 205
//         Name: Lucas Yang
//    StudentID: 1219127442
//      Lecture: MWF 8:35am-9:25am
//  Description: This is the running class used to work with the stages. Also has a small GUI to keep
//				 track of player points and location data.

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Runner extends Application
{
	private static Label currentCoords;
	private static Label goalCoords;
	private static Label score;
	
	private Board board;
	private StackPane rootPane;
	
	public void start(Stage arg0)
	{
	      currentCoords = new Label();
	      goalCoords = new Label();
	      score = new Label();
		
		  //create a Board object
	      board = new Board();
	   
	      //put gui on top of the rootPane
	      rootPane = new StackPane();
	      rootPane.getChildren().add(board);
	   
	      // Create a scene and place rootPane in the stage
	      Scene scene = new Scene(rootPane, 400, 400);
	      arg0.setTitle("Adventure++"); 
	      arg0.setScene(scene); // Place the scene in the stage
	      
	      VBox sidePane = new VBox();
	      		sidePane.setPadding(new Insets(10, 10, 10, 10));
	      		sidePane.setSpacing(30);
	      Stage stage2 = new Stage();
	      Scene scene2 = new Scene(sidePane, 200, 300);
	      stage2.setScene(scene2);
	      
	      sidePane.getChildren().addAll(currentCoords, goalCoords, score);
	      
	      stage2.setX(835);
	      stage2.setY(94);
	      
	      stage2.show();
	      arg0.show(); // Display the stage, arg0 is second so it auto-selects it
	      
	      stage2.setAlwaysOnTop(true); //stages cannot be clicked off of by accident 
	      arg0.setAlwaysOnTop(true);
	}
		
	public static void main(String[] args)
	{
		launch(args);
	}
	
	public static void updateValues(int[] coords, int sc) //updates all the labels with the right score and coordinates
	{
		currentCoords.setText("Current location: (" + coords[0] + ", " + coords[1] + ")");
		goalCoords.setText("Goal location: (" + coords[2] + ", " + coords[3] + ")");
		score.setText("Score: " + sc);
	}
}
