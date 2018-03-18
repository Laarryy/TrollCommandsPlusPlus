package me.egg82.tcpp.exceptions;

import org.bukkit.entity.Player;

public class PlayerImmuneException extends RuntimeException {
	//vars
	public static final PlayerImmuneException EMPTY = new PlayerImmuneException(null);
	private static final long serialVersionUID = -1574184630178007154L;
	
	private Player player = null;
	
	//constructor
	public PlayerImmuneException(Player player) {
		super();
		
		this.player = player;
	}
	
	//public
	public Player getPlayer() {
		return player;
	}
	
	//private
	
}
