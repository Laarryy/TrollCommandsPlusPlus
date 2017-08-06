package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityTargetEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityTargetEventCommand extends EventCommand<EntityTargetEvent> {
	//vars
	private ArrayList<EventCommand<EntityTargetEvent>> events = new ArrayList<EventCommand<EntityTargetEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityTargetEventCommand(EntityTargetEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityTarget");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityTargetEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityTargetEvent.class).newInstance(event);
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
