package me.egg82.tcpp.events.inventory.inventoryDrag;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;

import me.egg82.tcpp.services.registries.LockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LockEventCommand extends EventCommand<InventoryDragEvent> {
	//vars
	private IRegistry<UUID> lockRegistry = ServiceLocator.getService(LockRegistry.class);
	
	//constructor
	public LockEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		
		if (lockRegistry.hasRegister(player.getUniqueId())) {
			if (event.getInventory().getHolder() instanceof Player) {
				if (((Player) event.getInventory().getHolder()).getUniqueId().equals(player.getUniqueId())) {
					event.setCancelled(true);
				}
			}
		}
	}
}
