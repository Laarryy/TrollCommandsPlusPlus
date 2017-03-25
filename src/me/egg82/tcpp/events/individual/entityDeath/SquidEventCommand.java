package me.egg82.tcpp.events.individual.entityDeath;

import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class SquidEventCommand extends EventCommand {
	//vars
	private IRegistry squidInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SQUID_INTERN_REGISTRY);
	
	//constructor
	public SquidEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		EntityDeathEvent e = (EntityDeathEvent) event;
		
		squidInternRegistry.computeIfPresent(e.getEntity().getUniqueId().toString(), (k, v) -> {
			e.getDrops().clear();
			e.setDroppedExp(0);
			return null;
		});
	}
}
