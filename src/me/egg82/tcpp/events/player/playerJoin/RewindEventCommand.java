package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.RewindRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class RewindEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry rewindRegistry = ServiceLocator.getService(RewindRegistry.class);
	
	//constructor
	public RewindEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (rewindRegistry.hasRegister(uuid)) {
			rewindRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
