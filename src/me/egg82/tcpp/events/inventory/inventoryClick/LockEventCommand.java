package me.egg82.tcpp.events.inventory.inventoryClick;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.egg82.tcpp.services.LockRegistry;
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
			if (event.getClickedInventory() != null && (event.getClickedInventory().getHolder() instanceof Player)) {
				if (((Player) event.getClickedInventory().getHolder()).getUniqueId().equals(player.getUniqueId())) {
					event.setCancelled(true);
				}
			} else if (event.getClickedInventory() == null) {
				event.setCancelled(true);
			}
		}
	}
}
