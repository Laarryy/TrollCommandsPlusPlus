package me.egg82.tcpp.events.block.blockBreak;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.services.registries.DisplayRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private IRegistry<UUID> displayRegistry = ServiceLocator.getService(DisplayRegistry.class);
	
	//constructor
	public DisplayEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Location loc = event.getBlock().getLocation();
		
		for (UUID key : displayRegistry.getKeys()) {
			Set<Location> blockedLocs = displayRegistry.getRegister(key, Set.class);
			
			if (blockedLocs.contains(loc)) {
				event.setCancelled(true);
				break;
			}
		}
	}
}
