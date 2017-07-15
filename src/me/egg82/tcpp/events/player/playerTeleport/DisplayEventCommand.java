package me.egg82.tcpp.events.player.playerTeleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import me.egg82.tcpp.services.DisplayLocationRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand<PlayerTeleportEvent> {
	//vars
	private IRegistry displayLocationRegistry = ServiceLocator.getService(DisplayLocationRegistry.class);
	
	//constructor
	public DisplayEventCommand(PlayerTeleportEvent event) {
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
			Location loc = (Location) displayLocationRegistry.getRegister(uuid);
			if (event.getTo().distanceSquared(loc) >= 4) {
				event.setCancelled(true);
				player.teleport(loc);
			}
		}
	}
}
