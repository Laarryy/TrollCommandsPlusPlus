package me.egg82.tcpp.events;

import java.util.ArrayList;

import org.bukkit.event.block.BlockBreakEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.Util;

public class BlockBreakEventCommand extends EventCommand {
	//vars
	private ArrayList<EventCommand> commands = new ArrayList<EventCommand>();
	
	//constructor
	public BlockBreakEventCommand() {
		super();
		
		ArrayList<Class<? extends EventCommand>> enums = Util.getClasses(EventCommand.class, "me.egg82.tcpp.events.individual.blockBreak");
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
		BlockBreakEvent e = (BlockBreakEvent) event;
		
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
