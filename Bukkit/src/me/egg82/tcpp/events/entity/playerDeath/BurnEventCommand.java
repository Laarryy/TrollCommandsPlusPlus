package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.BurnRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class BurnEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IVariableRegistry<UUID> burnRegistry = ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		burnRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
