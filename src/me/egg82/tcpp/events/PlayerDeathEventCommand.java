package me.egg82.tcpp.events;

import java.util.List;

import org.bukkit.event.Event;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerDeathEventCommand extends EventCommand {
	//vars
	
	//constructor
	public PlayerDeathEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		List<Class<EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.playerDeath");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand c = null;
			try {
				c = commands.get(i).getDeclaredConstructor(Event.class).newInstance(event);
				c.start();
			} catch (Exception ex) {
				
			}
		}
	}
}