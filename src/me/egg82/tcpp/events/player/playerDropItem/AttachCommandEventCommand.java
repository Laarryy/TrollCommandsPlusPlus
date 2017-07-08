package me.egg82.tcpp.events.player.playerDropItem;

import java.util.ArrayList;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerDropItemEvent;
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
		PlayerDropItemEvent e = (PlayerDropItemEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		if (!nbtHelper.hasTag(e.getItemDrop().getItemStack(), "tcppCommand")) {
			return;
		}
		
		ItemMeta meta = e.getItemDrop().getItemStack().getItemMeta();
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
		e.getItemDrop().getItemStack().setItemMeta(meta);
	}
}
