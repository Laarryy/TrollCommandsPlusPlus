package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class EntityTargetLivingEntityEventCommand extends EventCommand<EntityTargetLivingEntityEvent> {
	//vars
	private ArrayList<EventCommand<EntityTargetLivingEntityEvent>> events = new ArrayList<EventCommand<EntityTargetLivingEntityEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public EntityTargetLivingEntityEventCommand(EntityTargetLivingEntityEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.entityTargetLivingEntity");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<EntityTargetLivingEntityEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(EntityTargetLivingEntityEvent.class).newInstance(event);
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
