package me.egg82.tcpp.events;

import java.util.ArrayList;

import org.bukkit.event.block.BlockPlaceEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.Util;

public class BlockPlaceEventCommand extends EventCommand {
	//vars
	private ArrayList<EventCommand> commands = new ArrayList<EventCommand>();
	
	//constructor
	public BlockPlaceEventCommand() {
		super();
		
		ArrayList<Class<? extends EventCommand>> enums = Util.getClasses(EventCommand.class, "me.egg82.tcpp.events.individual.blockPlace");
		for (Class<? extends EventCommand> c : enums) {
			try {
				commands.add(c.newInstance());
			} catch (Exception ex) {
				
			}
		}
	}
	
	//public
	
	//private
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		EventCommand command = null;
		for (int i = 0; i < commands.size(); i++) {
			if (e.isCancelled()) {
				return;
			}
			
			command = commands.get(i);
			command.setEvent(event);
			command.start();
		}
	}
}
