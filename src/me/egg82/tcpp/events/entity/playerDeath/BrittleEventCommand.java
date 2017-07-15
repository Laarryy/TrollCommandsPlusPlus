package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BrittleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BrittleEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry brittleRegistry = ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		brittleRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
