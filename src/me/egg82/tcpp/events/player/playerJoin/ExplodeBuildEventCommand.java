package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.ExplodeBuildRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ExplodeBuildEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry explodeBuildRegistry = ServiceLocator.getService(ExplodeBuildRegistry.class);
	
	//constructor
	public ExplodeBuildEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (explodeBuildRegistry.hasRegister(uuid)) {
			explodeBuildRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
