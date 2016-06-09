package me.egg82.tcpp.events.individual.playerPickupItemEvent;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;

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
		PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
		
		if (e.getItem().hasMetadata(MetadataType.VEGETABLE)) {
			e.setCancelled(true);
		}
	}
}
