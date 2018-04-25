package me.egg82.tcpp.events.player.playerDropItem;

import java.util.ArrayList;

import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.events.EventCommand;

public class AttachCommandEventCommand extends EventCommand<PlayerDropItemEvent> {
	//vars
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachCommandEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack item = event.getItemDrop().getItemStack();
		
		if (!nbtHelper.getCompound(item).hasTag("tcppCommand")) {
			return;
		}
		
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
	}
}
