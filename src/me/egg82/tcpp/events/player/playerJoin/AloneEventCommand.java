package me.egg82.tcpp.events.player.playerJoin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.lists.AloneSet;
import ninja.egg82.bukkit.reflection.player.IPlayerHelper;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.concurrent.IConcurrentSet;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class AloneEventCommand extends EventHandler<PlayerJoinEvent> {
	//vars
	private IConcurrentSet<UUID> aloneSet = ServiceLocator.getService(AloneSet.class);
	
	private IPlayerHelper playerHelper = ServiceLocator.getService(IPlayerHelper.class);
	
	//constructor
	public AloneEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (aloneSet.contains(uuid)) {
			for (Player p : Bukkit.getServer().getOnlinePlayers()) {
				playerHelper.hidePlayer(player, p);
			}
		}
		
		for (UUID key : aloneSet) {
			Player p = CommandUtil.getPlayerByUuid(key);
			if (p != null) {
				playerHelper.hidePlayer(p, player);
			}
		}
	}
}
