package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BombRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BombEventCommand extends EventCommand {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(BombRegistry.class);
	
	//constructor
	public BombEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		bombRegistry.setRegister(e.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
