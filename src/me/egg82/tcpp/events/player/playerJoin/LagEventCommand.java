package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LagEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry lagRegistry = ServiceLocator.getService(LagRegistry.class);
	
	//constructor
	public LagEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (lagRegistry.hasRegister(uuid)) {
			lagRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
