package me.egg82.tcpp.commands.lucky;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.core.LuckyCommand;

public class GiveSwordCommand extends LuckyCommand {
    //vars

    //constructor
    public GiveSwordCommand() {
        super();
    }

    //public

    //private
    protected void onExecute(long elapsedMilliseconds) {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Lucky Sword");
        sword.setItemMeta(meta);

        sword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
        sword.addEnchantment(Enchantment.DURABILITY, 2);
        sword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 2);

        Map<Integer, ItemStack> floatingItems = player.getInventory().addItem(sword);
        for (Entry<Integer, ItemStack> item : floatingItems.entrySet()) {
            player.getWorld().dropItemNaturally(player.getLocation(), item.getValue());
        }
    }
}
