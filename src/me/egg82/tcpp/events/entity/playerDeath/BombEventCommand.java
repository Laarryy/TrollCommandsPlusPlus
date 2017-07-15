package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BombRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BombEventCommand extends EventCommand<PlayerDeathEvent> {
	//vars
	private IRegistry bombRegistry = ServiceLocator.getService(BombRegistry.class);
	
	//constructor
	public BombEventCommand(PlayerDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		bombRegistry.setRegister(event.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
