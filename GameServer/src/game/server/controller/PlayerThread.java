package game.server.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import game.server.bean.Command;
import game.server.bean.Player;
import game.server.common.Config;
import game.server.dbconnect.Entity;

public class PlayerThread extends Thread {
	public Socket connectionSocket;
	public ServiceHandler serviceHandler=null;
	public Player player=null;
	BufferedReader infromClient;
	DataOutputStream outToCLient;
	private boolean isLogin = false;
	
	public PlayerThread(Socket socket, ServiceHandler serviceHandler, Player player) {
		this.connectionSocket = socket;
		this.serviceHandler = serviceHandler;
		this.player = player;
	}
	
	@Override
	public void run() {
		try {
			infromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToCLient = new DataOutputStream(connectionSocket.getOutputStream());
			
			while(!isLogin) {
				String message = infromClient.readLine();
				
				if(message.startsWith(Config.LOGIN_CODE)) {
					message = message.substring(4);
					if(Entity.checkUser(message)) {
						String[] obj = message.split("\\|");
						player.setUsername(obj[0]);
						isLogin = true;
						outToCLient.writeBytes("log ok|"+obj[0]+"\n");
					} else {
						outToCLient.writeBytes("log failed\n");
					}
				}
				else if(message.startsWith(Config.REGISTER_CODE)) {
					message = message.substring(4);
					int flag = 1;
					String[] obj = message.split("\\|");
					for(String user : Entity.database) {
						int index = user.indexOf('|');
						user = user.substring(0, index);
						if(user.equals(obj[0])) {
							outToCLient.writeBytes("reg failed\n");
							flag = 0;
							break;
						}
					}
					
					if(flag == 1) {
						Entity.updateDatabase(message);
						outToCLient.writeBytes("reg ok\n");	
					}
				}
			}
			
			while(true) {
				String message = infromClient.readLine();
				if(serviceHandler.isRunning()) {
					Command cmd = new Command(player, message);
					serviceHandler.PushToQueue(cmd);
				}
				else {
					outToCLient.writeBytes(Config.STARTGAME_CODE+" ready\n");
				}
			}
		} catch (Exception e) {
			
		}
	}
}
