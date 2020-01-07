package me.egg82.tcpp.utils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryUtil {
    private InventoryUtil() { }

    public static Inventory getClickedInventory(InventoryClickEvent event) {
        if (event.getRawSlot() < 0) {
            return null;
        } else if (event.getView().getTopInventory() != null && event.getRawSlot() < event.getView().getTopInventory().getSize()) {
            return event.getView().getTopInventory();
        } else {
            return event.getView().getBottomInventory();
        }
    }

    public static Inventory getClickedInventory(InventoryDragEvent event) {
        if (!event.getRawSlots().iterator().hasNext() || event.getRawSlots().iterator().next().intValue() < 0) {
            return null;
        } else if (event.getView().getTopInventory() != null && event.getRawSlots().iterator().next().intValue() < event.getView().getTopInventory().getSize()) {
            return event.getView().getTopInventory();
        } else {
            return event.getView().getBottomInventory();
        }
    }
}
