package me.egg82.tcpp.events.individual.blockBreak;

import java.util.ArrayList;

import org.bukkit.event.block.BlockBreakEvent;

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
		BlockBreakEvent e = (BlockBreakEvent) event;
		String uuid = e.getPlayer().getUniqueId().toString();
		
		if (slowUndoRegistry.contains(uuid)) {
			ArrayList<ImmutableMap<String, Object>> list = (ArrayList<ImmutableMap<String, Object>>) slowUndoInternRegistry.getRegister(uuid);
			list.add(ImmutableMap.of("type", e.getBlock().getType(), "loc", e.getBlock().getLocation(), "mode", e.getPlayer().getGameMode()));
		}
	}
}
