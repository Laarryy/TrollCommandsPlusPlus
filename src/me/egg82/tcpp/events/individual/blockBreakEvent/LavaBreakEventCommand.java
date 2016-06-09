package me.egg82.tcpp.events.individual.blockBreakEvent;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class LavaBreakEventCommand extends EventCommand {
	//vars
	private IRegistry lavaBreakRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.LAVA_BREAK_REGISTRY);
	
	//constructor
	public LavaBreakEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		BlockBreakEvent e = (BlockBreakEvent) event;
		String name = e.getPlayer().getName().toLowerCase();
		
		if (lavaBreakRegistry.contains(name)) {
			e.setCancelled(true);
			e.getBlock().setType(Material.LAVA);
			lavaBreakRegistry.setRegister(name, null);
		}
	}
}
