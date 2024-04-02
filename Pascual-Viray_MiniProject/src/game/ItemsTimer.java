package game;

class ItemsTimer extends Thread {
	// player that will receive powerups
	private Player player;

	// duration of powerup
	private int time;

	// duration of upgrade
	private final static int UPGRADED_TIME = 5;

	ItemsTimer (Player player) {
		// initializes attributes
		this.player = player;
		this.time = ItemsTimer.UPGRADED_TIME;
	}

	// for resetting time
	void refresh () {
		this.time = ItemsTimer.UPGRADED_TIME;
	}

	// count down and brings back normal speed / lose immunity depending on the item after
	// designated time
	private void countDown () {
		while (this.time != 0) {
			try {
				Thread.sleep(1000);
				this.time--;
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
		}

		if (this.player.getItemType() == 0) {
			player.decreaseSpeed();
		} else if (this.player.getItemType() == 1) {
			this.player.setImmunityFalse();
		}
	}

	@Override
	public void run () {
		this.countDown();
	}


}
