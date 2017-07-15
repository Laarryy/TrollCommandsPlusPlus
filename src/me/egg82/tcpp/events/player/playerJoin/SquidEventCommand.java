package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.SquidRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SquidEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry squidRegistry = ServiceLocator.getService(SquidRegistry.class);
	
	//constructor
	public SquidEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (squidRegistry.hasRegister(uuid)) {
			squidRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
