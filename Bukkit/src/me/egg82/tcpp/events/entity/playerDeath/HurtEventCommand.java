package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.HurtRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class HurtEventCommand extends EventCommand<PlayerDeathEvent> {
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
