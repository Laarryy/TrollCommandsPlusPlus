package me.egg82.tcpp.events.inventory.inventoryMoveItem;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryMoveItemAttachErase implements Consumer<InventoryMoveItemEvent> {
    public InventoryMoveItemAttachErase() { }

    public void accept(InventoryMoveItemEvent event) {
        eraseLore(event.getItem());
    }

    private boolean eraseLore(ItemStack item) {
        if (item == null) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return false;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) {
            return false;
        }

        boolean retVal = lore.removeIf(v -> ChatColor.stripColor(v).trim().startsWith("Command to run: "));

        meta.setLore(lore);
        item.setItemMeta(meta);

        return retVal;
    }
}
