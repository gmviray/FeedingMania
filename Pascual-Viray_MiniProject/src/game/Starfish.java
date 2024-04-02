package game;

import javafx.scene.image.Image;

class Starfish extends Items {

	////////////////	ATTRIBUTRES   ////////////////
	public final static int GET_TYPE = 2; //dictates what type of item this is (starfish)

	////////////////	CONSTRUCTOR  /////////////////
	public Starfish(double xPos, double yPos) {
		super(xPos, yPos, Items.ITEM2);  //calls superclass constructor with parameters (positions and type)
	}

	////////////////METHODS  /////////////////

	@Override
	//overriden method in items superclass
	void checkCollision(Player player) {
		if (this.collidesWith(player)) { //checks if player collides with item
			player.setItemTypeStarfish();
			this.vanish(); //item vanishes since it is collected
			player.setImmunityTrue(); //sets immunity of player to true
		}
	}
}
