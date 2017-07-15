package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityDeathEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityDeathEventCommand extends EventCommand<EntityDeathEvent> {
	//vars
	private ArrayList<EventCommand<EntityDeathEvent>> events = new ArrayList<EventCommand<EntityDeathEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityDeathEventCommand(EntityDeathEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityDeath");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityDeathEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityDeathEvent.class).newInstance(event);
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
