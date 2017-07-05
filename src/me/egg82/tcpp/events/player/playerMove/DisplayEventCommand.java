package me.egg82.tcpp.events.player.playerMove;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.DisplayLocationRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class DisplayEventCommand extends EventCommand {
	//vars
	private IRegistry displayLocationRegistry = (IRegistry) ServiceLocator.getService(DisplayLocationRegistry.class);
	
	//constructor
	public DisplayEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		String uuid = player.getUniqueId().toString();
		
		if (displayLocationRegistry.hasRegister(uuid)) {
			Location loc = (Location) displayLocationRegistry.getRegister(uuid);
			if (e.getTo().distanceSquared(loc) >= 4) {
				e.setCancelled(true);
				player.teleport(loc);
			}
		}
	}
}
