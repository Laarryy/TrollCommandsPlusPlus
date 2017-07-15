package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerTeleportEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerTeleportEventCommand extends EventCommand<PlayerTeleportEvent> {
	//vars
	private ArrayList<EventCommand<PlayerTeleportEvent>> events = new ArrayList<EventCommand<PlayerTeleportEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerTeleportEventCommand(PlayerTeleportEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerTeleport");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerTeleportEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerTeleportEvent.class).newInstance(event);
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
