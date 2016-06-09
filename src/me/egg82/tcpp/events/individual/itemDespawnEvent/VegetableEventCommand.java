package me.egg82.tcpp.events.individual.itemDespawnEvent;

import org.bukkit.event.Event;
import org.bukkit.event.entity.ItemDespawnEvent;

import me.egg82.tcpp.enums.MetadataType;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand {
	//vars
	
	//constructor
	public VegetableEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		ItemDespawnEvent e = (ItemDespawnEvent) event;
		
		if (e.getEntity().hasMetadata(MetadataType.VEGETABLE)) {
			e.setCancelled(true);
		}
	}
}
