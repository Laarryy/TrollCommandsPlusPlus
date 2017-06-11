package me.egg82.tcpp.events.inventory.inventoryClose;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.egg82.tcpp.services.TrollInventoryRegistry;
import me.egg82.tcpp.services.TrollPlayerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class TrollEventCommand extends EventCommand {
	//vars
	private IRegistry trollInventoryRegistry = (IRegistry) ServiceLocator.getService(TrollInventoryRegistry.class);
	private IRegistry trollPlayerRegistry = (IRegistry) ServiceLocator.getService(TrollPlayerRegistry.class);
	
	//constructor
	public TrollEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		InventoryCloseEvent e = (InventoryCloseEvent) event;
		
		String uuid = e.getPlayer().getUniqueId().toString();
		
		// This is one lookup faster than not having it. Optimize where we can.
		if (!trollInventoryRegistry.hasRegister(uuid)) {
			return;
		}
		
		trollInventoryRegistry.setRegister(uuid, Inventory.class, null);
		trollPlayerRegistry.setRegister(uuid, Player.class, null);
	}
}
