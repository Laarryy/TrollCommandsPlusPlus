package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.BurnRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class BurnEventCommand extends EventCommand {
	//vars
	IRegistry burnRegistry = (IRegistry) ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		burnRegistry.setRegister(e.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
