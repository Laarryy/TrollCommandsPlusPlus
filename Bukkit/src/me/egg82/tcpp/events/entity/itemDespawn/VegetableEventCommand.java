package me.egg82.tcpp.events.entity.itemDespawn;

import java.util.UUID;

import org.bukkit.event.entity.ItemDespawnEvent;

import me.egg82.tcpp.services.registries.VegetableItemRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class VegetableEventCommand extends EventCommand<ItemDespawnEvent> {
	//vars
	private IVariableRegistry<UUID> vegetableItemRegistry = ServiceLocator.getService(VegetableItemRegistry.class);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (vegetableItemRegistry.hasValue(event.getEntity())) {
			event.setCancelled(true);
		}
	}
}
