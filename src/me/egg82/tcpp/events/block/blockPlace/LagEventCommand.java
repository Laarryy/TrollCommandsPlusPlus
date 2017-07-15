package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.LagBlockRegistry;
import me.egg82.tcpp.services.LagRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.BlockData;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.TaskUtil;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IRegistry lagBlockRegistry = ServiceLocator.getService(LagBlockRegistry.class);
	
	//constructor
	public LagEventCommand(BlockPlaceEvent event) {
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
		BlockState blockState = event.getBlock().getState();
		Material blockType = blockState.getType();
		ItemStack hand = event.getItemInHand();
		GameMode gameMode = player.getGameMode();
		
		event.setCancelled(true);
		
		// Manually doing the event after 1.5-2.5 seconds
		TaskUtil.runSync(new Runnable() {
			public void run() {
				if (gameMode != GameMode.CREATIVE) {
					// Set the inventory
					int handAmount = hand.getAmount();
					
					if (handAmount <= 1) {
						hand.setAmount(0);
					} else {
						hand.setAmount(handAmount - 1);
					}
				}
				
				// Break the block using the captured state
				BlockUtil.setBlock(block, new BlockData(null, blockState, blockType), true);
				lagBlockRegistry.setRegister(locationString, Location.class, null);
			}
		}, MathUtil.fairRoundedRandom(30, 50));
	}
}
