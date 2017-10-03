package me.egg82.tcpp.events.inventory.inventoryClick;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import me.egg82.tcpp.services.registries.LockRegistry;
import me.egg82.tcpp.util.InventoryUtil;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LockEventCommand extends EventCommand<InventoryClickEvent> {
	//vars
	private IRegistry<UUID> lockRegistry = ServiceLocator.getService(LockRegistry.class);
	
	//constructor
	public LockEventCommand(InventoryClickEvent event) {
		super(event);
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
