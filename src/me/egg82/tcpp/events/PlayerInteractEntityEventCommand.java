package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerInteractEntityEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerInteractEntityEventCommand extends EventCommand<PlayerInteractEntityEvent> {
	//vars
	private ArrayList<EventCommand<PlayerInteractEntityEvent>> events = new ArrayList<EventCommand<PlayerInteractEntityEvent>>();
	
	//constructor
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PlayerInteractEntityEventCommand(PlayerInteractEntityEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerInteractEntity");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerInteractEntityEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerInteractEntityEvent.class).newInstance(event);
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
