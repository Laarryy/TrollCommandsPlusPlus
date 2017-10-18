package me.egg82.tcpp.events.player.playerPickupItem;

import java.util.UUID;

import org.bukkit.event.player.PlayerPickupItemEvent;

import me.egg82.tcpp.services.registries.TripRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class TripEventCommand extends EventCommand<PlayerPickupItemEvent> {
	//vars
	private IRegistry<UUID> tripRegistry = ServiceLocator.getService(TripRegistry.class);
	
	//constructor
	public TripEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (tripRegistry.hasRegister(event.getPlayer().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
