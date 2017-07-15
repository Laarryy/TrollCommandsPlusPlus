package me.egg82.tcpp.events.inventory.inventoryClick;

import org.bukkit.event.inventory.InventoryClickEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand<InventoryClickEvent> {
	//vars
	private IRegistry freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(InventoryClickEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (freezeRegistry.hasRegister(event.getWhoClicked().getUniqueId().toString())) {
			event.setCancelled(true);
		}
	}
}
