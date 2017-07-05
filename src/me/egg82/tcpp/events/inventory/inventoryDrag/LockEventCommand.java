package me.egg82.tcpp.events.inventory.inventoryDrag;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryDragEvent;

import me.egg82.tcpp.services.LockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LockEventCommand extends EventCommand {
	//vars
	private IRegistry lockRegistry = (IRegistry) ServiceLocator.getService(LockRegistry.class);
	
	//constructor
	public LockEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		InventoryDragEvent e = (InventoryDragEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = (Player) e.getWhoClicked();
		
		if (lockRegistry.hasRegister(player.getUniqueId().toString())) {
			if (e.getInventory().getHolder() instanceof Player) {
				if (((Player) e.getInventory().getHolder()).getUniqueId().compareTo(player.getUniqueId()) == 0) {
					e.setCancelled(true);
				}
			}
		}
	}
}
