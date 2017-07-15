package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.StarveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class StarveEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry starveRegistry = ServiceLocator.getService(StarveRegistry.class);
	
	//constructor
	public StarveEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		starveRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
