package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.InventoryDragEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class InventoryDragEventCommand extends EventCommand<InventoryDragEvent> {
	//vars
	private ArrayList<EventCommand<InventoryDragEvent>> events = new ArrayList<EventCommand<InventoryDragEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InventoryDragEventCommand(InventoryDragEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.inventory.inventoryDrag");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<InventoryDragEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(InventoryDragEvent.class).newInstance(event);
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
