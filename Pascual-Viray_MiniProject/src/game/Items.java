package game;

import javafx.scene.image.Image;

abstract class Items extends Sprite {

	////////////////  ATTRIBUTES //////////////////
	public boolean isClaimed;
	public boolean pauseAppear;
	//stores times for animating
	public long startSpawnItem;
	public long overallTime;

	//dictates what type of item it is
	private int itemType = -1;

	//static attributes dictates the image of the item corresponding to the item type
	public final static Image ITEM1 = new Image ("images/ITEM1.png");
	public final static Image ITEM2 = new Image ("images/ITEM2.png");

	//initial size of item
	public final static int INITIAL_SIZE = 30;

	/////////////////////////   CONSTRUCTOR   ////////////////////////
	public Items(double xPos, double yPos, Image image) {
		super(xPos, yPos, image); //super key word to call constructor of superclass
		//initializes size of item
		this.width = Items.INITIAL_SIZE;
		this.height = Items.INITIAL_SIZE;
		//sets attributes to false
		this.isClaimed = false;
		this.pauseAppear = false;
		//stores current time in spawntime
		this.startSpawnItem = System.nanoTime();

	}

	////////////////   METHOD  /////////////////

	//abstract method for checking collision
	abstract void checkCollision (Player player);
}
