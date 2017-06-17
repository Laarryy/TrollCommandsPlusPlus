package me.egg82.tcpp.events.inventory.inventoryClick;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import me.egg82.tcpp.services.LockRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LockEventCommand extends EventCommand {
	//vars
	IRegistry lockRegistry = (IRegistry) ServiceLocator.getService(LockRegistry.class);
	
	//constructor
	public LockEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		InventoryClickEvent e = (InventoryClickEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = (Player) e.getWhoClicked();
		InventoryType invType = e.getClickedInventory().getType();
		
		if (lockRegistry.hasRegister(player.getUniqueId().toString()) && (invType == InventoryType.PLAYER || invType == InventoryType.CREATIVE)) {
			e.setCancelled(true);
		}
	}
}
