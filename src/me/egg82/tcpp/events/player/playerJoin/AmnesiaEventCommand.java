package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.AmnesiaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AmnesiaEventCommand extends EventCommand {
	//vars
	private IRegistry amnesiaRegistry = (IRegistry) ServiceLocator.getService(AmnesiaRegistry.class);
	
	//constructor
	public AmnesiaEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (amnesiaRegistry.hasRegister(uuid)) {
			amnesiaRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
