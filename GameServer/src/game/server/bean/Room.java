package game.server.bean;

import java.util.ArrayList;
import java.util.List;

public class Room {
	private List<Player> lst = new ArrayList<Player>();
	public void addPlayer(Player player) {
		lst.add(player);
	}
	
	public List<Player> GetListPlayer() {
		return lst;
	}
}
