package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.nbt.INBTHelper;

public class AttachCommandEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachCommandEventCommand(BlockPlaceEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		ItemStack item = event.getItemInHand();
		
		if (!nbtHelper.hasTag(item, "tcppCommand")) {
			return;
		}
		if (!nbtHelper.supportsBlocks()) {
			event.getPlayer().sendMessage(ChatColor.YELLOW + "[Warning] The NBT library you've chosen does not support blocks.");
			event.getPlayer().sendMessage(ChatColor.YELLOW + "[Warning] The block you just placed will not have any commands attached to it.");
			return;
		}
		
		nbtHelper.addTag(event.getBlock(), "tcppSender", nbtHelper.getTag(item, "tcppSender"));
		nbtHelper.addTag(event.getBlock(), "tcppCommand", nbtHelper.getTag(item, "tcppCommand"));
	}
}
