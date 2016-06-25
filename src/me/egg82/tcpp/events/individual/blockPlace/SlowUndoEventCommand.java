package me.egg82.tcpp.events.individual.blockPlace;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;

import com.google.common.collect.ImmutableMap;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class SlowUndoEventCommand extends EventCommand {
	//vars
	private IRegistry slowUndoRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOW_UNDO_REGISTRY);
	private IRegistry slowUndoInternRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOW_UNDO_INTERN_REGISTRY);
	
	//constructor
	public SlowUndoEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void execute() {
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		String name = e.getPlayer().getName().toLowerCase();
		
		if (slowUndoRegistry.contains(name)) {
			ArrayList<ImmutableMap<String, Object>> list = (ArrayList<ImmutableMap<String, Object>>) slowUndoInternRegistry.getRegister(name);
			list.add(ImmutableMap.of("type", Material.AIR, "loc", e.getBlock().getLocation(), "mode", e.getPlayer().getGameMode()));
		}
	}
}
