package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityShootBowEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityShootBowEventCommand extends EventCommand<EntityShootBowEvent> {
	//vars
	private ArrayList<EventCommand<EntityShootBowEvent>> events = new ArrayList<EventCommand<EntityShootBowEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityShootBowEventCommand(EntityShootBowEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityShootBow");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityShootBowEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityShootBowEvent.class).newInstance(event);
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
