package me.egg82.tcpp.events.entity.playerDeath;

import java.util.UUID;

import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.HurtRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class HurtEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry<UUID> hurtRegistry = ServiceLocator.getService(HurtRegistry.class);
	
	//constructor
	public HurtEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		hurtRegistry.removeRegister(event.getEntity().getUniqueId());
	}
}
