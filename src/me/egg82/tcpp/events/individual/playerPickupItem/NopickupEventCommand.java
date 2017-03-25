package me.egg82.tcpp.events.individual.playerPickupItem;

import org.bukkit.event.player.PlayerPickupItemEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class NopickupEventCommand extends EventCommand {
	//vars
	private IRegistry noPickupRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.NO_PICKUP_REGISTRY);
	
	//constructor
	public NopickupEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
		
		if (noPickupRegistry.contains(e.getPlayer().getUniqueId().toString())) {
			e.setCancelled(true);
		}
	}
}
