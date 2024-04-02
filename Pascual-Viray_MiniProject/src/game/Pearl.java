package game;

import javafx.scene.image.Image;

class Pearl extends Items {

	////////////////    ATTRIBUTRES   ////////////////

	public final static int GET_TYPE = 1; //dictates what type of item this is (pearl)
	public final static int SPEED_UPGRADE = 2; //stores speed multiplier since pealr items doubles the speed on a blob

	////////////////  CONSTRUCTOR  /////////////////
	Pearl (double x, double y) {
		super (x, y, Items.ITEM1); //calls superclass constructor with parameters (positions and type)
	}

	////////////////METHODS  /////////////////

	@Override
	//overriden method in items superclass
	void checkCollision(Player player) {
		if (this.collidesWith(player)) { //checks if player collides with item
			player.setItemTypePearl();
			this.vanish(); //item vanishes since it is collected
			player.increaseSpeed(); //increases speed of player
		}
	}
}
