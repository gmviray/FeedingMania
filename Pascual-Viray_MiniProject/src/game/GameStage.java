package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import game.About;
//import HomePage.Instructions;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.scene.ImageCursor;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class GameStage {
	// window height and width (800x800)
	public final static int WINDOW_HEIGHT = 800;
	// map height and width (2400x2400)
	public final static int MAP_HEIGHT = 2400;

	// root
	private Group root;

	// stage
	private Stage stage;

	// welcome scene
	private Scene splashScene;

	// game scene
	private Scene gameScene;

	// for graphics
	private Canvas canvas;

	// icon
	private final static Image ICON = new Image ("images/fish.png", 500, 500, false, false);

	// welcome page
	private final static Image BACKGROUND = new Image ("images/INTRO.png", 800, 800, false, false);

	public GameStage() {
		// initializes attributes

		// canvas with the size of the window
		this.canvas = new Canvas(GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);

		// initializes root and adds canvas as its child
		this.root = new Group();
		this.root.getChildren().add(this.canvas);

		this.gameScene = new Scene(root);
	}

	// sets stage
	public void setStage(Stage  stage) {
		this.stage = stage;

		stage.setTitle("Feeding Mania!");
		this.stage.getIcons().add(ICON);

		this.initSplash(stage);

		stage.setScene(this.splashScene);
		stage.setResizable(false);
		stage.show();
	}

	// creates splash scene
	private void initSplash (Stage stage) {

		// for stacking elements
		StackPane root = new StackPane();

		// calls create canvas (makes bg), and createVBox for the buttons
		root.getChildren().addAll(this.createCanvas(), this.createVBox());
		this.splashScene = new Scene(root);
	}

	// method for drawing the background
	private Canvas createCanvas () {
		Canvas canvas = new Canvas(GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		gc.drawImage(this.BACKGROUND, 0, 0);
		return canvas;
	}

	// method for creating buttons for new game, about, and instructions
	private VBox createVBox() {

		// for buttons in center
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(15);

		// button for start game
		Button b1 = new Button ();

		Image startimg = new Image ("images/1.png");
		ImageView startView = new ImageView();
		startView.setImage(startimg);
		startView.setFitHeight(50);
		startView.setFitWidth(500);

		b1.setGraphic(startView);
		b1.setOnAction(start);

		// button for about
		Button b2 = new Button ();

		Image aboutimg = new Image ("images/2.png");
		ImageView aboutView = new ImageView();
		aboutView.setImage(aboutimg);
		aboutView.setFitHeight(50);
		aboutView.setFitWidth(500);

		b2.setGraphic(aboutView);
		b2.setOnAction(about);

		// button for instructions
		Button b3 = new Button ();

		Image instrucimg = new Image ("images/3.png");
		ImageView instrucView = new ImageView();
		instrucView.setImage(instrucimg);
		instrucView.setFitHeight(50);
		instrucView.setFitWidth(500);

		b3.setGraphic(instrucView);
		b3.setOnAction(instruc);


		// adds buttons to vbox
		vbox.getChildren().add(b1);
		vbox.getChildren().add(b2);
		vbox.getChildren().add(b3);

		return vbox;
	}

	// sets stage of about class if button is clicked
	private EventHandler<ActionEvent> about = new EventHandler<ActionEvent>(){
		 public void handle(ActionEvent e) {
			About about = new About();
			about.setStage(stage);
		 }
	 };

	 // sets stage of instructions class if button is clicked
	 private EventHandler<ActionEvent> instruc = new EventHandler<ActionEvent>(){
		 public void handle(ActionEvent e) {
			 Instructions instruc = new Instructions();
			 instruc.setStage(stage);
		 }
	 };

	 // starts gametimer class if start button is clicked
	 private EventHandler<ActionEvent> start = new EventHandler<ActionEvent>(){
		 public void handle(ActionEvent e) {
			 setGame(stage);
		 }
	 };

	 // starts game
	 void setGame (Stage stage) {
		 // changes splash scene to game scene
		 stage.setScene(this.gameScene);

		 GraphicsContext gc = this.canvas.getGraphicsContext2D();

		 // starts game timer
		 GameTimer gameTimer = new GameTimer (gameScene, gc);
		 gameTimer.start();
	 }
}