package game.client.bean;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import game.client.com.Communication;
import game.client.com.Config;

public class gameManager implements KeyListener {

	public static Player player;
	//public static Player player2;
	public static ArrayList<Bullet> bullet;
	private ArrayList<Enemy> enemies;
	private volatile LinkedList<Command> queueCommands;

	private long current;
	private long delay;
	private int health;
	private int score;
	private boolean start;
	private String uname;
	private String gameStatus;
	private boolean readyToPlay = false;
	public static List<Player> friends;
	
	public int GetScore() {
		return score;
	}

	public gameManager(String uname) {
		this.uname = uname;
		queueCommands = new LinkedList<Command>();
		friends = new LinkedList<Player>();
	}
	
	public void setStart(boolean bo) {
		this.start = bo;
	}
	
	public void addFriend(String playerName) {
		Player friend = new Player((gameSetUp.gameWidth / 2) + 50, (gameSetUp.gameHeight - 60) + 50, playerName);
		friends.add(friend);
	}
	
	public void setReadyToPlay(boolean boo) {
		this.readyToPlay = boo;
	}
	
	public void PushToQueue(String message) {
		queueCommands.addLast(new Command(null, message));
	}
	
	public void RenderEnemies() {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				while(true) {
					if(!queueCommands.isEmpty()) {
						Command cmd = queueCommands.removeFirst();
						String message = cmd.getCommand();
						if(message.startsWith(Config.RENDERENEMY_CODE)) {
							if (health > 0) {
								message = message.substring(4);
								String[] coordinates = message.split("\\|");
								int randX = Integer.parseInt(coordinates[0]);
								int randY = Integer.parseInt(coordinates[1]);
								enemies.add(new Enemy(randX, -randY));
							}
						}
					}
				}
			}
		};
		Thread renderEnemies = new Thread(run);
		renderEnemies.start();
	}

	public void init() {
		Display.frame.addKeyListener(this);
		player = new Player((gameSetUp.gameWidth / 2) + 50, (gameSetUp.gameHeight - 60) + 50, uname);
		player.init();
		for(Player fr : friends) {
			fr.FriendInit();
		}

		bullet = new ArrayList<Bullet>();
		enemies = new ArrayList<Enemy>();
		current = System.nanoTime();
		delay = 2000;

		health = player.getHealth();
		score = 0;
	}

	public void tick() {
		if (start) {
			player.tick();

			for (int i = 0; i < bullet.size(); i++) {
				bullet.get(i).tick();
			}

			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).tick();
			}
		}
	}

	public void render(Graphics g) {
		if (start) {
			player.render(g);
			for(Player fr : friends) {
				fr.render(g);
			}

			for (int i = 0; i < bullet.size(); i++) {
				bullet.get(i).render(g);
			}

			for (int i = 0; i < bullet.size(); i++) {
				if (bullet.get(i).getY() <= 50) {
					bullet.remove(i);
					i--;

				}
			}

			// enemies
			for (int i = 0; i < enemies.size(); i++) {
				if (!(enemies.get(i).getX() <= 50 || enemies.get(i).getX() >= 450 - 50
						|| enemies.get(i).getY() >= 450 - 50)) {

					if (enemies.get(i).getY() >= 50) {
						enemies.get(i).render(g);
					}
				}
			}

			// enemies & player collision
			for (int i = 0; i < enemies.size(); i++) {
				int ex = enemies.get(i).getX();
				int ey = enemies.get(i).getY();

				int px = player.getX();
				int py = player.getY();

				if (px < ex + 50 && px + 60 > ex && py < ey + 50 && py + 60 > ey) {
					enemies.remove(i);
					i--;
					health--;
					System.out.println(health);
					if (health <= 0) {
						enemies.removeAll(enemies);
						player.setHealth(0);
						start = false;
						String message = "stt "+player.getUserName()+"|die";
						Communication.Send(message);
					}
				}

				// bullets && enemy collision
				for (int j = 0; j < bullet.size(); j++) {
					int bx = bullet.get(j).getX();
					int by = bullet.get(j).getY();

					if (ex < bx + 6 && ex + 50 > bx && ey < by + 6 && ey + 50 > by) {
						try {
							enemies.remove(i);
							i--;
						} catch (Exception e) {
							
						}
						if(bullet.get(j).getUserName() == player.getUserName()) {
							score = score + 1;
							String message = "stt "+player.getUserName()+"|score";
							Communication.Send(message);
						}
						bullet.remove(j);
						j--;
					}
				}
				g.setColor(new Color(189, 227, 188));
				g.setFont(new Font("calibri", Font.BOLD, 30));
				g.drawString("Name : " + player.getUserName(), 70, 500);
				g.drawString("Score : " + score, 70, 530);
				g.drawString("Health : "+ health, 70, 560);
			}
		}

		else {
			g.setColor(new Color(189, 227, 188));
			g.setFont(new Font("arial", Font.PLAIN, 30));
			g.drawString(gameStatus, 100, (gameSetUp.gameHeight / 2) + 50);
		}
	}
	
	public void StartByHost() {
		RenderEnemies();
		start = true;
		init();
	}

	public void keyPressed(KeyEvent e) {
		int source = e.getKeyCode();
		if (source == KeyEvent.VK_ENTER && readyToPlay) {
			if(player.isHost()) {
				String message = "sta hoststart";
				Communication.Send(message);
			}
			RenderEnemies();
			start = true;
			init();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

}
