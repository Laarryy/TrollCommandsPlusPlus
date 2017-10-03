package me.egg82.tcpp.events.player.playerPickupArrow;

import java.util.UUID;

import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.registries.SpartaArrowRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SpartaEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IRegistry<UUID> spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);
	
	//constructor
	public SpartaEventCommand(PlayerPickupArrowEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (spartaArrowRegistry.hasRegister(event.getArrow().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
