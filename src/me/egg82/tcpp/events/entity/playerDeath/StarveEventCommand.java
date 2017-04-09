package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.StarveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class StarveEventCommand extends EventCommand {
	//vars
	private IRegistry starveRegistry = (IRegistry) ServiceLocator.getService(StarveRegistry.class);
	
	//constructor
	public StarveEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		starveRegistry.setRegister(e.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
