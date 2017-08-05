package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.InventoryOpenEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class InventoryOpenEventCommand extends EventCommand<InventoryOpenEvent> {
	//vars
	private ArrayList<EventCommand<InventoryOpenEvent>> events = new ArrayList<EventCommand<InventoryOpenEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InventoryOpenEventCommand(InventoryOpenEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.inventory.inventoryOpen");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<InventoryOpenEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(InventoryOpenEvent.class).newInstance(event);
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
