package me.egg82.tcpp.events.player.playerRespawn;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.registries.VegetableLocationRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class VegetableEventCommand extends EventCommand<PlayerRespawnEvent> {
	//vars
	private IVariableRegistry<UUID> vegetableLocationRegistry = ServiceLocator.getService(VegetableLocationRegistry.class);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Location itemLocation = vegetableLocationRegistry.getRegister(event.getPlayer().getUniqueId(), Location.class);
		if (itemLocation != null) {
			event.setRespawnLocation(itemLocation);
		}
	}
}
