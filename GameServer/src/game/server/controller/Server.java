package game.server.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import game.server.bean.Room;
import game.server.bean.Player;
import game.server.common.Config;
import game.server.dbconnect.Entity;

public class Server {

	public static void main(String[] args) {
		ServerSocket listener;
		try {
			System.out.println("Server open!");
			listener = new ServerSocket(6789);
			Room room = new Room();
			ServiceHandler serviceHandler = new ServiceHandler();
			Entity.getDatabase();
			
			while(true) {
				Socket connectionSocket = listener.accept();

				Player player = new Player(connectionSocket);
				if(room.GetListPlayer().size() == 0) {
					player.setHost(true);
				}
				PlayerThread playerThread = new PlayerThread(connectionSocket, serviceHandler, player);
				playerThread.start();
				room.addPlayer(player);
				if(room.GetListPlayer().size() == Config.MAX_PLAYER) {
					serviceHandler.setRoom(room);
					serviceHandler.setRunning(true);
					serviceHandler.start();
					while(player.getUsername() == null) {
						System.out.println("");
					}
					serviceHandler.sendReadyMessageToClient();
				}
				else {
					serviceHandler.setRoom(room);
					while(player.getUsername() == null) {
						System.out.println("");
					}
					serviceHandler.sendWaitMessageToCLient();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
