package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerJoinEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerJoinEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private ArrayList<EventCommand<PlayerJoinEvent>> events = new ArrayList<EventCommand<PlayerJoinEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerJoinEventCommand(PlayerJoinEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerJoin");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerJoinEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerJoinEvent.class).newInstance(event);
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
