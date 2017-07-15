package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityDamageByEntityEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityDamageByEntityEventCommand extends EventCommand<EntityDamageByEntityEvent> {
	//vars
	private ArrayList<EventCommand<EntityDamageByEntityEvent>> events = new ArrayList<EventCommand<EntityDamageByEntityEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityDamageByEntityEventCommand(EntityDamageByEntityEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityDamageByEntity");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityDamageByEntityEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityDamageByEntityEvent.class).newInstance(event);
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
