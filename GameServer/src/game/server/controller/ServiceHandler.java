package game.server.controller;

import java.util.LinkedList;
import java.util.Random;

import game.server.bean.Command;
import game.server.bean.Room;
import game.server.bean.Player;
import game.server.common.Config;
import game.server.common.SocketUtil;
import game.server.dbconnect.Entity;

public class ServiceHandler extends Thread {
	public volatile LinkedList<Command> queueCommands = new LinkedList<Command>();
	private boolean isRunning = false;
	private Room room;
	private long delay;
	private long current;

	public ServiceHandler() {
		
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void renderEnemies() {
		current = System.nanoTime();
		delay = 3000;
		Runnable run = new Runnable() {
			@Override
			public void run() {
				while (isRunning) {
					long breaks = (System.nanoTime() - current) / 1000000;
					if (breaks > delay) {
						// Render enemies by random x, random y
						Random rand = new Random();
						int randX = rand.nextInt(450);
						int randY = rand.nextInt(450);
						queueCommands.addLast(new Command(null, "ren " + randX + "|" + randY));
						
						current = System.nanoTime();
					}
				}
			}
		};

		Thread renEnemies = new Thread(run);
		renEnemies.start();
	}

	public synchronized void PushToQueue(Command cmd) {
		queueCommands.addLast(cmd);
	}

	public void sendMessageToCliet(String message) {
		for (Player pl : room.GetListPlayer()) {
			SocketUtil.sendViaTcp(pl.getSocket(), message);
		}
	}

	public void sendWaitMessageToCLient() {
		for (Player pl : room.GetListPlayer()) {
			SocketUtil.sendViaTcp(pl.getSocket(), "sta failed\n");
		}
	}

	public void sendStartMessageToCLient() {
		for (Player pl : room.GetListPlayer()) {
			if (!pl.isHost()) {
				SocketUtil.sendViaTcp(pl.getSocket(), "sta ok\n");
			}
		}
	}

	public void sendReadyMessageToClient() {
		String lstUser = "";
		for (Player pl : room.GetListPlayer()) {
			lstUser = lstUser + "|" + pl.getUsername();
		}

		for (Player pl : room.GetListPlayer()) {
			if (pl.isHost()) {
				String message = "sta ready" + lstUser + "\n";
				SocketUtil.sendViaTcp(pl.getSocket(), message);
			} else {
				String message = "sta waithost" + lstUser + "\n";
				SocketUtil.sendViaTcp(pl.getSocket(), message);
			}
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			if (!queueCommands.isEmpty()) {
				try {
					Command command = queueCommands.removeFirst();
					String message = command.getControlString();
					if(message.startsWith(Config.MOVE_CODE)) {
						sendMessageToCliet(message);
					}
					else if(message.startsWith(Config.STATUS_IN_GAME)) {
						//message = message.substring(4);
						//String[] cmds = message.split("\\|");
						sendMessageToCliet("stt gameover");
					}
					else if (message.startsWith(Config.STARTGAME_CODE)) {
						message = message.substring(4);
						if (message.equals("hoststart")) {
							sendStartMessageToCLient();
							renderEnemies();
						}
					} else if (message.startsWith(Config.RENDERENEMY_CODE)) {
						sendMessageToCliet(message);
					} 
				} catch (Exception ex) {
					//System.out.println("Error: " + ex);
				}
			}
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}
}
