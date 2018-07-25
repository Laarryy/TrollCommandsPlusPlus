package me.egg82.tcpp.events.inventory.inventoryClose;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryCloseEvent;

import me.egg82.tcpp.registries.RandomMenuMenuRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class RandomMenuEventCommand extends EventHandler<InventoryCloseEvent> {
	//vars
	private IVariableRegistry<UUID> randomMenuMenuRegistry = ServiceLocator.getService(RandomMenuMenuRegistry.class);
	
	//constructor
	public RandomMenuEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		randomMenuMenuRegistry.removeRegister(event.getPlayer().getUniqueId());
	}
}
