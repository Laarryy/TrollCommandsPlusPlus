package me.egg82.tcpp.events.player.playerPickupArrow;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.NoPickupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NoPickupEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IRegistry noPickupRegistry = ServiceLocator.getService(NoPickupRegistry.class);
	
	//constructor
	public NoPickupEventCommand(PlayerPickupArrowEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (noPickupRegistry.hasRegister(player.getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}
}
