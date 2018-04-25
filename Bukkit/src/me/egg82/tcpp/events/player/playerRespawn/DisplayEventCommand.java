package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.registries.DisplayLocationRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class DisplayEventCommand extends EventCommand<PlayerRespawnEvent> {
	//vars
	private IVariableRegistry<UUID> displayLocationRegistry = ServiceLocator.getService(DisplayLocationRegistry.class);
	
	//constructor
	public DisplayEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Location loc = displayLocationRegistry.getRegister(event.getPlayer().getUniqueId(), Location.class);
		if (loc != null) {
			event.setRespawnLocation(loc);
		}
	}
}
