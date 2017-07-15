package me.egg82.tcpp.events.block.blockBreak;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.LagBlockRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.player.IPlayerHelper;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private IRegistry lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagBlockRegistry = ServiceLocator.getService(LagBlockRegistry.class);
	
	private IPlayerHelper playerUtil = ServiceLocator.getService(IPlayerHelper.class);
	
	//constructor
	public LagEventCommand(BlockBreakEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		Block block = event.getBlock();
		Location blockLocation = block.getLocation();
		String locationString = blockLocation.getWorld() + "," + blockLocation.getX() + "," + blockLocation.getY() + "," + blockLocation.getZ();
		
		// Block is currently being lagged. Nobody should interact with it.
		if (lagBlockRegistry.hasRegister(locationString)) {
			event.setCancelled(true);
			return;
		}
		if (!lagRegistry.hasRegister(player.getUniqueId().toString())) {
			return;
		}
		
		lagBlockRegistry.setRegister(locationString, Location.class, blockLocation);
		
		// Capture the current state of everything
		BlockState blockState = block.getState();
		GameMode gameMode = player.getGameMode();
		ItemStack tool = playerUtil.getItemInMainHand(player);
		
		event.setCancelled(true);
		
		// Manually doing the event after a random interval
		TaskUtil.runSync(new Runnable() {
			public void run() {
				// Break the block using the captured state
				BlockUtil.breakNaturally(blockState, blockLocation, gameMode, tool, true);
				lagBlockRegistry.setRegister(locationString, Location.class, null);
			}
		}, MathUtil.fairRoundedRandom(15, 30));
	}
}
