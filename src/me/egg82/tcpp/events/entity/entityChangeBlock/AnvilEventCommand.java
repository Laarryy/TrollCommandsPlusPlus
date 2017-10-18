package me.egg82.tcpp.events.entity.entityChangeBlock;

import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import me.egg82.tcpp.services.registries.AnvilRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AnvilEventCommand extends EventCommand<EntityChangeBlockEvent> {
	//vars
	private IRegistry<UUID> anvilRegistry = ServiceLocator.getService(AnvilRegistry.class);
	
	//constructor
	public AnvilEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Entity entity = event.getEntity();
		UUID uuid = entity.getUniqueId();
		
		if (anvilRegistry.hasRegister(uuid)) {
			entity.getWorld().playSound(entity.getLocation(), Sound.BLOCK_ANVIL_LAND, 1.0f, 1.0f);
			entity.remove();
			event.setCancelled(true);
			anvilRegistry.removeRegister(uuid);
		}
	}
}
