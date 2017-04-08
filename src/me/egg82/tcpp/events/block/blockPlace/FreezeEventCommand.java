package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand {
	//vars
	IRegistry freezeRegistry = (IRegistry) ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (freezeRegistry.hasRegister(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}
}
