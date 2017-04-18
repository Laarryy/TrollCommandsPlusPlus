package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.Event;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class BlockPlaceEventCommand extends EventCommand {
	//vars
	private ArrayList<EventCommand> events = new ArrayList<EventCommand>();
	
	//constructor
	public BlockPlaceEventCommand(Event event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.block.blockPlace");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(Event.class).newInstance(event);
			} catch (Exception ex) {
				continue;
			}
			events.add(run);
		}
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		events.forEach((v) -> {
			v.setEvent(event);
			v.start();
		});
	}
}
