package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.StarveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class StarveEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry<UUID> starveRegistry = ServiceLocator.getService(StarveRegistry.class);
	
	//constructor
	public StarveEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		starveRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
