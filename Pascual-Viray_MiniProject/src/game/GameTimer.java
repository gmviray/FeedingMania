package game;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;




class GameTimer extends AnimationTimer {
	// WORLD SETTINGS
	public final static int worldWidth = 2400;
	public final static int worldHeight = 2400;

	// for graphhics
	private GraphicsContext gc;

	// player blob
	private Player player;

	// scene
	private Scene scene;

	// directions used by the player blob
	private static boolean goLeft;
	private static boolean goRight;
	private static boolean goDown;
	private static boolean goUp;

	// x and y position of the background, used for moving the bg
	private double backgroundX;
	private double backgroundY;

	// array list of food, enemies, and items
	private ArrayList<Food> allFood;
	private static ArrayList<Enemy> allEnemies;
	private ArrayList<Items> allItems;

	// max enemy is 10, enemy count is for making sure that 10 enemies are
	// put in the map
	public final static int MAX_ENEMY = 10;
	public static int ENEMY_COUNT = 0;

	// for plotting the enemy (initial position) and food to the map
	public final static int MAX_XYPOS = 2400;
	public final static int MIN_XYPOS = 0;

	// for the variation of designs
	public final static int MAX_FOODTYPE = 5;
	public final static int MAX_ENEMYTYPE = 5;

	// for moving the enemy
	public final static int MAX_DIRECTIONS = 4;

	// maximum number of food on map
	public final static int MAX_FOOD = 50;
	// for printing food onto the map
	public static int FOOD_COUNT = 0;

	// for making items appear
	private boolean firstItem = true;
	public static double ITEM_DELAY = 10;
	public final static double IF_UNCLAIMED = 5;
	private long startItemsAppear;

	// for game stats
	private long timeLong;
	private int foodEaten = 0;
	private int blobsEaten = 0;

	// to check whether the game is over or not (when enemyCount = 0)
	private int enemyCount = 10;

	// background image
	private Image background = new Image ("images/underwater.png", GameTimer.worldWidth+400, GameTimer.worldHeight+400, false, false);

	GameTimer (Scene scene, GraphicsContext gc) {
		this.gc = gc;
		this.scene = scene;

		// initializes the player
		this.player = new Player("Puck");

		// initializes arraylists for food, enemy, and items
		this.allFood = new ArrayList<Food>();
		this.allEnemies = new ArrayList<Enemy>();
		this.allItems = new ArrayList<Items>();

		// sets start timer for moving enemies
		for (Enemy enemy : this.allEnemies) {
			enemy.startMove = System.nanoTime();
		}

		// sets time for duration and printing items
		this.startItemsAppear = this.timeLong = System.nanoTime();

		// prints food and enemies
		this.initializeFood();
		this.initializeEnemy();

		// calls method for detecting event handlers like the keys wasd
		this.prepareActionHandlers();

	}

	@Override
	// calls again and again
	public void handle(long currentNanoTime) {

		// calls method for redrawing the background according to its bgX and bgY
		this.redrawBackgroundImage();

		// checks time for making items appear and puts items every 10 secs
		this.makeItemsAppear(currentNanoTime);

		// moving sprites and rendering them to the map
		this.moveSprites(currentNanoTime);
		this.renderSprites();

		// methods for checking collisions
		this.checkFoodCollision();
		this.checkEnemyCollision();
		this.checkItemCollision();

		// draw game stats at the top of the screen
		this.drawStats(currentNanoTime);

		// checks if the player is still alive, if not, game ends
		if (!(this.player.isAlive())) {
			// game timer ends
			this.stop();
			// draws the final game stats
			this.drawGameOver(currentNanoTime);
		}

		// if all enemies are eaten or the enemy count is already 0,
		// game timer ends and final game stats is drawn
		if (this.blobsEaten == 10 || this.enemyCount == 0) {
			this.stop();
			this.drawWinner(currentNanoTime);
		}
	}

	// method for redrawing the background image
	void redrawBackgroundImage () {
		// clears the graphic content of the window
		this.gc.clearRect(0, 0, GameStage.WINDOW_HEIGHT, GameStage.WINDOW_HEIGHT);

		// redraws the background image
		this.gc.drawImage(background, this.backgroundX, this.backgroundY);
	}


	//Method for making items (power ups) appear and disappear
	private void makeItemsAppear (long currentNanoTime) {
		double startTime = (currentNanoTime - this.startItemsAppear) / 1000000000.0;
		if(startTime > GameTimer.ITEM_DELAY){ //checks if time is more than the initial 10 seconds delay
			if(this.firstItem){ //checks if this is the first instance
				this.pearlItem();
				this.starItem();
				this.firstItem = false;
			}else{ //if instance is not the first time
				long overallTime = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime - this.startItemsAppear);
				//creates item every 10 seconds using modulo operator
				if((overallTime)%10 == 0){
					if(this.allItems.size() == 0){
						this.pearlItem();
						this.starItem();
					}
				//removes item every 5 seconds using modulo operator
				}if(((overallTime)%5 == 0) && ((overallTime)%10 !=0)){
					for (int i = 0; i < this.allItems.size(); i++) { //for loop for removing all items in itemlist
						Items item = this.allItems.get(i);
						this.allItems.remove(i);
					}
				}
			}
		}
	}

	// method for initializing a pearl collectible
	void pearlItem () {
		Random r = new Random ();

		int xPos, yPos;

		// gets a random position on the map by generating a random number
		xPos = r.nextInt(GameTimer.MAX_XYPOS);
		yPos = r.nextInt(GameTimer.MAX_XYPOS);

		// initializes pearl collectible through the generated x and y positions
		Items pearlCollectible = new Pearl(xPos, yPos);

		// adds it to the arraylist of items
		this.allItems.add(pearlCollectible);
	}

	// method for initializing a starfish item
	void starItem () {
		Random r = new Random ();

		int xPos, yPos;

		// gets a random position on the map by generating a random number
		xPos = r.nextInt(GameTimer.MAX_XYPOS);
		yPos = r.nextInt(GameTimer.MAX_XYPOS);

		// initializes starfish collectible through the generated x and y positions
		Items starCollectible = new Starfish(xPos, yPos);

		// adds it to the array list of items
		this.allItems.add(starCollectible);
	}

	// calls render method found in the sprite class for every element on the map
	void renderSprites () {
		// draws player blob to the map
		this.player.render(this.gc);

		// draws enemy blobs to the map
		for (Enemy enemy : this.allEnemies)
			enemy.render(this.gc);

		// draws food to the map
		for (Food food : this.allFood)
			food.render(this.gc);

		// draws items to the map
		for (Items item : this.allItems)
			item.render(this.gc);
	}

	// method for moving elements found in the map
	void moveSprites (long currentNanoTime) {
		// makes the player blob always at the center of the window
		this.player.updateScreenXY();

		// calls method to move the player blob
		// (bg, food, and enemies will move but player blob will remain at the center)
		// gives an illusion that the player blob is moving
		this.movePlayer();

		// for all enemies, calls method for moving enemies
		for (int i=0; i < GameTimer.allEnemies.size(); i++){
			Enemy enemy = GameTimer.allEnemies.get(i);
			this.moveEnemies(currentNanoTime, enemy);
		}
	}

	// method for detecting action handlers (key press and key release)
	private void prepareActionHandlers () {

		// key press, sets boolean values of player's directions to true if pressed
		this.scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();

				if (code.equals("A")) {
					GameTimer.goLeft = true;
				} else if (code.equals("D")) {
					GameTimer.goRight = true;
				} else if (code.equals("W")) {
					GameTimer.goUp = true;
				} else if (code.equals("S")) {
					GameTimer.goDown = true;
				}

			}

		});

		// key release, sets boolean values of player's directions to false if key is released
		this.scene.setOnKeyReleased(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent e) {
				String code = e.getCode().toString();

				if (code.equals("A")) {
					GameTimer.goLeft = false;
				} else if (code.equals("D")) {
					GameTimer.goRight = false;
				} else if (code.equals("W")) {
					GameTimer.goUp = false;
				} else if (code.equals("S")) {
					GameTimer.goDown = false;
				}

			}

		});
	}

	// method for moving player but through illusion only
	private void movePlayer() {
		// based on which key is pressed, the boolean values are changing due to the method found above
		// each if statement checks the boolean values, and for every condition, there is a series of
		// methods that are called

		// these methods move the background, food, enemies, and items based on the speed of the player
		// through this, the player blob will look like it is moving when really, the elements around it
		// are those that are moving

		if (GameTimer.goLeft) {
			if (this.backgroundX < 0) {
				this.backgroundX += this.player.PLAYER_SPEED;

				for (Food food : this.allFood)
					food.xPos += this.player.PLAYER_SPEED;

				for (Enemy enemy : this.allEnemies)
					enemy.xPos += this.player.PLAYER_SPEED;

				for (Items item : this.allItems)
					item.xPos += this.player.PLAYER_SPEED;
			}

			this.player.setDX(-this.player.PLAYER_SPEED);

		} else if (GameTimer.goRight) {
			if (this.backgroundX > -2000) {
				this.backgroundX -= this.player.PLAYER_SPEED;
				for (Food food : this.allFood)
					food.xPos -= this.player.PLAYER_SPEED;

				for (Enemy enemy : this.allEnemies)
					enemy.xPos -= this.player.PLAYER_SPEED;

				for (Items item : this.allItems)
					item.xPos -= this.player.PLAYER_SPEED;
			}

			this.player.setDX(this.player.PLAYER_SPEED);
		} else if (GameTimer.goUp) {
			if (this.backgroundY < 0) {
				this.backgroundY += this.player.PLAYER_SPEED;

				for (Food food : this.allFood)
					food.yPos += this.player.PLAYER_SPEED;

				for (Enemy enemy : this.allEnemies)
					enemy.yPos += this.player.PLAYER_SPEED;

				for (Items item : this.allItems)
					item.yPos += this.player.PLAYER_SPEED;
			}

			this.player.setDY(-this.player.PLAYER_SPEED);
		} else if (GameTimer.goDown) {
			if (this.backgroundY > -2000 || (this.backgroundY > -2000 && this.backgroundX >= 0  )) {
				this.backgroundY -= this.player.PLAYER_SPEED;

				for (Food food : this.allFood)
					food.yPos -= this.player.PLAYER_SPEED;

				for (Enemy enemy : this.allEnemies)
					enemy.yPos -= this.player.PLAYER_SPEED;

				for (Items item : this.allItems)
					item.yPos -= this.player.PLAYER_SPEED;
			}

			this.player.setDY(this.player.PLAYER_SPEED);
		} else {
			this.player.setDX(0);
			this.player.setDY(0);

		}

		this.player.move();

		// called methods setDX, setDY, and move are attempts to move at the sides of the map
	}

	//Private Method for moving the enemies in a random direction for a random number of seconds
	private void moveEnemies(long currentNanoTime, Enemy enemy) {
		double elapsedTime;
		double endTime;
		//this portion of the code is for setting the direction of the enemies in which they will move
		Random rr = new Random(); //new random rr
		int direction = rr.nextInt(GameTimer.MAX_DIRECTIONS); //initializes direction variable to a random number from 0 to 3
		enemy.direction = direction; //this sets the direction attribute in the enemy object
		//checks if enemy is in motion, if not in motion, the following code will run
		if(enemy.motion == 1){
			if (enemy.direction == 0) { // if 0, set direction to down
				enemy.Down = true;
			} else if (enemy.direction == 1) { //if 1, set direction to left
				enemy.Left = true;
			} else if (enemy.direction == 2) { //if 2, set direction to right
				enemy.Right = true;
			} else if (enemy.direction == 3) { //if 3, set direction to up
				enemy.Up = true;
			}
		}

		elapsedTime = (currentNanoTime - enemy.startMove) / 1000000000.0;
		endTime = elapsedTime + enemy.duration;
		//overalltime is responsible for checking how long an enemy has been moving in a specific position
		enemy.overallTime = TimeUnit.NANOSECONDS.toSeconds(currentNanoTime - enemy.startSpawn);

		//this is for moving the enemies with their specific direction for a period of time
		//enemy.loopSec is the duration in which the enemy will move in a specific direction
		//checks if overallTime is still less than loopSec(duration)
		if (enemy.overallTime <= enemy.loopSec) {
			enemy.motion = 0; //sets in motion to 0, meaning that it is in motion
			//moves the enemy object depending depending on the direction and speed
			if (enemy.Left) {
				enemy.setDX(-enemy.ENEMY_SPEED);
			} else if (enemy.Right) {
				enemy.setDX(enemy.ENEMY_SPEED);
			} else if (enemy.Up) {
				enemy.setDY(-enemy.ENEMY_SPEED);
			} else if (enemy.Down) {
				enemy.setDY(enemy.ENEMY_SPEED);
			} else {
				enemy.setDX(0);
				enemy.setDY(0);
			}

			//moveenemyblob method in Enemy class for updating times
			enemy.moveEnemyBlob();
		}else{ //if the duration is over, the direction and duration will reset
			enemy.startSpawn = currentNanoTime; //sets the startSpawn of the enemy to the current time
			enemy.motion = 1; //sets enemy that it is not in motion
			Random r = new Random();
			Random r1 = new Random();
			enemy.loopSec = r.nextInt(5)+1; //new random duration from 1-5 secs
			enemy.direction = r1.nextInt(4); //new random direction
			setEnemyMovementsFalse(enemy); //sets all enemy's movements to false
		}
	}

	// sets boolean values for enemy movements to false (as a reset)
	private void setEnemyMovementsFalse (Enemy enemy) {
		enemy.Down = false;
		enemy.Up = false;
		enemy.Left = false;
		enemy.Right = false;
	}

	// method for checking food collision with the player and the enemy blobs
	private void checkFoodCollision () {

		// accesses all food
		for (int i = 0; i < this.allFood.size(); i++) {
			// player collision to food
			Food food = this.allFood.get(i);

			// if player collides with the food
			if (food.checkCollision(this.player)) {
				// eatFood method is called wherein the blob increases its size
				this.player.eatFood();
				// the image of the player is reloaded with its new size
				this.player.loadImage(this.player.img);
				// since the food is already consumed, it will vanish from the map
				food.vanish();
				// the player blob's speed will decrease
				this.player.updateSpeed();
				// food is removed from the arrayList so that it will not be put to map anymore
				this.allFood.remove(i);
				// increases number of food eaten, for game stats
				this.foodEaten++;
				// replaces the food to other location on the map
				this.spawnFood();
			}

			// accesses all enemies from the arraylist
			for (int k = 0; k < this.allEnemies.size(); k++) {
				Enemy enemy = this.allEnemies.get(k);

				// if enemy collides with food
				if (food.checkCollision(enemy)) {
				// eatFood method is called wherein the blob increases its size
				enemy.eatFood();
				// the image of the enemy is reloaded with its new size
				enemy.loadImage(enemy.img);
				// consumed food will be removed from the map
				food.vanish();
				// the speed of the blob will decrease
				enemy.updateSpeed();
				// food will be removed from the arrayList
				this.allFood.remove(i);
				// replaces the food to other location on the map
				this.spawnFood();
				}
			}
		}
	}

	// method for checking enemy collisions
	private void checkEnemyCollision () {

		// accesses all enemies from its arrayList
		for (int i = 0; i < this.allEnemies.size(); i++) {
			Enemy enemy = this.allEnemies.get(i);

			// checks enemy collision with player
			if (enemy.checkCollision(this.player)) {

				// if the player blob is bigger than the enemy, it will eat the enemy
				if (this.player.width > enemy.width) {
					// calls eatEnemy method, increases the size of the player blob
					// by the size of the enemy
					this.player.eatEnemy(enemy);
					// reloads the image of the player with its new size
					this.player.loadImage(this.player.img);
					// enemy is removed from the map
					enemy.vanish();
					// player's size is decreased
					this.player.updateSpeed();
					// enemy is also removed from the arrayList
					this.allEnemies.remove(i);

					// for game stats checking
					this.blobsEaten++;
					this.enemyCount--;


					// if the enemy is bigger than the player, the game is over unless it has immunity
				} else if (this.player.width < enemy.width) {

					// if the player does not have an immunity, player blob will die and the game is over
					if (!(this.player.getImmunity())) {
						this.player.die();
					}
				}
			}

			// checks for enemy collision with an enemy
			for (int j = 0; j < this.allEnemies.size(); j++) {
				Enemy enemy2 = this.allEnemies.get(j);

				// if enemy 1 collides with enemy 2
				if (enemy.checkCollision(enemy2)) {
					// if enemy 1 is bigger than enemy 2
					if (enemy.width > enemy2.width) {
						// enemy 1 will eat enemy 2, update size
						enemy.eatEnemy(enemy2);
						// its image will be reloaded with its new size
						enemy.loadImage(enemy.img);
						// enemy 2 will vanish from the map
						enemy2.vanish();
						// enemy 1's speed is updated
						enemy.updateSpeed();
						// enemy 2 os removed from the arrayList
						this.allEnemies.remove(j);

						// for game stats
						this.enemyCount--;

						// if the enemy 2 is bigger than enemy 1
					} else if (enemy.width < enemy2.width) {
						// enemy 2 will eat enemy 1, update size
						enemy2.eatEnemy(enemy);
						// enemy 2 will be reloaded with its new size
						enemy2.loadImage(enemy2.img);
						// enemy 1 will vanish from the map
						enemy.vanish();
						// enemy 2 will decrease its speed
						enemy2.updateSpeed();
						// enemy 1 will be removed from the arrayList
						this.allEnemies.remove(i);

						// for checking game stats
						this.enemyCount--;
					}
				}
			}
		}
	}

	// method for checking item collisions
	private void checkItemCollision () {
		// checks each item if it collides with the player
		for (int i = 0; i < this.allItems.size(); i++) {
			Items item = this.allItems.get(i);

			// if item is visible, check collision method will be called
			// it is an abstract method that are also called in pearl and starfish classes
			if (item.isVisible()) {
				item.checkCollision(this.player);
			} else {
				// if it is not visible, items are removed from the arraylist
				this.allItems.remove(i);
			}
		}
	}

	private void spawnEnemy () {
		int xPos, yPos, design;
		Image img = Enemy.ENEMY1;
		Random r = new Random();

		design = r.nextInt(GameTimer.MAX_ENEMYTYPE);

		if (design == 0) {
			img = Enemy.ENEMY1;
		} else if (design == 1) {
			img = Enemy.ENEMY2;
		} else if (design == 2) {
			img = Enemy.ENEMY3;
		} else if (design == 3) {
			img = Enemy.ENEMY4;
		} else if (design == 4) {
			img = Enemy.ENEMY5;
		}

		xPos = r.nextInt(GameTimer.worldWidth+Enemy.INITIAL_SIZE);
		yPos = r.nextInt(GameTimer.worldHeight+Enemy.INITIAL_SIZE);

		this.allEnemies.add(new Enemy(xPos, yPos, img));
	}

	// method for giving food elements its x and y positions and adds them to the arrayList
	private void spawnFood () {
		int xPos, yPos, color;
		Image img = Food.BLUE_FOOD;
		Random r = new Random();

		color = r.nextInt(GameTimer.MAX_FOODTYPE);

		if (color == 0) {
			img = Food.BLUE_FOOD;
		} else if (color == 1) {
			img = Food.GREEN_FOOD;
		} else if (color == 2) {
			img = Food.ORANGE_FOOD;
		} else if (color == 3) {
			img = Food.PURPLE_FOOD;
		} else if (color == 4) {
			img = Food.YELLOW_FOOD;
		}

		xPos = r.nextInt(GameTimer.worldWidth + Food.FOOD_SIZE);
		yPos = r.nextInt(GameTimer.worldHeight + Food.FOOD_SIZE);
		this.allFood.add(new Food(xPos, yPos, img));
	}

	// prints game stats for the duration of the game, found at the top of the window
	private void drawStats (long currentNanoTime) {
		double elapsedTime = (currentNanoTime - this.timeLong) / 1000000000.0;
		int duration = (int) (elapsedTime);

		this.gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 20));
		this.gc.setFill(Color.WHITE);

		this.gc.fillText("FOOD EATEN:", 20, 40);
		this.gc.fillText(this.foodEaten+"", 168, 40);

		this.gc.fillText("BLOBS EATEN:", 220, 40);
		this.gc.fillText(this.blobsEaten+"", 380, 40);

		this.gc.fillText("SIZE: ", 430, 40);
		this.gc.fillText(this.player.width+" px", 500, 40);

		this.gc.fillText("TIME: ", 600, 40);
		this.gc.fillText(duration+"", 680, 40);
	}

	// calls spawnEnemy
	private void initializeEnemy () {
		for (int i = 0; i <= GameTimer.MAX_ENEMY; i++) {
			this.spawnEnemy();
		}
	}

	// calls spawnFood method 50 times to initialize food
	private void initializeFood () {
		for (int i = 0; i <= GameTimer.MAX_FOOD; i++) {
			this.spawnFood();
		}
	}

	// prints final game stats when game over
	private void drawGameOver (long currentNanoTime) {
		double elapsedTime = (currentNanoTime - this.timeLong) / 1000000000.0;
		int duration = (int) (elapsedTime);

		this.gc.drawImage(background, 0, 0);
		this.gc.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 50));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText("GAME OVER!", 230, 250);

		this.gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25));
		this.gc.fillText("Food Eaten: ",  250, 340);
		this.gc.fillText (this.foodEaten+"", 500, 340);

		this.gc.fillText("Blobs Eaten: ", 250, 370);
		this.gc.fillText(this.blobsEaten+"", 500, 370);

		this.gc.fillText("Final Size: ", 250, 400);
		this.gc.fillText(this.player.width+"", 500, 400);

		this.gc.fillText("Final Time: ", 250, 430);
		this.gc.fillText(duration+"", 500, 430);
	}

	// method for printing final game stats when game is finished
	private void drawWinner (long currentNanoTime) {
		double elapsedTime = (currentNanoTime - this.timeLong) / 1000000000.0;
		int duration = (int) (elapsedTime);

		Image winnerbg = new Image ("images/winnerbg.png", 800, 800, false, false);

		this.gc.drawImage(winnerbg, 0, 0);
		this.gc.setFont(Font.font("Times New Roman", FontWeight.EXTRA_BOLD, 50));
		this.gc.setFill(Color.WHITE);
		this.gc.fillText("CONGRATULATIONS!", 150, 250);

		this.gc.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.ITALIC, 25));
		this.gc.fillText("Food Eaten: ",  250, 340);
		this.gc.fillText (this.foodEaten+"", 500, 340);

		this.gc.fillText("Blobs Eaten: ", 250, 370);
		this.gc.fillText(this.blobsEaten+"", 500, 370);

		this.gc.fillText("Final Size: ", 250, 400);
		this.gc.fillText(this.player.width+"", 500, 400);

		this.gc.fillText("Final Time: ", 250, 430);
		this.gc.fillText(duration+"", 500, 430);
	}
}
