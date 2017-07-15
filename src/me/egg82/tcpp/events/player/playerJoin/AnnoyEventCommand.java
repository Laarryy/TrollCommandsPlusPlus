package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.AnnoyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AnnoyEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	IRegistry annoyRegistry = ServiceLocator.getService(AnnoyRegistry.class);
	
	//constructor
	public AnnoyEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (annoyRegistry.hasRegister(uuid)) {
			annoyRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
