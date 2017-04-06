package me.egg82.tcpp.events.block.blockBreak;

import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand {
	//vars
	
	//constructor
	public DisplayEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		//TODO Finish this
	}
}
