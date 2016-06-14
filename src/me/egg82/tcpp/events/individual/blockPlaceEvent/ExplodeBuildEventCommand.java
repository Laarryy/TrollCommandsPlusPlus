package me.egg82.tcpp.events.individual.blockPlaceEvent;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class ExplodeBuildEventCommand extends EventCommand {
	//vars
	private IRegistry explodeBuildRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.EXPLODE_BUILD_REGISTRY);
	
	//constructor
	public ExplodeBuildEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		String name = e.getPlayer().getName().toLowerCase();
		
		if (explodeBuildRegistry.contains(name)) {
			e.setCancelled(true);
			Location loc = e.getBlock().getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0f, false, false);
			explodeBuildRegistry.setRegister(name, null);
		}
	}
}
