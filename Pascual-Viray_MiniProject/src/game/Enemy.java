package game;

import java.util.Random;

import javafx.scene.image.Image;

public class Enemy extends Sprite {
	//////////////// ATTRIBUTES //////////////////

	public final static int INITIAL_SIZE = 40; //initial size of enemy 40px

	//dictates the image of the enemy corresponding to the enemy type
	public final static Image ENEMY1 = new Image ("images/ENEMY1.png");
	public final static Image ENEMY2 = new Image ("images/ENEMY2.png");
	public final static Image ENEMY3 = new Image ("images/ENEMY3.png");
	public final static Image ENEMY4 = new Image ("images/ENEMY4.png");
	public final static Image ENEMY5 = new Image ("images/ENEMY5.png");

	//dictates the direction of the enemy
	public boolean Up = false;
	public boolean Left = false;
	public boolean Right = false;
	public boolean Down = false;
	private final static int DIRECTIONS = 4;

	//stores times for animating
	public long currentNanoTime;
	public long overallTime;
	public long loopSec;
	public long startSpawn;
	public int motion;
	public int direction;
	public static long startMove;
	public static double duration;

	boolean isAlive;
	public static double ENEMY_SPEED;
	int speed;
	public int enemyID; //uniqe enemy id for monitoring
	public static boolean goSignal = false;


	////////////////   CONSTRUCTOR  //////////////////
	public Enemy(double xPos, double yPos, Image image) {
		super(xPos, yPos, image); //super keyword for instantiating superclass
		//initializes size of enemy
		this.width = this.INITIAL_SIZE;
		this.height = this.INITIAL_SIZE;
		this.updateSpeed(); //class update speed to get initialize speed of enemy
		this.enemyID = GameTimer.ENEMY_COUNT + 1; //enemy ID

		Random r = new Random();
		Random r2 = new Random();
		this.loopSec = r.nextInt(5)+1; //randomizes initial duration of movement
		this.direction = r2.nextInt(4); //randomizes initial direction for moving
		this.startSpawn = System.nanoTime(); //sets current time as startspwan time
		this.motion = 1;
		this.currentNanoTime = System.nanoTime();
	}


	////////////////  METHODS  //////////////////

	// method for updating speed of enemy blob using formula for speed
	void updateSpeed () {
		this.ENEMY_SPEED = 120/this.height; //stores speed in enemy_speed
	}

	// setter for duration for movement
	public void setDuration (double seconds) {
		this.duration = seconds;
	}

	//getter for enemy duration
	public double getDuration () {
		return this.duration;
	}

	//getter for enemy direction
	public int getDirection () {
		int direction;
		Random r = new Random ();
		direction = r.nextInt(Enemy.DIRECTIONS);

		return direction;
	}

	//method for moving enemy blob
	void moveEnemyBlob () {
		if(this.xPos+this.dx >= 0 && this.xPos+this.dx <= GameTimer.worldWidth-this.width)
			this.xPos += this.dx;

    	if (this.yPos+this.dy >= 0 && this.yPos+this.dy <= GameTimer.worldHeight-this.height)
    		this.yPos += this.dy;
	}

	//method for updating times for moving enemy
	public void moveEnemy (long currentNanoTime) {
		double elapsedTime = (currentNanoTime - this.startMove) / 1000000000.0;
		double endTime = elapsedTime + this.duration;

		//System.out.println("elapsed time: " + elapsedTime);
		//System.out.println("end time: " + endTime + "\n\n");

	}

	//method for cheking excess in enemy position
	void checkExcess () {
		double excess;

		if ((this.getYPos() + this.height) > GameTimer.worldHeight) {
			excess = GameTimer.worldHeight - (this.getYPos() + this.height);
			this.yPos = this.yPos + excess;
		}

		if ((this.getXPos() + this.width) > GameTimer.worldWidth) {
			excess = GameTimer.worldWidth - (this.getXPos() + this.height);
			this.xPos = this.xPos + excess;
		}
	}

	//method for eating food
	void eatFood () {
		//double excess;
		//updates size of enemy based on food
		this.width = this.width + Food.FOOD_EFFECT;
		this.height = this.height + Food.FOOD_EFFECT;

		this.checkExcess();
	}

	//method cwhen an enemy eats another enemy
	void eatEnemy (Enemy enemy) {
		//updates sie of enemy based on size of eaten enemy
		this.width = this.width + enemy.width;
		this.height = this.height + enemy.height;

		this.checkExcess();
	}

	//method for checking enemy collision with another sprite object
	boolean checkCollision (Sprite sprite) {
		//returns true or false when an enemy object collides with another object
		if (this.collidesWith(sprite)) {
			return true;
		} else {
			return false;
		}
	}

}
