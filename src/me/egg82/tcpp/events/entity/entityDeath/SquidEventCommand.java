package me.egg82.tcpp.events.entity.entityDeath;

import java.util.UUID;

import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.services.registries.SquidDeathRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SquidEventCommand extends EventCommand<EntityDeathEvent> {
	//vars
	private IRegistry<UUID> squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	
	//constructor
	public SquidEventCommand(EntityDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID uuid = event.getEntity().getUniqueId();
		
		if (squidDeathRegistry.hasRegister(uuid)) {
			event.getDrops().clear();
			event.setDroppedExp(0);
			squidDeathRegistry.removeRegister(uuid);
		}
	}
}
