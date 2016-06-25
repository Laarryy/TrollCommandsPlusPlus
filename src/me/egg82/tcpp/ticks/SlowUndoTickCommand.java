package me.egg82.tcpp.ticks;

import java.util.ArrayList;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class SlowUndoTickCommand extends TickCommand {
	//vars
	private IRegistry reg2 = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOW_UNDO_INTERN_REGISTRY);
	
	//constructor
	public SlowUndoTickCommand() {
		super();
		ticks = 20l;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		String[] names = reg2.registryNames();
		for (String name : names) {
			e((ArrayList<ImmutableMap<String, Object>>) reg2.getRegister(name));
		}
	}
	private void e(ArrayList<ImmutableMap<String, Object>> maps) {
		if (maps.size() > 0 && Math.random() <= 0.5d) {
			ImmutableMap<String, Object> map = maps.get(0);
			
			Location loc = (Location) map.get("loc");
			if ((GameMode) map.get("mode") == GameMode.CREATIVE) {
				loc.getBlock().setType((Material) map.get("type"));
			} else {
				if (loc.getBlock().getType() != Material.AIR) {
					loc.getBlock().breakNaturally();
				}
				loc.getBlock().setType((Material) map.get("type"));
			}
			
			maps.remove(0);
		}
	}
}
