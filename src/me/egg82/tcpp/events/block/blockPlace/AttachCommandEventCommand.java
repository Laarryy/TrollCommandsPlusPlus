package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockPlaceEvent;

import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.core.nbt.INBTCompound;
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
		
		INBTCompound itemCompound = nbtHelper.getCompound(event.getItemInHand());
		
		if (!itemCompound.hasTag("tcppCommand")) {
			return;
		}
		if (!nbtHelper.supportsBlocks()) {
			event.getPlayer().sendMessage(ChatColor.YELLOW + "[Warning] The NBT library you've chosen does not support blocks.");
			event.getPlayer().sendMessage(ChatColor.YELLOW + "[Warning] The block you just placed will not have any commands attached to it.");
			return;
		}
		
		INBTCompound blockCompound = nbtHelper.getCompound(event.getBlock());
		blockCompound.setString("tcppSender", itemCompound.getString("tcppSender"));
		blockCompound.setString("tcppCommand", itemCompound.getString("tcppCommand"));
	}
}
