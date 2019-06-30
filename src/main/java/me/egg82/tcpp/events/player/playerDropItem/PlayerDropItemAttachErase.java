package me.egg82.tcpp.events.player.playerDropItem;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerDropItemAttachErase implements Consumer<PlayerDropItemEvent> {
    public PlayerDropItemAttachErase() { }

    public void accept(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (eraseLore(item)) {
            event.getItemDrop().setItemStack(item);
        }
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
