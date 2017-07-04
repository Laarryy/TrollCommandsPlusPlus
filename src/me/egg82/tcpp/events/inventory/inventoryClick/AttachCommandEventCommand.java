package me.egg82.tcpp.events.inventory.inventoryClick;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;

public class AttachCommandEventCommand extends EventCommand {
	//vars
	private INBTHelper nbtHelper = (INBTHelper) ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachCommandEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		InventoryClickEvent e = (InventoryClickEvent) event;
		
		if (e.isCancelled()) {
			System.out.println("Cancelled");
			return;
		}
		if (e.getClickedInventory() == null) {
			System.out.println("Clicked inventory == null");
			return;
		}
		InventoryHolder holder = e.getClickedInventory().getHolder();
		if (holder == e.getWhoClicked()) {
			System.out.println("Issuer == Destination");
			return;
		}
		if (e.getAction() == InventoryAction.CLONE_STACK
			|| e.getAction() == InventoryAction.COLLECT_TO_CURSOR
			|| e.getAction() == InventoryAction.NOTHING
			|| e.getAction() == InventoryAction.PICKUP_ALL
			|| e.getAction() == InventoryAction.PICKUP_HALF
			|| e.getAction() == InventoryAction.PICKUP_ONE
			|| e.getAction() == InventoryAction.PICKUP_SOME
			|| e.getAction() == InventoryAction.UNKNOWN
		) {
			System.out.println("Action != Place");
			return;
		}
		
		ItemStack item = e.getCursor();
		if (!nbtHelper.hasTag(item, "tcppCommand")) {
			System.out.println("No NBT Tag");
			return;
		}
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(null);
		item.setItemMeta(meta);
		
		if (holder instanceof Player) {
			System.out.println("Running command");
			Bukkit.dispatchCommand((Player) holder, (String) nbtHelper.getTag(item, "tcppCommand"));
			nbtHelper.removeTag(item, "tcppCommand");
		} else {
			System.out.println("Holder != Player");
		}
	}
}
