package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BurnRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BurnEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry<UUID> burnRegistry = ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		burnRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
