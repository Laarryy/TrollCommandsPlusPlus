package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.event.block.BlockPlaceEvent;

import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class AttachEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public AttachEventCommand() {
		super();
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
		
		INBTCompound blockCompound = nbtHelper.getCompound(event.getBlock());
		blockCompound.setString("tcppSender", itemCompound.getString("tcppSender"));
		blockCompound.setString("tcppCommand", itemCompound.getString("tcppCommand"));
	}
}
