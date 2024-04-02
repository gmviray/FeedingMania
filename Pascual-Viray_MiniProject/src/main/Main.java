package main;

import game.GameStage;
import javafx.application.Application;
import javafx.stage.Stage;

// calls launch
public class Main extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		// creates new instance of game stage then calls method set stage
		GameStage theGameStage = new GameStage();
		theGameStage.setStage(stage);
	}
}
