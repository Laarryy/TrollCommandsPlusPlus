package me.egg82.tcpp.events.inventory.inventoryClick;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;
import ninja.egg82.startup.InitRegistry;

public class AttachCommandEventCommand extends EventCommand {
	//vars
	private INBTHelper nbtHelper = (INBTHelper) ServiceLocator.getService(INBTHelper.class);
	
	private JavaPlugin plugin = (JavaPlugin) ((IRegistry) ServiceLocator.getService(InitRegistry.class)).getRegister("plugin");
	
	//constructor
	public AttachCommandEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		InventoryClickEvent e = (InventoryClickEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		ItemStack item = null;
		InventoryAction action = e.getAction();
		
		Inventory bottom = e.getView().getBottomInventory();
		Inventory top = e.getView().getTopInventory();
		Inventory clicked = e.getClickedInventory();
		
		if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			item = e.getCurrentItem();
			
			if (!nbtHelper.hasTag(item, "tcppCommand")) {
				return;
			}
			
			if (clicked == top) {
				InventoryHolder holder = bottom.getHolder();
				if (holder instanceof Player) {
					Bukkit.dispatchCommand((Player) holder, (String) nbtHelper.getTag(item, "tcppCommand"));
					nbtHelper.removeTag(item, "tcppCommand");
				}
			} else {
				ItemMeta meta = item.getItemMeta();
				meta.setLore(null);
				item.setItemMeta(meta);
				
				update((Player) e.getWhoClicked());
				
				InventoryHolder holder = top.getHolder();
				if (holder instanceof Player) {
					Bukkit.dispatchCommand((Player) holder, (String) nbtHelper.getTag(item, "tcppCommand"));
					nbtHelper.removeTag(item, "tcppCommand");
				}
			}
		} else if (
			action == InventoryAction.HOTBAR_MOVE_AND_READD
			|| action == InventoryAction.HOTBAR_SWAP
			|| action == InventoryAction.PLACE_ALL
			|| action == InventoryAction.PLACE_ONE
			|| action == InventoryAction.PLACE_SOME
			|| action == InventoryAction.SWAP_WITH_CURSOR
		) {
			item = e.getCursor();
			
			if (!nbtHelper.hasTag(item, "tcppCommand")) {
				return;
			}
			
			if (clicked == top) {
				ItemMeta meta = item.getItemMeta();
				meta.setLore(null);
				item.setItemMeta(meta);
				
				update((Player) e.getWhoClicked());
				
				InventoryHolder holder = top.getHolder();
				if (holder instanceof Player) {
					Bukkit.dispatchCommand((Player) holder, (String) nbtHelper.getTag(item, "tcppCommand"));
					nbtHelper.removeTag(item, "tcppCommand");
				}
			} else {
				if (item.getItemMeta().getLore() == null) {
					InventoryHolder holder = bottom.getHolder();
					if (holder instanceof Player) {
						Bukkit.dispatchCommand((Player) holder, (String) nbtHelper.getTag(item, "tcppCommand"));
						nbtHelper.removeTag(item, "tcppCommand");
					}
				}
			}
		} else {
			return;
		}
	}
	
	private void update(Player player) {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				player.updateInventory();
			}
		}, 1L);
	}
}
