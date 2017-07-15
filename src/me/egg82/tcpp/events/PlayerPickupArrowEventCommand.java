package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerPickupArrowEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerPickupArrowEventCommand extends EventCommand<PlayerPickupArrowEvent> {
	//vars
	private ArrayList<EventCommand<PlayerPickupArrowEvent>> events = new ArrayList<EventCommand<PlayerPickupArrowEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerPickupArrowEventCommand(PlayerPickupArrowEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerPickupArrow");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerPickupArrowEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerPickupArrowEvent.class).newInstance(event);
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
