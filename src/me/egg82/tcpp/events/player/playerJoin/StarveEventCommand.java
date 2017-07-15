package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.StarveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class StarveEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry starveRegistry = ServiceLocator.getService(StarveRegistry.class);
	
	//constructor
	public StarveEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (starveRegistry.hasRegister(uuid)) {
			starveRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
