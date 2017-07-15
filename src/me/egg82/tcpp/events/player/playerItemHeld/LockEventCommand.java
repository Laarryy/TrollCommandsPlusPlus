package me.egg82.tcpp.events.player.playerItemHeld;

import org.bukkit.event.player.PlayerItemHeldEvent;

import me.egg82.tcpp.services.LockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LockEventCommand extends EventCommand<PlayerItemHeldEvent> {
	//vars
	IRegistry lockRegistry = ServiceLocator.getService(LockRegistry.class);
	
	//constructor
	public LockEventCommand(PlayerItemHeldEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (lockRegistry.hasRegister(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}
}
