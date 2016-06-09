package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.entity.ItemDespawnEvent;

import me.egg82.tcpp.events.individual.itemDespawnEvent.VegetableEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class ItemDespawnEventCommand extends EventCommand {
	//vars
	private VegetableEventCommand vegetable = null;
	
	//constructor
	public ItemDespawnEventCommand(Event event) {
		super(event);
		
		vegetable = new VegetableEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		ItemDespawnEvent e = (ItemDespawnEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		vegetable.start();
	}
}
