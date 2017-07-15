package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerPickupItemEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerPickupItemEventCommand extends EventCommand<PlayerPickupItemEvent> {
	//vars
	private ArrayList<EventCommand<PlayerPickupItemEvent>> events = new ArrayList<EventCommand<PlayerPickupItemEvent>>();
	
	//constructor
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayerPickupItemEventCommand(PlayerPickupItemEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerPickupItem");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerPickupItemEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerPickupItemEvent.class).newInstance(event);
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
