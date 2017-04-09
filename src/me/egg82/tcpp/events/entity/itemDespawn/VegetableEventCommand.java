package me.egg82.tcpp.events.entity.itemDespawn;

import org.bukkit.event.Event;
import org.bukkit.event.entity.ItemDespawnEvent;

import me.egg82.tcpp.services.VegetableItemRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableItemRegistry = (IRegistry) ServiceLocator.getService(VegetableItemRegistry.class);
	
	//constructor
	public VegetableEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		ItemDespawnEvent e = (ItemDespawnEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (vegetableItemRegistry.hasValue(e.getEntity())) {
			e.setCancelled(true);
		}
	}
}
