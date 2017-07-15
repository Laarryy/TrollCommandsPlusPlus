package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class AsyncPlayerChatEventCommand extends EventCommand<AsyncPlayerChatEvent> {
	//vars
	private ArrayList<EventCommand<AsyncPlayerChatEvent>> events = new ArrayList<EventCommand<AsyncPlayerChatEvent>>();
	
	//constructor
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AsyncPlayerChatEventCommand(AsyncPlayerChatEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.asyncPlayerChat");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<AsyncPlayerChatEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(AsyncPlayerChatEvent.class).newInstance(event);
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
