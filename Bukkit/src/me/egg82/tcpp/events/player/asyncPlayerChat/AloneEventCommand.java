package me.egg82.tcpp.events.player.asyncPlayerChat;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.egg82.tcpp.lists.AloneSet;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class AloneEventCommand extends EventHandler<AsyncPlayerChatEvent> {
	//vars
	private IConcurrentSet<UUID> aloneSet = ServiceLocator.getService(AloneSet.class);
	
	//constructor
	public AloneEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		Set<Player> players = event.getRecipients();
		HashSet<Player> removedPlayers = new HashSet<Player>();
		
		if (aloneSet.contains(player.getUniqueId())) {
			for (Player p : players) {
				if (p.getUniqueId().equals(player.getUniqueId())) {
					continue;
				}
				removedPlayers.add(p);
			}
			return;
		}
		
		for (Player p : players) {
			if (aloneSet.contains(p.getUniqueId())) {
				removedPlayers.add(p);
			}
		}
		
		players.removeAll(removedPlayers);
	}
}
