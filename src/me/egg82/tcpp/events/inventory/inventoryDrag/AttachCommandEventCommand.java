package me.egg82.tcpp.events.inventory.inventoryDrag;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.egg82.tcpp.util.InventoryUtil;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.plugin.utils.TaskUtil;

public class AttachCommandEventCommand extends EventCommand<InventoryDragEvent> {
	//vars
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachCommandEventCommand(InventoryDragEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack item = null;
		INBTCompound itemCompound = null;
		InventoryAction action = null;
		
		Inventory bottom = event.getView().getBottomInventory();
		Inventory top = event.getView().getTopInventory();
		Inventory clicked = InventoryUtil.getClickedInventory(event);
		
		if (clicked.equals(top)) {
			action = InventoryAction.MOVE_TO_OTHER_INVENTORY;
		} else {
			action = (event.getCursor() == null || event.getCursor().getAmount() == 0) ? InventoryAction.PLACE_ALL :InventoryAction.PLACE_SOME;
		}
		
		if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
			item = event.getOldCursor();
			itemCompound = nbtHelper.getCompound(item);
			
			if (!itemCompound.hasTag("tcppCommand")) {
				return;
			}
			
			if (clicked == top) {
				InventoryHolder holder = bottom.getHolder();
				if (holder instanceof Player) {
					Player sender = CommandUtil.getPlayerByUuid(itemCompound.getString("tcppSender"));
					if (sender != null) {
						CommandUtil.dispatchCommandAtPlayerLocation(sender, (Player) holder, itemCompound.getString("tcppCommand"));
					} else {
						if (CommandUtil.getOfflinePlayerByUuid(itemCompound.getString("tcppSender")).isOp()) {
							CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), (Player) holder, itemCompound.getString("tcppCommand"));
						} else {
							Bukkit.dispatchCommand((Player) holder, itemCompound.getString("tcppCommand"));
						}
					}
					
					itemCompound.removeTag("tcppSender");
					itemCompound.removeTag("tcppCommand");
				}
			} else {
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>(meta.getLore());
				int removeLine = -1;
				for (int i = 0; i < lore.size(); i++) {
					if (lore.get(i).contains("Command to run:")) {
						removeLine = i;
						break;
					}
				}
				if (removeLine > -1) {
					lore.remove(removeLine);
					lore.remove(removeLine);
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				update((Player) event.getWhoClicked());
				
				InventoryHolder holder = top.getHolder();
				if (holder instanceof Player) {
					Player sender = CommandUtil.getPlayerByUuid(itemCompound.getString("tcppSender"));
					if (sender != null) {
						CommandUtil.dispatchCommandAtPlayerLocation(sender, (Player) holder, itemCompound.getString("tcppCommand"));
					} else {
						if (CommandUtil.getOfflinePlayerByUuid(itemCompound.getString("tcppSender")).isOp()) {
							CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), (Player) holder, itemCompound.getString("tcppCommand"));
						} else {
							Bukkit.dispatchCommand((Player) holder, itemCompound.getString("tcppCommand"));
						}
					}
					
					itemCompound.removeTag("tcppSender");
					itemCompound.removeTag("tcppCommand");
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
			item = event.getCursor();
			itemCompound = nbtHelper.getCompound(item);
			
			if (!itemCompound.hasTag("tcppCommand")) {
				return;
			}
			
			if (clicked == top) {
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> lore = new ArrayList<String>(meta.getLore());
				int removeLine = -1;
				for (int i = 0; i < lore.size(); i++) {
					if (lore.get(i).contains("Command to run:")) {
						removeLine = i;
						break;
					}
				}
				if (removeLine > -1) {
					lore.remove(removeLine);
					lore.remove(removeLine);
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				update((Player) event.getWhoClicked());
				
				InventoryHolder holder = top.getHolder();
				if (holder instanceof Player) {
					Player sender = CommandUtil.getPlayerByUuid(itemCompound.getString("tcppSender"));
					if (sender != null) {
						CommandUtil.dispatchCommandAtPlayerLocation(sender, (Player) holder, itemCompound.getString("tcppCommand"));
					} else {
						if (CommandUtil.getOfflinePlayerByUuid(itemCompound.getString("tcppSender")).isOp()) {
							CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), (Player) holder, itemCompound.getString("tcppCommand"));
						} else {
							Bukkit.dispatchCommand((Player) holder, itemCompound.getString("tcppCommand"));
						}
					}
					
					itemCompound.removeTag("tcppSender");
					itemCompound.removeTag("tcppCommand");
				}
			} else {
				if (item.getItemMeta().getLore() == null) {
					InventoryHolder holder = bottom.getHolder();
					if (holder instanceof Player) {
						Player sender = CommandUtil.getPlayerByUuid(itemCompound.getString("tcppSender"));
						if (sender != null) {
							CommandUtil.dispatchCommandAtPlayerLocation(sender, (Player) holder, itemCompound.getString("tcppCommand"));
						} else {
							if (CommandUtil.getOfflinePlayerByUuid(itemCompound.getString("tcppSender")).isOp()) {
								CommandUtil.dispatchCommandAtPlayerLocation(Bukkit.getConsoleSender(), (Player) holder, itemCompound.getString("tcppCommand"));
							} else {
								Bukkit.dispatchCommand((Player) holder, itemCompound.getString("tcppCommand"));
							}
						}
						
						itemCompound.removeTag("tcppSender");
						itemCompound.removeTag("tcppCommand");
					}
				}
			}
		} else {
			return;
		}
	}
	
	@SuppressWarnings("deprecation")
	private void update(Player player) {
		TaskUtil.runSync(new Runnable() {
			public void run() {
				player.updateInventory();
			}
		}, 1L);
	}
}
