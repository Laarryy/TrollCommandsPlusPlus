package me.egg82.tcpp.events.individual.playerInteract;

import org.bukkit.event.player.PlayerInteractEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class VegetableEventCommand extends EventCommand {
	//vars
	private IRegistry vegetableRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.VEGETABLE_REGISTRY);
	
	//constructor
	public VegetableEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		PlayerInteractEvent e = (PlayerInteractEvent) event;
		
		if (vegetableRegistry.contains(e.getPlayer().getName().toLowerCase())) {
			e.setCancelled(true);
		}
	}
}
