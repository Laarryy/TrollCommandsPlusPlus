package me.egg82.tcpp.events.entity.entityExplode;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.egg82.tcpp.services.DisplayRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<EntityExplodeEvent> {
	//vars
	private IRegistry<UUID> displayRegistry = ServiceLocator.getService(DisplayRegistry.class);
	
	//constructor
	public DisplayEventCommand(EntityExplodeEvent event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		List<Block> blocks = event.blockList();
		
		for (UUID key : displayRegistry.getKeys()) {
			for (Location loc : (Set<Location>) displayRegistry.getRegister(key)) {
				blocks.remove(loc.getBlock());
			}
		}
	}
}
