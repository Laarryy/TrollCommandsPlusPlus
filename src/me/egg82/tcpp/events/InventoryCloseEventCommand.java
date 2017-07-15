package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.InventoryCloseEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class InventoryCloseEventCommand extends EventCommand<InventoryCloseEvent> {
	//vars
	private ArrayList<EventCommand<InventoryCloseEvent>> events = new ArrayList<EventCommand<InventoryCloseEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InventoryCloseEventCommand(InventoryCloseEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.inventory.inventoryClose");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<InventoryCloseEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(InventoryCloseEvent.class).newInstance(event);
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
