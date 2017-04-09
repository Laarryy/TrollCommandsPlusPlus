package me.egg82.tcpp.events.entity.playerDeath;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.egg82.tcpp.services.SpartaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SpartaEventCommand extends EventCommand {
	//vars
	private IRegistry spartaRegistry = (IRegistry) ServiceLocator.getService(SpartaRegistry.class);
	
	//constructor
	public SpartaEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerDeathEvent e = (PlayerDeathEvent) event;
		spartaRegistry.setRegister(e.getEntity().getUniqueId().toString(), Player.class, null);
	}
}
