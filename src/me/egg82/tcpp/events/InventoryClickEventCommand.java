package me.egg82.tcpp.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.inventory.InventoryClickEvent;

import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.utils.ReflectUtil;

public class InventoryClickEventCommand extends EventCommand<InventoryClickEvent> {
	//vars
	private ArrayList<EventCommand<InventoryClickEvent>> events = new ArrayList<EventCommand<InventoryClickEvent>>();
	
	//constructor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public InventoryClickEventCommand(InventoryClickEvent event) {
		super(event);
		
		List<Class<? extends EventCommand>> commands = ReflectUtil.getClasses(EventCommand.class, "me.egg82.tcpp.events.inventory.inventoryClick");
		for (int i = 0; i < commands.size(); i++) {
			EventCommand<InventoryClickEvent> run = null;
			try {
				run = commands.get(i).getDeclaredConstructor(InventoryClickEvent.class).newInstance(event);
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
