package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.entity.ItemDespawnEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class ItemDespawnEventCommand extends EventCommand<ItemDespawnEvent> {
	//vars
	private ArrayList<EventCommand<ItemDespawnEvent>> events = new ArrayList<EventCommand<ItemDespawnEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ItemDespawnEventCommand(ItemDespawnEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.entity.itemDespawn");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<ItemDespawnEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(ItemDespawnEvent.class).newInstance(event);
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
