package me.egg82.tcpp.events.block.blockPlace;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.DisplayBlockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry displayBlockRegistry = ServiceLocator.getService(DisplayBlockRegistry.class);
	
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
		
		String[] names = displayBlockRegistry.getRegistryNames();
		for (String name : names) {
			Set<Location> blockedLocs = displayBlockRegistry.getRegister(name, Set.class);
			
			if (blockedLocs.contains(loc)) {
				event.setCancelled(true);
			}
		}
	}
}
