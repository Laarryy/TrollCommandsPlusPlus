package me.egg82.tcpp.events;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.events.individual.blockPlaceEvent.LagEventCommand;
import ninja.egg82.plugin.commands.EventCommand;

public class BlockPlaceEventCommand extends EventCommand {
	//vars
	private LagEventCommand lag = null;
	
	//constructor
	public BlockPlaceEventCommand(Event event) {
		super(event);
		
		lag = new LagEventCommand(event);
	}
	
	//public
	
	//private
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		lag.start();
	}
}
