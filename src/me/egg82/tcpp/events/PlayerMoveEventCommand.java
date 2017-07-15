package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerMoveEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerMoveEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private ArrayList<EventCommand<PlayerMoveEvent>> events = new ArrayList<EventCommand<PlayerMoveEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerMoveEventCommand(PlayerMoveEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerMove");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerMoveEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerMoveEvent.class).newInstance(event);
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
