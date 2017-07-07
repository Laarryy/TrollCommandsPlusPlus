package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.KillRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DelayKillEventCommand extends EventCommand {
	//vars
	private IRegistry killRegistry = (IRegistry) ServiceLocator.getService(KillRegistry.class);
	
	//constructor
	public DelayKillEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		String uuid = e.getEntity().getUniqueId().toString();
		
		killRegistry.setRegister(uuid, Player.class, null);
	}
}
