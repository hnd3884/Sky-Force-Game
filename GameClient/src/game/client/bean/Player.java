package game.client.bean;

//import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;

import game.client.com.Communication;

public class Player implements KeyListener {
	private int x;
	private int y;
	private boolean left, right, fire;

	private long current;
	private long delay;
	private int health;
	private String userName;
	private boolean isHost = false;
	// private Socket socket;

	public void SetUserName(String username) {
		this.userName = username;
	}
	public Player(int x, int y, String uname) {
		this.x = x;
		this.y = y;
		this.userName = uname;
	}

	public void FriendInit() {
		current = System.nanoTime();
		delay = 100;
		health = 3;
	}

	public void init() {
		Display.frame.addKeyListener(this);
		current = System.nanoTime();
		delay = 100;
		health = 3;
	}

	public void tick() {
		if (!(health <= 0)) {
			if (left) {
				if (x >= 50) {
					x -= 5;
					Communication.Send("mov " + userName + "|left");
				}
			}
			if (right) {
				if (x <= 450 - 60) {
					x += 5;
					Communication.Send("mov " + userName + "|right");
				}
			}
			if (fire) {
				long breaks = (System.nanoTime() - current) / 1000000;
				if (breaks > delay) {
					gameManager.bullet.add(new Bullet(x + 30, y + 10, userName));
					Communication.Send("mov " + userName + "|fire");
				}
				current = System.nanoTime();
			}
		}
	}

	public void TurnLeftByHost() {
		if (x >= 50) {
			x -= 5;
		}
	}

	public void TurnRightByHost() {
		if (x <= 450 - 60) {
			x += 5;
		}
	}

	public void FireByHost() {
		long breaks = (System.nanoTime() - current) / 1000000;
		if (breaks > delay) {
			gameManager.bullet.add(new Bullet(x + 30, y + 10, userName));
		}
		current = System.nanoTime();
	}

	public void render(Graphics g) {
		if (!(health <= 0)) {
			g.drawImage(loadImage.player, x, y, 60, 60, null);
		}
	}

	public void keyPressed(KeyEvent e) {
		int source = e.getKeyCode();
		if (source == KeyEvent.VK_LEFT) {
			left = true;
		} else if (source == KeyEvent.VK_RIGHT) {
			right = true;
		} else if (source == KeyEvent.VK_SPACE) {
			fire = true;
		}
	}

	public void keyReleased(KeyEvent e) {
		int source = e.getKeyCode();
		if (source == KeyEvent.VK_LEFT) {
			left = false;
		}
		else if (source == KeyEvent.VK_RIGHT) {
			right = false;

		}
		else if (source == KeyEvent.VK_SPACE) {
			fire = false;
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public String getUserName() {
		return userName;
	}

	public boolean isHost() {
		return isHost;
	}

	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}
}
