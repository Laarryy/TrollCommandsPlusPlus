package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.LavaBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LavaBreakEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry lavaBreakRegistry = ServiceLocator.getService(LavaBreakRegistry.class);
	
	//constructor
	public LavaBreakEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (lavaBreakRegistry.hasRegister(uuid)) {
			lavaBreakRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
