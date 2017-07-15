package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.SpartaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SpartaEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry spartaRegistry = ServiceLocator.getService(SpartaRegistry.class);
	
	//constructor
	public SpartaEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (spartaRegistry.hasRegister(uuid)) {
			spartaRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
