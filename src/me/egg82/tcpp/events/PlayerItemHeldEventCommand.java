package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerItemHeldEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerItemHeldEventCommand extends EventCommand<PlayerItemHeldEvent> {
	//vars
	private ArrayList<EventCommand<PlayerItemHeldEvent>> events = new ArrayList<EventCommand<PlayerItemHeldEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerItemHeldEventCommand(PlayerItemHeldEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerItemHeld");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerItemHeldEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerItemHeldEvent.class).newInstance(event);
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
