package me.egg82.tcpp.events.player.playerRespawn;

import org.bukkit.entity.Item;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.VegetableItemRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand<PlayerRespawnEvent> {
	//vars
	private IRegistry vegetableItemRegistry = ServiceLocator.getService(VegetableItemRegistry.class);
	
	//constructor
	public VegetableEventCommand(PlayerRespawnEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		Item groundItem = vegetableItemRegistry.getRegister(event.getPlayer().getUniqueId().toString(), Item.class);
		if (groundItem != null) {
			event.setRespawnLocation(groundItem.getLocation());
		}
	}
}
