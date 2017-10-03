package me.egg82.tcpp.events.inventory.inventoryClose;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryCloseEvent;

import me.egg82.tcpp.services.registries.RandomMenuMenuRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class RandomMenuEventCommand extends EventCommand<InventoryCloseEvent> {
	//vars
	private IRegistry<UUID> randomMenuMenuRegistry = ServiceLocator.getService(RandomMenuMenuRegistry.class);
	
	//constructor
	public RandomMenuEventCommand(InventoryCloseEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		randomMenuMenuRegistry.removeRegister(event.getPlayer().getUniqueId());
	}
}
