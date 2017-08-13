package me.egg82.tcpp.util;

import org.bukkit.event.inventory.InventoryClickEvent;
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
	
	//private
	
}
