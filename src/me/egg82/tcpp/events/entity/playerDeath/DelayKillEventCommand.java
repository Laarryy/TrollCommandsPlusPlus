package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.KillRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DelayKillEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry killRegistry = ServiceLocator.getService(KillRegistry.class);
	
	//constructor
	public DelayKillEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		killRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
