package game;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite {
	// attributes of all sprites for moving and printing them to the map
	protected Image img;
	protected double xPos, yPos, dx, dy;
	protected boolean visible;
	protected double width;
	protected double height;


	public Sprite (double xPos, double yPos, Image image) {

		// initialize attributes
		this.xPos = xPos;
		this.yPos = yPos;
		this.loadImage(image);
		this.visible = true;



	}

	// getting bounds of sprite
	private Rectangle2D getBounds() {
		return new Rectangle2D(this.xPos, this.yPos, this.width, this.height);
	}

	// setting size to the size of the image itself
	private void setSize() {
		this.width = this.img.getWidth();
        this.height = this.img.getHeight();
	}

	// method as a checker whether a sprite collides with another sprite
	protected boolean collidesWith (Sprite sprite2) {
		Rectangle2D rect1 = this.getBounds();
		Rectangle2D rect2 = sprite2.getBounds();

		return rect1.intersects(rect2);
	}

	// loads image of sprite to map
	protected void loadImage(Image image){
		try{
			this.img = image;
	        //this.setSize();
		} catch(Exception e)	{
			e.printStackTrace();
		}
	}

	// draws image to map
	public void render(GraphicsContext gc) {

		if (this.isVisible())
			gc.drawImage(this.img, this.xPos, this.yPos, this.width, this.height);
	}

	public Image getImage(){
		return this.img;
	}

	public double getXPos(){
		return this.xPos;
	}

	public double getYPos(){
		return this.yPos;
	}

	public void setDX(double PLAYER_SPEED){
		this.dx = PLAYER_SPEED;
	}

	public void setDY(double PLAYER_SPEED){
		this.dy = PLAYER_SPEED;
	}

	public boolean isVisible(){
		return visible;
	}

	public void vanish(){
		this.visible = false;
	}

}
