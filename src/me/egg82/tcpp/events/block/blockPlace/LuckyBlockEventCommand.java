package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.services.registries.LuckyBlockRegistry;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LuckyBlockEventCommand extends EventCommand<BlockPlaceEvent> {
	//vars
	private IRegistry<Location> luckyBlockRegistry = ServiceLocator.getService(LuckyBlockRegistry.class);
	
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public LuckyBlockEventCommand(BlockPlaceEvent event) {
		super(event);
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		INBTCompound itemCompound = nbtHelper.getCompound(event.getItemInHand());
		
		if (!itemCompound.hasTag("tcppLucky")) {
			return;
		}
		
		event.getPlayer().sendMessage(ChatColor.YELLOW + "[Note] The lucky block you just placed will NOT be saved if the server is reloaded.");
		luckyBlockRegistry.setRegister(event.getBlock().getLocation(), itemCompound.getDouble("tcppLucky"));
	}
}
