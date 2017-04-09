package me.egg82.tcpp.events.block.blockBreak;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.egg82.tcpp.services.SlowUndoRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.startup.InitRegistry;
import ninja.egg82.utils.MathUtil;

public class SlowUndoEventCommand extends EventCommand {
	//vars
	private IRegistry slowUndoRegistry = (IRegistry) ServiceLocator.getService(SlowUndoRegistry.class);
	private IRegistry initRegistry = (IRegistry) ServiceLocator.getService(InitRegistry.class);
	
	//constructor
	public SlowUndoEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		BlockBreakEvent e = (BlockBreakEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (!slowUndoRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		// Save block state
		Location blockLocation = e.getBlock().getLocation();
		BlockData blockData = BlockUtil.getBlock(e.getBlock());
		
		// Wait 4-6 seconds
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask((JavaPlugin) initRegistry.getRegister("plugin"), new Runnable() {
			public void run() {
				// "Undo" this event
				BlockUtil.setBlock(blockLocation, blockData);
			}
		}, MathUtil.fairRoundedRandom(80, 120));
	}
}
