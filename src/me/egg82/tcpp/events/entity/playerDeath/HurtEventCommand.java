package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.HurtRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class HurtEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	IRegistry hurtRegistry = ServiceLocator.getService(HurtRegistry.class);
	
	//constructor
	public HurtEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		hurtRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
