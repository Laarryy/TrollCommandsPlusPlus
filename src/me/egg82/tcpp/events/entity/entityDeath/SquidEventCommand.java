package me.egg82.tcpp.events.entity.entityDeath;

import org.bukkit.entity.Squid;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.services.SquidDeathRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SquidEventCommand extends EventCommand {
	//vars
	private IRegistry squidDeathRegistry = (IRegistry) ServiceLocator.getService(SquidDeathRegistry.class);
	
	//constructor
	public SquidEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		EntityDeathEvent e = (EntityDeathEvent) event;
		
		String uuid = e.getEntity().getUniqueId().toString();
		
		if (squidDeathRegistry.hasRegister(uuid)) {
			e.getDrops().clear();
			e.setDroppedExp(0);
			squidDeathRegistry.setRegister(uuid, Squid.class, null);
		}
	}
}
