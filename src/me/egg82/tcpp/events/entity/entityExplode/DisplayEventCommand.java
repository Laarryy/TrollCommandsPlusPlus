package me.egg82.tcpp.events.entity.entityExplode;

import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityExplodeEvent;

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
		EntityExplodeEvent e = (EntityExplodeEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		List<Block> blocks = e.blockList();
		
		String[] names = displayBlockRegistry.getRegistryNames();
		for (String name : names) {
			for (Location loc : (Set<Location>) displayBlockRegistry.getRegister(name)) {
				blocks.remove(loc.getBlock());
			}
		}
	}
}
