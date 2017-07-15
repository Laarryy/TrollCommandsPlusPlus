package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BurnRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BurnEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	IRegistry burnRegistry = ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		burnRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
