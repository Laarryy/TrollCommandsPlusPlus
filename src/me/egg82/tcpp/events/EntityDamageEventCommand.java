package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityDamageEventCommand extends EventCommand<EntityDamageEvent> {
	//vars
	private ArrayList<EventCommand<EntityDamageEvent>> events = new ArrayList<EventCommand<EntityDamageEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityDamageEventCommand(EntityDamageEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityDamage");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityDamageEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityDamageEvent.class).newInstance(event);
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
