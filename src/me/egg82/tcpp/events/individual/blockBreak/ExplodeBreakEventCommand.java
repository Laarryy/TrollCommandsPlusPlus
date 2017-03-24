package me.egg82.tcpp.events.individual.blockBreak;

import org.bukkit.Location;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class ExplodeBreakEventCommand extends EventCommand {
	//vars
	private IRegistry explodeBreakRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.EXPLODE_BREAK_REGISTRY);
	
	//constructor
	public ExplodeBreakEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		BlockBreakEvent e = (BlockBreakEvent) event;
		String uuid = e.getPlayer().getUniqueId().toString();
		
		explodeBreakRegistry.computeIfPresent(uuid, (k,v) -> {
			e.setCancelled(true);
			Location loc = e.getBlock().getLocation();
			loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), 4.0f, false, false);
			
			return null;
		});
	}
}
