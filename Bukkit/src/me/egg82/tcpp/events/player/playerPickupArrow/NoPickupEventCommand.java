package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.registries.NoPickupRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class NoPickupEventCommand extends EventHandler<PlayerPickupArrowEvent> {
	//vars
	private IVariableRegistry<UUID> noPickupRegistry = ServiceLocator.getService(NoPickupRegistry.class);
	
	//constructor
	public NoPickupEventCommand() {
		super();
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
