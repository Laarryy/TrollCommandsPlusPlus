package me.egg82.tcpp.events.custom;

import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class LagAsyncPlayerChatEvent extends AsyncPlayerChatEvent {
	//vars
	
	//constructor
	public LagAsyncPlayerChatEvent(boolean async, Player who, String message, Set<Player> players) {
		super(async, who, message, players);
	}
	
	//public
	
	//private
	
}
