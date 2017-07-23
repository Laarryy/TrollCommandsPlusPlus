package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.NoPickupRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NoPickupEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IRegistry<UUID> noPickupRegistry = ServiceLocator.getService(NoPickupRegistry.class);
	
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
		
		if (noPickupRegistry.hasRegister(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
