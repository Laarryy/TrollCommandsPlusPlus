package me.egg82.tcpp.events.player.playerRespawn;

import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerRespawnEvent;

import me.egg82.tcpp.services.VegetableItemRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableItemRegistry = (IRegistry) ServiceLocator.getService(VegetableItemRegistry.class);
	
	//constructor
	public VegetableEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerRespawnEvent e = (PlayerRespawnEvent) event;
		
		Item groundItem = (Item) vegetableItemRegistry.getRegister(e.getPlayer().getUniqueId().toString());
		if (groundItem != null) {
			e.setRespawnLocation(groundItem.getLocation());
		}
	}
}
