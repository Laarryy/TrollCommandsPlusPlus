package me.egg82.tcpp.events.player.playerPickupItem;

import java.util.UUID;

import org.bukkit.event.player.PlayerPickupItemEvent;

import me.egg82.tcpp.services.registries.NoPickupRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class NoPickupEventCommand extends EventCommand<PlayerPickupItemEvent> {
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
