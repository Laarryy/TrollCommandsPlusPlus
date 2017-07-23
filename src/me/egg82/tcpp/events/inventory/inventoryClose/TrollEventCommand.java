package me.egg82.tcpp.events.inventory.inventoryClose;

import java.util.UUID;

import org.bukkit.event.inventory.InventoryCloseEvent;

import me.egg82.tcpp.services.TrollInventoryRegistry;
import me.egg82.tcpp.services.TrollPageRegistry;
import me.egg82.tcpp.services.TrollPlayerRegistry;
import me.egg82.tcpp.services.TrollSearchRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class TrollEventCommand extends EventCommand<InventoryCloseEvent> {
	//vars
	private IRegistry<UUID> trollInventoryRegistry = ServiceLocator.getService(TrollInventoryRegistry.class);
	private IRegistry<UUID> trollPlayerRegistry = ServiceLocator.getService(TrollPlayerRegistry.class);
	private IRegistry<UUID> trollPageRegistry = ServiceLocator.getService(TrollPageRegistry.class);
	private IRegistry<UUID> trollSearchRegistry = ServiceLocator.getService(TrollSearchRegistry.class);
	
	//constructor
	public TrollEventCommand(InventoryCloseEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID uuid = event.getPlayer().getUniqueId();
		
		// This is one lookup faster than not having it. Optimize where we can.
		if (!trollInventoryRegistry.hasRegister(uuid)) {
			return;
		}
		
		trollInventoryRegistry.removeRegister(uuid);
		trollPlayerRegistry.removeRegister(uuid);
		trollPageRegistry.removeRegister(uuid);
		trollSearchRegistry.removeRegister(uuid);
	}
}
