package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import game.About;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.ImageCursor;
import javafx.scene.control.Button;

public class About {
	// used for graphics
	private Canvas canvas;
	private static GraphicsContext gc;

	// arranges elements in a stack
	private StackPane pane;

	private Scene scene;
	private Stage stage;

	// page counter
	private static int COUNTER = 1;

	// icon
	private final static Image ICON = new Image ("images/fish.png", 500, 500, false, false);

	// images per page
	private final static Image ABT1 = new Image ("images/ABOUT1.png", 800, 800, false, false);
	private final static Image ABT2 = new Image ("images/ABOUT2.png", 800, 800, false, false);
	private final static Image ABT3 = new Image ("images/ABOUT3.png", 800, 800, false, false);
	private final static Image ABT4 = new Image ("images/ABOUT4.png", 800, 800, false, false);

	About () {
		// sets canvas by the size of the window
		this.canvas = new Canvas (GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);
		this.gc = canvas.getGraphicsContext2D();

		// initializes stack pane
		this.pane = new StackPane();

		// creates scene by the size of the window
		this.scene = new Scene (pane, GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);
	}

	// method for setting the stage
	public void setStage (Stage stage) {
		// initializes stage
		this.stage = stage;

		// sets icon
		stage.getIcons().add(ICON);

		// sets title
		this.stage.setTitle("About the Game");

		// sets scene
		this.stage.setScene(this.scene);

		// shows stage to window
		this.stage.show();

		// calls method that sets properties
		this.setProperties();
	}

	// method to set properties
	private void setProperties () {

		// in page 1 initially
		About.gc.drawImage(About.ABT1, 0, 0);

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

		// sets event handler for every click
		home.setOnAction(backToHome);
		next.setOnAction(nextPage);
		prev.setOnAction(prevPage);

		// adds nodes to pane
		this.pane.getChildren().add(canvas);
		this.pane.getChildren().add(next);
		this.pane.getChildren().add(prev);
		this.pane.getChildren().add(home);
	}

	// returns to welcome scene when home button is clicked
	private EventHandler<ActionEvent> backToHome = new EventHandler<ActionEvent>(){
		 public void handle(ActionEvent e) {
			GameStage home = new GameStage();
			home.setStage(stage);
		 }
	 };

	 // switch to next page when next page button is clicked
	 private EventHandler<ActionEvent> nextPage = new EventHandler<ActionEvent>(){
		 private final int MAX_PAGES = 4;
		 public void handle(ActionEvent e) {
			if (About.COUNTER < this.MAX_PAGES) {
				About.COUNTER ++;
			}

			About.turnPages();
		 }
	 };

	 // returns to previous page when prev page button is clicked
	 private EventHandler<ActionEvent> prevPage = new EventHandler<ActionEvent>(){
		 private final int MIN_PAGES = 1;
		 public void handle(ActionEvent e) {
			if (About.COUNTER > this.MIN_PAGES) {
				About.COUNTER --;
			}

			About.turnPages();
		 }
	 };

	 private static void turnPages () {
		 switch (About.COUNTER) {
		 case 1:
			 About.gc.drawImage(About.ABT1, 0, 0);
			 break;
		 case 2:
			 About.gc.drawImage(About.ABT2, 0, 0);
			 break;
		 case 3:
			 About.gc.drawImage(About.ABT3, 0, 0);
			 break;
		 case 4:
			 About.gc.drawImage(About.ABT4, 0, 0);
			 break;
		 }
	 }
}
