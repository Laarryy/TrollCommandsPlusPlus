package me.egg82.tcpp.commands.lucky;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.core.LuckyCommand;

public class GiveArmorCommand extends LuckyCommand {
	//vars
	
	//constructor
	public GiveArmorCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
		ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
		
		ItemMeta meta = chest.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Lucky Chest");
		chest.setItemMeta(meta);
		
		meta = boots.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD + "Lucky Boots");
		boots.setItemMeta(meta);
		
		chest.addEnchantment(Enchantment.DURABILITY, 2);
		chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
		boots.addEnchantment(Enchantment.DURABILITY, 2);
		boots.addEnchantment(Enchantment.PROTECTION_FALL, 3);
		
		Map<Integer, ItemStack> floatingItems = player.getInventory().addItem(chest, boots);
		for (Entry<Integer, ItemStack> item : floatingItems.entrySet()) {
			player.getWorld().dropItemNaturally(player.getLocation(), item.getValue());
		}
	}
}
