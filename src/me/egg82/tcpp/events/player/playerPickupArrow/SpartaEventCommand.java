package me.egg82.tcpp.events.player.playerPickupArrow;

import org.bukkit.event.player.PlayerPickupArrowEvent;

import me.egg82.tcpp.services.SpartaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SpartaEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private IRegistry spartaRegistry = ServiceLocator.getService(SpartaRegistry.class);
	
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
		
		if (spartaRegistry.hasRegister(event.getArrow().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}
}
