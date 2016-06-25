package me.egg82.tcpp.events.individual.itemDespawn;

import org.bukkit.event.entity.ItemDespawnEvent;

import me.egg82.tcpp.enums.MetadataType;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand {
	//vars
	
	//constructor
	public VegetableEventCommand() {
		super();
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
