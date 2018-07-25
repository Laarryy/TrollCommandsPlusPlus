package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.registries.ClumsyRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.BlockUtil;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.bukkit.utils.LocationUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class ClumsyTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> clumsyRegistry = ServiceLocator.getService(ClumsyRegistry.class);
	
	//constructor
	public ClumsyTickCommand() {
		super(0L, 10L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : clumsyRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		if (Math.random() <= 0.1) {
			PlayerInventory inventory = player.getInventory();
			ArrayList<Integer> filledSlots = new ArrayList<Integer>();
			
			for (int i = 0; i < inventory.getSize(); i++) {
				ItemStack slot = inventory.getItem(i);
				if (slot != null && slot.getType() != Material.AIR) {
					filledSlots.add(i);
				}
			}
			
			if (filledSlots.size() == 0) {
				return;
			}
			
			int droppedItemSlot = filledSlots.get(MathUtil.fairRoundedRandom(0, filledSlots.size() - 1));
			ItemStack items = inventory.getItem(droppedItemSlot);
			
			ItemStack droppedItem = new ItemStack(items);
			droppedItem.setAmount(1);
			
			int itemsAmount = items.getAmount();
			
			if (itemsAmount == 1) {
				inventory.setItem(droppedItemSlot, null);
			} else {
				items.setAmount(itemsAmount - 1);
			}
			
			player.getWorld().dropItemNaturally(BlockUtil.getHighestSolidBlock(LocationUtil.getLocationBehind(player.getLocation(), MathUtil.random(1.5d, 3.0d))).add(0.0d, 1.0d, 0.0d), droppedItem);
		}
	}
}
