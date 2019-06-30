package me.egg82.tcpp.events.inventory.inventoryDrag;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import me.egg82.tcpp.utils.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class InventoryDragAttachErase implements Consumer<InventoryDragEvent> {
    private final Plugin plugin;

    public InventoryDragAttachErase(Plugin plugin) {
        this.plugin = plugin;
    }

    public void accept(InventoryDragEvent event) {
        InventoryAction action;
        Inventory clicked = InventoryUtil.getClickedInventory(event);

        if (clicked == event.getView().getTopInventory()) {
            action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
        } else {
            action = (event.getCursor() == null || event.getCursor().getAmount() == 0) ? InventoryAction.PLACE_ALL : InventoryAction.PLACE_SOME;
        }

        if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            boolean erased = false;
            for (Map.Entry<Integer, ItemStack> kvp : event.getNewItems().entrySet()) {
                if (eraseLore(kvp.getValue())) {
                    erased = true;
                }
            }
            if (erased) {
                Bukkit.getScheduler().runTaskLater(plugin, ((Player) event.getWhoClicked())::updateInventory, 1L);
            }
        } else if (
                action == InventoryAction.HOTBAR_MOVE_AND_READD
                || action == InventoryAction.HOTBAR_SWAP
                || action == InventoryAction.PLACE_ALL
                || action == InventoryAction.PLACE_ONE
                || action == InventoryAction.PLACE_SOME
                || action == InventoryAction.SWAP_WITH_CURSOR
        ) {
            if (clicked == event.getView().getTopInventory()) {
                boolean erased = false;
                for (Map.Entry<Integer, ItemStack> kvp : event.getNewItems().entrySet()) {
                    if (eraseLore(kvp.getValue())) {
                        erased = true;
                    }
                }
                if (erased) {
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

        meta.setLore(lore);
        item.setItemMeta(meta);

        return retVal;
    }
}
