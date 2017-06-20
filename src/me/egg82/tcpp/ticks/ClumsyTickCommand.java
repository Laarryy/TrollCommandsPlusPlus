package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.Collections;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.services.ClumsyRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class ClumsyTickCommand extends TickCommand {
	//vars
	private IRegistry clumsyRegistry = (IRegistry) ServiceLocator.getService(ClumsyRegistry.class);
	
	//constructor
	public ClumsyTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = clumsyRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) clumsyRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
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
			
			Collections.shuffle(filledSlots);
			
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
			
			player.getWorld().dropItemNaturally(BlockUtil.getTopAirBlock(LocationUtil.getLocationBehind(player.getLocation(), MathUtil.random(1.5d, 3.0d))), droppedItem);
		}
	}
}
