package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.PlayerDeathEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerDeathEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private ArrayList<EventCommand<PlayerDeathEvent>> events = new ArrayList<EventCommand<PlayerDeathEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerDeathEventCommand(PlayerDeathEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.playerDeath");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerDeathEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerDeathEvent.class).newInstance(event);
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