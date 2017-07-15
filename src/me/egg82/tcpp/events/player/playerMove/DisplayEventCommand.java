package me.egg82.tcpp.events.player.playerMove;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.DisplayLocationRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IRegistry displayLocationRegistry = ServiceLocator.getService(DisplayLocationRegistry.class);
	
	//constructor
	public DisplayEventCommand(PlayerMoveEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (displayLocationRegistry.hasRegister(uuid)) {
			Location loc = displayLocationRegistry.getRegister(uuid, Location.class);
			if (event.getTo().distanceSquared(loc) >= 4) {
				event.setCancelled(true);
				player.teleport(loc);
			}
		}
	}
}
