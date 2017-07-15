package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerQuitEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerQuitEventCommand extends EventCommand<PlayerQuitEvent> {
	//vars
	private ArrayList<EventCommand<PlayerQuitEvent>> events = new ArrayList<EventCommand<PlayerQuitEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerQuitEventCommand(PlayerQuitEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerQuit");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerQuitEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerQuitEvent.class).newInstance(event);
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
