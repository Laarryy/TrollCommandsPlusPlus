package me.egg82.tcpp.ticks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import me.egg82.tcpp.services.SpoilRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class SpoilTickCommand extends TickCommand {
	//vars
	private IRegistry spoilRegistry = (IRegistry) ServiceLocator.getService(SpoilRegistry.class);
	
	//constructor
	public SpoilTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = spoilRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) spoilRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.05) {
			PlayerInventory inventory = player.getInventory();
			ItemStack rottenFlesh = new ItemStack(Material.ROTTEN_FLESH, 1);
			
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
			
			for (int i = 0; i < filledSlots.size(); i++) {
				ItemStack items = inventory.getItem(filledSlots.get(i));
				Material type = items.getType();
				String typeString = type.name().toLowerCase();
				
				if (typeString.contains("raw") || typeString.contains("cooked") || typeString.contains("fish")) {
					placeItem(rottenFlesh, inventory);
				} else if (typeString.contains("apple")
				|| typeString.contains("potato")
				|| typeString.contains("beetroot")
				|| type == Material.BREAD
				|| type == Material.CAKE
				|| typeString.contains("carrot")
				|| typeString.contains("chorus")
				|| type == Material.COOKIE
				|| type == Material.PUMPKIN_PIE
				|| typeString.equals("rabbit_stew")) {
					// Do nothing, as we'll be removing the item below anyway
				} else {
					continue;
				}
				
				int amount = items.getAmount();
				if (amount == 1) {
					inventory.setItem(filledSlots.get(i), null);
				} else {
					items.setAmount(amount - 1);
				}
				
				break;
			}
		}
	}
	
	private boolean placeItem(ItemStack item, Inventory inventory) {
		int slot = -1;
		Map<Integer, ? extends ItemStack> slots = inventory.all(item.getType());
		
		for (Map.Entry<Integer, ? extends ItemStack> entry : slots.entrySet()) {
			int amount = entry.getValue().getAmount();
			if (amount - item.getAmount() <= item.getMaxStackSize()) {
				item.setAmount(item.getAmount() + amount);
				slot = entry.getKey();
				break;
			}
		}
		
		if (slot == -1) {
			slot = inventory.firstEmpty();
		}
		
		inventory.setItem(slot, item);
		return true;
	}
}
