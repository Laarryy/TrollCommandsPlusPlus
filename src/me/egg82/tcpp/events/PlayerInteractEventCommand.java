package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerInteractEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerInteractEventCommand extends EventCommand<PlayerInteractEvent> {
	//vars
	private ArrayList<EventCommand<PlayerInteractEvent>> events = new ArrayList<EventCommand<PlayerInteractEvent>>();
	
	//constructor
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayerInteractEventCommand(PlayerInteractEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerInteract");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerInteractEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerInteractEvent.class).newInstance(event);
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
