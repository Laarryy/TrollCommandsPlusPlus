package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.ExplodeBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ExplodeBreakEventCommand extends EventCommand<PlayerJoinEvent> {
	//vars
	private IRegistry explodeBreakRegistry = ServiceLocator.getService(ExplodeBreakRegistry.class);
	
	//constructor
	public ExplodeBreakEventCommand(PlayerJoinEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (explodeBreakRegistry.hasRegister(uuid)) {
			explodeBreakRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
