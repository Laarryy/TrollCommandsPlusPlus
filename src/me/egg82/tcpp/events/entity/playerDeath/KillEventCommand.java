package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.KillRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class KillEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry<UUID> killRegistry = ServiceLocator.getService(KillRegistry.class);
	
	//constructor
	public KillEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		killRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
