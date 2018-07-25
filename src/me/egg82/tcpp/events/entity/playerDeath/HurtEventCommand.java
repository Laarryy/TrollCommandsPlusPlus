package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.registries.HurtRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class HurtEventCommand extends EventHandler<PlayerDeathEvent> {
	//vars
	private IVariableRegistry<UUID> hurtRegistry = ServiceLocator.getService(HurtRegistry.class);
	
	//constructor
	public HurtEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		hurtRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
