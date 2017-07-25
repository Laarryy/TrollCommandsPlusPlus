package me.egg82.tcpp.events.inventory.inventoryClick;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.egg82.tcpp.services.TrollInventoryRegistry;
import me.egg82.tcpp.services.TrollPageRegistry;
import me.egg82.tcpp.services.TrollPlayerRegistry;
import me.egg82.tcpp.services.TrollSearchRegistry;
import me.egg82.tcpp.util.GuiUtil;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class TrollEventCommand extends EventCommand<InventoryClickEvent> {
	//vars
	private IRegistry<UUID> trollInventoryRegistry = ServiceLocator.getService(TrollInventoryRegistry.class);
	private IRegistry<UUID> trollPlayerRegistry = ServiceLocator.getService(TrollPlayerRegistry.class);
	private IRegistry<UUID> trollPageRegistry = ServiceLocator.getService(TrollPageRegistry.class);
	private IRegistry<UUID> trollSearchRegistry = ServiceLocator.getService(TrollSearchRegistry.class);
	
	//constructor
	public TrollEventCommand(InventoryClickEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = (Player) event.getWhoClicked();
		UUID uuid = player.getUniqueId();
		
		if (!trollInventoryRegistry.hasRegister(uuid)) {
			return;
		}
		
		if (event.getClickedInventory() == null || event.getClickedInventory() != event.getInventory()) {
			event.setCancelled(true);
			return;
		}
		
		ItemStack item = event.getCurrentItem();
		if (item == null || item.getType() == Material.AIR) {
			return;
		}
		
		String name = item.getItemMeta().getDisplayName().toLowerCase();
		
		if (name.contains("previous")) {
			int newPage = trollPageRegistry.getRegister(uuid, Integer.class) - 1;
			Inventory inv = GuiUtil.createInventory(player,  trollSearchRegistry.getRegister(uuid, String.class), newPage);
			trollPageRegistry.setRegister(uuid, newPage);
			event.getInventory().setContents(inv.getContents());
		} else if (name.contains("next")) {
			int newPage = trollPageRegistry.getRegister(uuid, Integer.class) + 1;
			Inventory inv = GuiUtil.createInventory(player, trollSearchRegistry.getRegister(uuid, String.class), newPage);
			trollPageRegistry.setRegister(uuid, newPage);
			event.getInventory().setContents(inv.getContents());
		} else if (name.contains("close")) {
			player.closeInventory();
		} else {
			player.getServer().dispatchCommand(player, "troll " + name.substring(name.indexOf('/') + 1) + " " + trollPlayerRegistry.getRegister(uuid, String.class));
			player.closeInventory();
		}
		
		event.setCancelled(true);
	}
}
