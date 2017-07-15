package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.KillRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DelayKillEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry killRegistry = ServiceLocator.getService(KillRegistry.class);
	
	//constructor
	public DelayKillEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (killRegistry.hasRegister(uuid)) {
			killRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
