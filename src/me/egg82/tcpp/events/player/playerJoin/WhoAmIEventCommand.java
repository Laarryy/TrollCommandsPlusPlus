package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.WhoAmIRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class WhoAmIEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry whoAmIRegistry = ServiceLocator.getService(WhoAmIRegistry.class);
	
	//constructor
	public WhoAmIEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (whoAmIRegistry.hasRegister(uuid)) {
			whoAmIRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
