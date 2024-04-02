package game;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class Player extends Sprite{
	// attributes

	private String name;

	// for checking if it is alive
	private boolean alive;

	// items timer
	private ItemsTimer timer;

	// for immunity if starfish is collected
	private boolean immunity;

	// player speed
	public static double PLAYER_SPEED;

	// image of blob
	private final static Image PLAYER_IMAGE = new Image ("images/blob.png");

	// initial x and y at the center of the window
	private final static double INITIAL_X = 400;
	private final static double INITIAL_Y = 400;

	private final static double INITIAL_SIZE = 40;

	private boolean pearlPowerUp = false;


	private int itemType = -1;

	Player(String name) {

		// super class from sprite
		super(Player.INITIAL_X, Player.INITIAL_Y, Player.PLAYER_IMAGE);

		this.name = name;
		this.alive = true;

		// sets height and width to initial size
		this.height = this.INITIAL_SIZE;
		this.width = this.INITIAL_SIZE;


		// makes x and y pos of blob at the center of the window
		this.updateScreenXY();

		// update speed using the formula
		this.updateSpeed();


	}

	// methods for items
	public int getItemType () {
		return this.itemType;
	}

	public void setItemTypePearl () {
		this.itemType = 0;
	}

	public void setItemTypeStarfish () {
		this.itemType = 1;
	}

	public void setImmunityTrue () {
		this.immunity = true;
	}

	public void setImmunityFalse () {
		this.immunity = false;
	}

	public boolean getImmunity () {
		return this.immunity;
	}

	// method for making x and y pos of blob at the center of the window
	void updateScreenXY () {
		this.xPos = (int) (GameStage.WINDOW_HEIGHT/2) - (this.width/2);
		this.yPos = (int) (GameStage.WINDOW_HEIGHT/2) - (this.height/2);
	}

	void setScreenX () {
		this.xPos = (int) (GameStage.WINDOW_HEIGHT/2) - (this.width/2);
	}

	// compute speed using formula
	void updateSpeed() {
		this.PLAYER_SPEED = 120/this.height;
	}

	// method for moving player blob
	void move() {
    	if(this.xPos+this.dx >= 0 && this.xPos+this.dx <= GameStage.MAP_HEIGHT-this.width)
			this.xPos += this.dx;

    	if (this.yPos+this.dy >= 0 && this.yPos+this.dy <= GameStage.MAP_HEIGHT-this.height)
    		this.yPos += this.dy;
	}

	// for drawing the blob more efficiently if it increases in size
	void checkExcess () {
		double excess;

		if ((this.getYPos() + this.height) > GameStage.MAP_HEIGHT) {
			excess = GameStage.MAP_HEIGHT - (this.getYPos() + this.height);
			this.yPos = this.yPos + excess;
		}

		if ((this.getXPos() + this.width) > GameStage.MAP_HEIGHT) {
			excess = GameStage.MAP_HEIGHT - (this.getXPos() + this.height);
			this.xPos = this.xPos + excess;
		}
	}

	// method for increasing size if blob eats food
	void eatFood () {
		//double excess;
		this.width = this.width + Food.FOOD_EFFECT;
		this.height = this.height + Food.FOOD_EFFECT;

		this.checkExcess();
	}

	// method for increasing size if blob eats another enemy
	void eatEnemy (Enemy enemy) {
		this.width = this.width + enemy.width;
		this.height = this.height + enemy.height;

		this.checkExcess();
	}

	// method for increasing size if blob gets pearl powerup
	void increaseSpeed () {

		this.PLAYER_SPEED = this.PLAYER_SPEED * Pearl.SPEED_UPGRADE;
		this.timer = new ItemsTimer(this);
		this.timer.start();
	}

	// method for decreasing speed after powerup
	void decreaseSpeed () {
		this.PLAYER_SPEED = this.PLAYER_SPEED / Pearl.SPEED_UPGRADE;
	}

	// method for immunity if blob picks up starfish powerup
	void immunity () {
		this.immunity = true;
		this.timer = new ItemsTimer(this);
		this.timer.start();
	}

	// method when blob dies
	void die () {
		this.alive = false;
	}

	// checks if blob is alive
	boolean isAlive () {
		return this.alive;
	}
}
