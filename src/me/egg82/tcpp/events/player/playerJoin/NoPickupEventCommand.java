package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.NoPickupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NoPickupEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry noPickupRegistry = ServiceLocator.getService(NoPickupRegistry.class);
	
	//constructor
	public NoPickupEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (noPickupRegistry.hasRegister(uuid)) {
			noPickupRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
