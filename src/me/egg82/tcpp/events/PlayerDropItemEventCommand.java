package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerDropItemEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerDropItemEventCommand extends EventCommand<PlayerDropItemEvent> {
	//vars
	private ArrayList<EventCommand<PlayerDropItemEvent>> events = new ArrayList<EventCommand<PlayerDropItemEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerDropItemEventCommand(PlayerDropItemEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerDropItem");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerDropItemEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerDropItemEvent.class).newInstance(event);
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