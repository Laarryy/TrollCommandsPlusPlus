package me.egg82.tcpp.events.player.playerJoin;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerJoinEvent;

import me.egg82.tcpp.services.ExplodeBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class ExplodeBreakEventCommand extends EventCommand {
	//vars
	private IRegistry explodeBreakRegistry = (IRegistry) ServiceLocator.getService(ExplodeBreakRegistry.class);
	
	//constructor
	public ExplodeBreakEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerJoinEvent e = (PlayerJoinEvent) event;
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (explodeBreakRegistry.hasRegister(uuid)) {
			explodeBreakRegistry.setRegister(uuid, Player.class, player);
		}
	}
}
