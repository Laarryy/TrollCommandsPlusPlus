package me.egg82.tcpp.events.entity.entityExplode;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.entity.EntityExplodeEvent;

import me.egg82.tcpp.services.registries.DisplayRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class DisplayEventCommand extends EventCommand<EntityExplodeEvent> {
	//vars
	private IVariableRegistry<UUID> displayRegistry = ServiceLocator.getService(DisplayRegistry.class);
	
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
		
		List<Block> blocks = event.blockList();
		
		for (UUID key : displayRegistry.getKeys()) {
			for (Location loc : (Set<Location>) displayRegistry.getRegister(key)) {
				blocks.remove(loc.getBlock());
			}
		}
	}
}
