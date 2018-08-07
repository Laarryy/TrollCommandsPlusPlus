package me.egg82.tcpp.events.block.blockBreak;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.FlowerPot;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import me.egg82.tcpp.registries.LagBlockRegistry;
import me.egg82.tcpp.registries.LagRegistry;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.bukkit.utils.TaskUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;
import ninja.egg82.utils.MathUtil;

public class LagEventCommand extends EventHandler<BlockBreakEvent> {
	//vars
	private IVariableRegistry<UUID> lagRegistry = ServiceLocator.getService(LagRegistry.class);
	private IVariableRegistry<Location> lagBlockRegistry = ServiceLocator.getService(LagBlockRegistry.class);
	
	private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public LagEventCommand() {
		super();
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
		
		// Block is currently being lagged. Nobody should interact with it.
		if (lagBlockRegistry.hasRegister(blockLocation)) {
			event.setCancelled(true);
			return;
		}
		if (!lagRegistry.hasRegister(player.getUniqueId())) {
			return;
		}
		
		lagBlockRegistry.setRegister(blockLocation, null);
		
		// Capture the current state of everything
		BlockState blockState = block.getState();
		GameMode gameMode = player.getGameMode();
		ItemStack tool = entityHelper.getItemInMainHand(player);
		
		event.setCancelled(true);
		
		// Manually doing the event after a random interval
		TaskUtil.runSync(new Runnable() {
			public void run() {
				// Break the block using the captured state
				breakBlock(blockState, blockLocation, gameMode, tool);
				lagBlockRegistry.removeRegister(blockLocation);
			}
		}, MathUtil.fairRoundedRandom(15, 30));
	}
	
	public static void breakBlock(BlockState oldState, Location location, GameMode gameMode, ItemStack tool) {
		if (gameMode != GameMode.CREATIVE) {
			ItemStack[] droppedItems = null;
			
			if (oldState instanceof InventoryHolder) {
				droppedItems = ((InventoryHolder) oldState).getInventory().getContents();
			} else if (oldState instanceof FlowerPot) {
				MaterialData contents = ((FlowerPot) oldState).getContents();
				if (contents != null) {
					droppedItems = new ItemStack[] { contents.toItemStack(1) };
				}
			} else if (oldState instanceof Jukebox) {
				Material disk = ((Jukebox) oldState).getPlaying();
				if (disk != null) {
					droppedItems = new ItemStack[] { new ItemStack(disk, 1) };
				}
			}
			
			if (droppedItems != null) {
				for (ItemStack i : droppedItems) {
					if (i == null) {
						continue;
					}
					location.getWorld().dropItemNaturally(location, i);
				}
			}
			
			location.getBlock().breakNaturally(tool);
		} else {
			location.getBlock().setType(Material.AIR, true);
		}
	}
}
