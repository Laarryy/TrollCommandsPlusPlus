package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

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
		BlockPlaceEvent e = (BlockPlaceEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		ItemStack item = e.getItemInHand();
		
		if (!nbtHelper.hasTag(item, "tcppCommand")) {
			return;
		}
		if (!nbtHelper.supportsBlocks()) {
			e.getPlayer().sendMessage(ChatColor.YELLOW + "[Warning] The NBT library you've chosen does not support blocks.");
			e.getPlayer().sendMessage(ChatColor.YELLOW + "[Warning] The block you just placed will not have any commands attached to it.");
			return;
		}
		
		nbtHelper.addTag(e.getBlock(), "tcppCommand", nbtHelper.getTag(item, "tcppCommand"));
	}
}
