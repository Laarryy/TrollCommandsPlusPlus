package me.egg82.tcpp.util;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryUtil {
	//vars
	
	//constructor
	public InventoryUtil() {
		
	}
	
	//public
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
		if (event.getRawSlots().iterator().next() < 0) {
			return null;
		} else if (event.getView().getTopInventory() != null && event.getRawSlots().iterator().next() < event.getView().getTopInventory().getSize()) {
			return event.getView().getTopInventory();
		} else {
			return event.getView().getBottomInventory();
		}
	}
	
	//private
	
}
