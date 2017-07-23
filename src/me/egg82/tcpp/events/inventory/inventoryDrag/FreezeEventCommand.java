package me.egg82.tcpp.events.inventory.inventoryDrag;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryDragEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand<InventoryDragEvent> {
	//vars
	private IRegistry<UUID> freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(InventoryDragEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		if (freezeRegistry.hasRegister(event.getWhoClicked().getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
