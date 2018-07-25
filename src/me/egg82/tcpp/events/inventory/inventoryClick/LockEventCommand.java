package me.egg82.tcpp.events.inventory.inventoryClick;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.egg82.tcpp.registries.LockRegistry;
import me.egg82.tcpp.util.InventoryUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class LockEventCommand extends EventHandler<InventoryClickEvent> {
	//vars
	private IVariableRegistry<UUID> lockRegistry = ServiceLocator.getService(LockRegistry.class);
	
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
			Inventory clicked = InventoryUtil.getClickedInventory(event);
			
			if (clicked != null && (clicked.getHolder() instanceof Player)) {
				if (((Player) clicked.getHolder()).getUniqueId().equals(player.getUniqueId())) {
					event.setCancelled(true);
				}
			} else if (clicked == null) {
				event.setCancelled(true);
			}
		}
	}
}
