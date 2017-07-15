package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.FreezeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class FreezeEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	IRegistry freezeRegistry = ServiceLocator.getService(FreezeRegistry.class);
	
	//constructor
	public FreezeEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (freezeRegistry.hasRegister(uuid)) {
			freezeRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
