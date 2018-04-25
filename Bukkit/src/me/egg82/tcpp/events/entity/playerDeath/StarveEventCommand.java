package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.StarveRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class StarveEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IVariableRegistry<UUID> starveRegistry = ServiceLocator.getService(StarveRegistry.class);
	
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
