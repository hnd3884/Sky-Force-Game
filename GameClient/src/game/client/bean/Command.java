package game.client.bean;

public class Command {
	private Player player;
	private String command;
	
	public Command(Player player, String command) {
		this.player = player;
		this.command = command;
	}
	
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
}
