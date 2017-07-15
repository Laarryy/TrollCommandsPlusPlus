package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityExplodeEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityExplodeEventCommand extends EventCommand<EntityExplodeEvent> {
	//vars
	private ArrayList<EventCommand<EntityExplodeEvent>> events = new ArrayList<EventCommand<EntityExplodeEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityExplodeEventCommand(EntityExplodeEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityExplode");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityExplodeEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityExplodeEvent.class).newInstance(event);
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
