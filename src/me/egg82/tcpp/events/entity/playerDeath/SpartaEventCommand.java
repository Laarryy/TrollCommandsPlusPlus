package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.registries.SpartaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SpartaEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry<UUID> spartaRegistry = ServiceLocator.getService(SpartaRegistry.class);
	
	//constructor
	public SpartaEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		spartaRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
