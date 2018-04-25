package me.egg82.tcpp.events.inventory.inventoryClose;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryCloseEvent;

import me.egg82.tcpp.services.registries.RandomMenuMenuRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class RandomMenuEventCommand extends EventCommand<InventoryCloseEvent> {
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
