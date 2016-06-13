package me.egg82.tcpp.events.individual.playerPickupItemEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerPickupItemEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class NopickupEventCommand extends EventCommand {
	//vars
	private IRegistry nopickupRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.NOPICKUP_REGISTRY);
	
	//constructor
	public NopickupEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerPickupItemEvent e = (PlayerPickupItemEvent) event;
		
		if (nopickupRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setCancelled(true);
		}
	}
}
