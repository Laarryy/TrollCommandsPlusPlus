package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityChangeBlockEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityChangeBlockEventCommand extends EventCommand<EntityChangeBlockEvent> {
	//vars
	private ArrayList<EventCommand<EntityChangeBlockEvent>> events = new ArrayList<EventCommand<EntityChangeBlockEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityChangeBlockEventCommand(EntityChangeBlockEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityChangeBlock");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityChangeBlockEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityChangeBlockEvent.class).newInstance(event);
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
