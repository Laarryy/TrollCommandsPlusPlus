package me.egg82.tcpp.events;

import java.util.ArrayList;

import org.bukkit.event.Event;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerJoinEventCommand extends EventCommand {
	//vars
	
	//constructor
	public PlayerJoinEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		ArrayList<Class<EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerJoin");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand c = null;
			try {
				c = commands.get(i).getDeclaredConstructor(Event.class).newInstance(event);
			} catch (Exception ex) {
				throw new RuntimeException("Event cannot be initialized.", ex);
			}
			c.start();
		}
	}
}
