package me.egg82.tcpp.events.inventory.inventoryClose;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import me.egg82.tcpp.services.TrollInventoryRegistry;
import me.egg82.tcpp.services.TrollPageRegistry;
import me.egg82.tcpp.services.TrollPlayerRegistry;
import me.egg82.tcpp.services.TrollSearchRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class TrollEventCommand extends EventCommand<InventoryCloseEvent> {
	//vars
	private IRegistry trollInventoryRegistry = ServiceLocator.getService(TrollInventoryRegistry.class);
	private IRegistry trollPlayerRegistry = ServiceLocator.getService(TrollPlayerRegistry.class);
	private IRegistry trollPageRegistry = ServiceLocator.getService(TrollPageRegistry.class);
	private IRegistry trollSearchRegistry = ServiceLocator.getService(TrollSearchRegistry.class);
	
	//constructor
	public TrollEventCommand(InventoryCloseEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String uuid = event.getPlayer().getUniqueId().toString();
		
		// This is one lookup faster than not having it. Optimize where we can.
		if (!trollInventoryRegistry.hasRegister(uuid)) {
			return;
		}
		
		trollInventoryRegistry.setRegister(uuid, Inventory.class, null);
		trollPlayerRegistry.setRegister(uuid, Player.class, null);
		trollPageRegistry.setRegister(uuid, Integer.class, null);
		trollSearchRegistry.setRegister(uuid, String.class, null);
	}
}
