package me.egg82.tcpp.events.individual.blockBreakEvent;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class DisplayEventCommand extends EventCommand {
	//vars
	private IRegistry displayInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.DISPLAY_INTERN_REGISTRY);
	
	//constructor
	public DisplayEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		BlockBreakEvent e = (BlockBreakEvent) event;
		String[] names = displayInternRegistry.registryNames();
		
		for (String name : names) {
			HashMap<String, Object> map = (HashMap<String, Object>) displayInternRegistry.getRegister(name);
			Location loc = (Location) map.get("loc");
			Location loc2 = e.getBlock().getLocation();
			
			int x1 = loc.getBlockX();
			int y1 = loc.getBlockY();
			int z1 = loc.getBlockZ();
			int x2 = loc2.getBlockX();
			int y2 = loc2.getBlockY();
			int z2 = loc2.getBlockZ();
			
			if (
					x2 >= x1 - 1 &&
					x2 <= x1 + 1 &&
					y2 >= y1 - 1 &&
					y2 <= y1 + 2 &&
					z2 >= z1 - 1 &&
					z2 <= z1 + 1
					) {
				e.setCancelled(true);
			}
		}
	}
}
