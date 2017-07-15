package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.HauntRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class HauntEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	IRegistry hauntRegistry = ServiceLocator.getService(HauntRegistry.class);
	
	//constructor
	public HauntEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (hauntRegistry.hasRegister(uuid)) {
			hauntRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
