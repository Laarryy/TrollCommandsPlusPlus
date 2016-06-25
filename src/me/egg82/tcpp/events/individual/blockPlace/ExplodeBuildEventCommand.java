package me.egg82.tcpp.events.individual.blockPlace;

import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class ExplodeBuildEventCommand extends EventCommand {
	//vars
	private IRegistry explodeBuildRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.EXPLODE_BUILD_REGISTRY);
	
	//constructor
	public ExplodeBuildEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		String name = e.getPlayer().getName().toLowerCase();
		
		explodeBuildRegistry.computeIfPresent(name, (k,v) -> {
			e.setCancelled(true);
			Location loc = e.getBlock().getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0f, false, false);
			
			return null;
		});
	}
}
