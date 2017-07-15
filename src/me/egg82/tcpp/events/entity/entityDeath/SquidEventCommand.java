package me.egg82.tcpp.events.entity.entityDeath;

import org.bukkit.entity.Squid;
import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.services.SquidDeathRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SquidEventCommand extends EventCommand<EntityDeathEvent> {
	//vars
	private IRegistry squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	
	//constructor
	public SquidEventCommand(EntityDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String uuid = event.getEntity().getUniqueId().toString();
		
		if (squidDeathRegistry.hasRegister(uuid)) {
			event.getDrops().clear();
			event.setDroppedExp(0);
			squidDeathRegistry.setRegister(uuid, Squid.class, null);
		}
	}
}
