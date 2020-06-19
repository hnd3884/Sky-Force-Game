package game.server.bean;

public class Command {
	private Player player;
	private String controlString;
	public Command(Player player, String controlString) {
		this.setPlayer(player);
		this.setControlString(controlString);
	}
	public String getControlString() {
		return controlString;
	}
	public void setControlString(String controlString) {
		this.controlString = controlString;
	}
	public Player getPlayer() {
		return player;
	}
	public void setPlayer(Player player) {
		this.player = player;
	}
}
