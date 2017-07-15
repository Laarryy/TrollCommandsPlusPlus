package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.player.PlayerItemConsumeEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class PlayerItemConsumeEventCommand extends EventCommand<PlayerItemConsumeEvent> {
	//vars
	private ArrayList<EventCommand<PlayerItemConsumeEvent>> events = new ArrayList<EventCommand<PlayerItemConsumeEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PlayerItemConsumeEventCommand(PlayerItemConsumeEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.player.playerItemConsume");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<PlayerItemConsumeEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(PlayerItemConsumeEvent.class).newInstance(event);
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
