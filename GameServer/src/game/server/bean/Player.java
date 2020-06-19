package game.server.bean;

import java.net.Socket;

public class Player {
	private Socket socket;
	private String username;
	private String password;
	private boolean isHost = false;
	private int point = 0;
	public Player(Socket socket) {
		this.socket = socket;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getPoint() {
		return point;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Socket getSocket() {
		return socket;
	}
	public boolean isHost() {
		return isHost;
	}
	public void setHost(boolean isHost) {
		this.isHost = isHost;
	}
}
