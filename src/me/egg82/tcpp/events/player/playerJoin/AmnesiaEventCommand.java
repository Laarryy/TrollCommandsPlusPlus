package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.AmnesiaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AmnesiaEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry amnesiaRegistry = ServiceLocator.getService(AmnesiaRegistry.class);
	
	//constructor
	public AmnesiaEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (amnesiaRegistry.hasRegister(uuid)) {
			amnesiaRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
