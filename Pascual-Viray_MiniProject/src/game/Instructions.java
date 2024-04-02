package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;

public class Instructions {

	// attributes

	// for graphics
	private Canvas canvas;
	private static GraphicsContext gc;

	// nodes
	private StackPane pane;
	private Scene scene;
	private Stage stage;

	// page counter
	private static int COUNTER = 1;

	// icon
	private final static Image ICON = new Image ("images/fish.png", 500, 500, false, false);

	// images
	private final static Image INSTRUC1 = new Image ("images/INSTRUC1.png", 800, 800, false, false);
	private final static Image INSTRUC2 = new Image ("images/INSTRUC2.png", 800, 800, false, false);
	private final static Image INSTRUC3 = new Image ("images/INSTRUC3.png", 800, 800, false, false);
	private final static Image INSTRUC4 = new Image ("images/INSTRUC4.png", 800, 800, false, false);

	Instructions () {
		// initializes attributes

		// sets canvas by the size of the window
		this.canvas = new Canvas (GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		// initializes stack pane
		this.pane = new StackPane();

		// creates scene by the size of the window
		this.scene = new Scene (pane, GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);
	}

	// sets stage
	public void setStage (Stage stage) {
		this.stage = stage;

		stage.getIcons().add(ICON);
		this.stage.setTitle("Game Instructions");

		// sets scene to instructions scene
		this.stage.setScene(this.scene);
		this.stage.show();

		// sets properties
		this.setProperties();
	}

	// method for setting properties
	private void setProperties () {
		// initially at page 1
		Instructions.gc.drawImage(Instructions.INSTRUC1, 0, 0);

		// next button
		Image nextButton = new Image ("images/next.png");
		ImageView nextView = new ImageView();
		nextView.setImage(nextButton);
		nextView.setFitHeight(40);
		nextView.setFitWidth(40);

		Button next = new Button();
		next.setGraphic(nextView);
		next.setTranslateX(110);
		next.setTranslateY(250);

		// previous button
		Image prevButton = new Image ("images/prev.png");
		ImageView prevView = new ImageView();
		prevView.setImage(prevButton);
		prevView.setFitHeight(40);
		prevView.setFitWidth(40);

		Button prev = new Button();
		prev.setGraphic(prevView);
		prev.setTranslateX(-110);
		prev.setTranslateY(250);

		// home button
		Image homeButton = new Image ("images/home button.png");
		ImageView homeView = new ImageView();
		homeView.setImage(homeButton);
		homeView.setFitHeight(60);
		homeView.setFitWidth(110);

		Button home = new Button();
		home.setGraphic(homeView);
		home.setTranslateY(250);

		// adds event handlers
		home.setOnAction(backToHome);
		next.setOnAction(nextPage);
		prev.setOnAction(prevPage);

		// adds nodes to pane
		this.pane.getChildren().add(canvas);
		this.pane.getChildren().add(next);
		this.pane.getChildren().add(prev);
		this.pane.getChildren().add(home);
	}

	// returns to home
	private EventHandler<ActionEvent> backToHome = new EventHandler<ActionEvent>(){
		 public void handle(ActionEvent e) {
			GameStage home = new GameStage();
			home.setStage(stage);
		 }
	 };

	 // turn pages if next button is clicked
	 private EventHandler<ActionEvent> nextPage = new EventHandler<ActionEvent>(){
		 private final int MAX_PAGES = 4;
		 public void handle(ActionEvent e) {
			if (Instructions.COUNTER < this.MAX_PAGES) {
				Instructions.COUNTER ++;
			}

			Instructions.turnPages();
		 }
	 };

	 // returns to previous page if prev button is clicked
	 private EventHandler<ActionEvent> prevPage = new EventHandler<ActionEvent>(){
		 private final int MIN_PAGES = 1;
		 public void handle(ActionEvent e) {
			if (Instructions.COUNTER > this.MIN_PAGES) {
				Instructions.COUNTER --;
			}

			Instructions.turnPages();
		 }
	 };

	 //method for turning page, draws image depending on page counter
	 private static void turnPages () {
		 switch (Instructions.COUNTER) {
		 case 1:
			 Instructions.gc.drawImage(Instructions.INSTRUC1, 0, 0);
			 break;
		 case 2:
			 Instructions.gc.drawImage(Instructions.INSTRUC2, 0, 0);
			 break;
		 case 3:
			 Instructions.gc.drawImage(Instructions.INSTRUC3, 0, 0);
			 break;
		 case 4:
			 Instructions.gc.drawImage(Instructions.INSTRUC4, 0, 0);
			 break;
		 }
	 }
}
