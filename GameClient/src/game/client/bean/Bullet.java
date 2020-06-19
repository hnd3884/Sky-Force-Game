package game.client.bean;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet {

	private int x;
	private int y;
	private int speed;
	private String userName;

	public Bullet(int x, int y, String username) {
		this.x = x;
		this.y = y;
		this.userName = username;
		speed = 10;

	}

	public void tick() {
		y -= speed;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public void render(Graphics g) {
		g.setColor(new Color(191, 157, 113));
		g.fillRect(x, y, 6, 10);
		g.setColor(Color.BLACK);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
