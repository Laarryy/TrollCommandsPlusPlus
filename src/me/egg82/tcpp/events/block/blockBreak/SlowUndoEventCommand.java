package me.egg82.tcpp.events.block.blockBreak;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.registries.SlowUndoRegistry;
import ninja.egg82.bukkit.core.BlockData;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class SlowUndoEventCommand extends EventHandler<BlockBreakEvent> {
	//vars
	private IVariableRegistry<UUID> slowUndoRegistry = ServiceLocator.getService(SlowUndoRegistry.class);
	
	//constructor
	public SlowUndoEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (!slowUndoRegistry.hasRegister(player.getUniqueId())) {
			return;
		}
		
		// Save block state
		BlockData blockData = BlockUtil.getBlock(event.getBlock());
		
		// Wait 4-6 seconds
		TaskUtil.runSync(new Runnable() {
			public void run() {
				// "Undo" this event
				BlockUtil.setBlock(event.getBlock(), blockData, true);
			}
		}, MathUtil.fairRoundedRandom(80, 120));
	}
}
