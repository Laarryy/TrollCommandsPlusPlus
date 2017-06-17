package me.egg82.tcpp.events.player.playerItemHeld;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerItemHeldEvent;

import me.egg82.tcpp.services.LockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LockEventCommand extends EventCommand {
	//vars
	IRegistry lockRegistry = (IRegistry) ServiceLocator.getService(LockRegistry.class);
	
	//constructor
	public LockEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerItemHeldEvent e = (PlayerItemHeldEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (lockRegistry.hasRegister(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}
}
