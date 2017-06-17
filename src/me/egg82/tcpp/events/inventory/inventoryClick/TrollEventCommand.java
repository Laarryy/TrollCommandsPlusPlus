package me.egg82.tcpp.events.inventory.inventoryClick;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
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

public class TrollEventCommand extends EventCommand {
	//vars
	private IRegistry trollInventoryRegistry = (IRegistry) ServiceLocator.getService(TrollInventoryRegistry.class);
	private IRegistry trollPlayerRegistry = (IRegistry) ServiceLocator.getService(TrollPlayerRegistry.class);
	private IRegistry trollPageRegistry = (IRegistry) ServiceLocator.getService(TrollPageRegistry.class);
	private IRegistry trollSearchRegistry = (IRegistry) ServiceLocator.getService(TrollSearchRegistry.class);
	
	//constructor
	public TrollEventCommand(Event event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		InventoryClickEvent e = (InventoryClickEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = (Player) e.getWhoClicked();
		String uuid = player.getUniqueId().toString();
		
		if (!trollInventoryRegistry.hasRegister(uuid)) {
			return;
		}
		
		if (e.getClickedInventory() == null || e.getClickedInventory() != e.getInventory()) {
			e.setCancelled(true);
			return;
		}
		
		ItemStack item = e.getCurrentItem();
		if (item == null || item.getType() == Material.AIR) {
			return;
		}
		
		String name = item.getItemMeta().getDisplayName().toLowerCase();
		
		if (name.contains("previous")) {
			int newPage = ((Integer) trollPageRegistry.getRegister(uuid)) - 1;
			Inventory inv = GuiUtil.createInventory(player, (String) trollSearchRegistry.getRegister(uuid), newPage);
			trollPageRegistry.setRegister(uuid, Integer.class, newPage);
			e.getInventory().setContents(inv.getContents());
		} else if (name.contains("next")) {
			int newPage = ((Integer) trollPageRegistry.getRegister(uuid)) + 1;
			Inventory inv = GuiUtil.createInventory(player, (String) trollSearchRegistry.getRegister(uuid), newPage);
			trollPageRegistry.setRegister(uuid, Integer.class, newPage);
			e.getInventory().setContents(inv.getContents());
		} else if (name.contains("close")) {
			player.closeInventory();
		} else {
			player.getServer().dispatchCommand(player, name.substring(name.indexOf('/') + 1) + " " + ((Player) trollPlayerRegistry.getRegister(uuid)).getName());
			player.closeInventory();
		}
		
		e.setCancelled(true);
	}
}
