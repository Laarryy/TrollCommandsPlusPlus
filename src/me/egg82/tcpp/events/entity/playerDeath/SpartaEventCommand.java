package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.SpartaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SpartaEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry spartaRegistry = ServiceLocator.getService(SpartaRegistry.class);
	
	//constructor
	public SpartaEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		spartaRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
