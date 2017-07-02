package me.egg82.tcpp.events.block.blockBreak;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.services.DisplayBlockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand {
	//vars
	private IRegistry displayBlockRegistry = (IRegistry) ServiceLocator.getService(DisplayBlockRegistry.class);
	
	//constructor
	public DisplayEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Location loc = e.getBlock().getLocation();
		
		String[] names = displayBlockRegistry.getRegistryNames();
		for (String name : names) {
			Set<Location> blockedLocs = (Set<Location>) displayBlockRegistry.getRegister(name);
			
			if (blockedLocs.contains(loc)) {
				e.setCancelled(true);
				break;
			}
		}
	}
}
