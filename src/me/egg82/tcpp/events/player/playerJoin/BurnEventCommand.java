package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.BurnRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BurnEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	IRegistry burnRegistry = ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (burnRegistry.hasRegister(uuid)) {
			burnRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
