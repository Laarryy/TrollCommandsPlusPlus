package me.egg82.tcpp.events.block.blockPlace;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.DisplayRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry<UUID> displayRegistry = ServiceLocator.getService(DisplayRegistry.class);
	
	//constructor
	public DisplayEventCommand(BlockPlaceEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Location loc = event.getBlock().getLocation();
		
		UUID[] keys = displayRegistry.getRegistryKeys();
		for (UUID key : keys) {
			Set<Location> blockedLocs = displayRegistry.getRegister(key, Set.class);
			
			if (blockedLocs.contains(loc)) {
				event.setCancelled(true);
			}
		}
	}
}
