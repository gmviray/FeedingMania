package game;

import javafx.scene.image.Image;

class Food extends Sprite{

	// final images
	public final static Image BLUE_FOOD = new Image("images/food_blue.png");
	public final static Image GREEN_FOOD = new Image("images/food_green.png");
	public final static Image ORANGE_FOOD = new Image("images/food_orange.png");
	public final static Image PURPLE_FOOD = new Image("images/food_purple.png");
	public final static Image YELLOW_FOOD = new Image("images/food_yellow.png");

	// food size
	public final static int FOOD_SIZE = 20;

	// increase in size of the blob when food is eaten
	public final static int FOOD_EFFECT = 10;


	public Food(double xPos, double yPos, Image img) {
		// superclass constructor for x and y pos and img values passed
		super(xPos, yPos, img);

		// food size is constant
		this.width = Food.FOOD_SIZE;
		this.height = Food.FOOD_SIZE;
	}

	// checks collision with another sprite, could be a player blob or an enemy blob
	// returns true if they collide, false if not
	boolean checkCollision (Sprite sprite) {
		if (this.collidesWith(sprite)) {
			return true;
		} else {
			return false;
		}
	}


}
