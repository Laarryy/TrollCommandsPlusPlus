package me.egg82.tcpp.events.inventory.inventoryClick;

import java.util.List;
import java.util.function.Consumer;
import me.egg82.tcpp.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class InventoryClickAttachErase implements Consumer<InventoryClickEvent> {
    private final Plugin plugin;

    public InventoryClickAttachErase(Plugin plugin) {
        this.plugin = plugin;
    }

    public void accept(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            if (eraseLore(event.getCurrentItem())) {
                Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
            }
        } else if (
                event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD
                || event.getAction() == InventoryAction.HOTBAR_SWAP
                || event.getAction() == InventoryAction.PLACE_ALL
                || event.getAction() == InventoryAction.PLACE_ONE
                || event.getAction() == InventoryAction.PLACE_SOME
                || event.getAction() == InventoryAction.SWAP_WITH_CURSOR
        ) {
            if (InventoryUtil.getClickedInventory(event) == event.getView().getTopInventory()) {
                if (eraseLore(event.getCursor())) {
                    Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
                }
            }
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

        if (retVal) {
            meta.setLore(lore);
            item.setItemMeta(meta);
        }

        return retVal;
    }
}
