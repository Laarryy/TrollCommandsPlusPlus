package me.egg82.tcpp.events.block.blockPlace;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.block.BlockPlaceEvent;

import me.egg82.tcpp.registries.LuckyBlockRegistry;
import ninja.egg82.nbt.core.INBTCompound;
import ninja.egg82.nbt.reflection.INBTHelper;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class LuckyBlockEventCommand extends EventHandler<BlockPlaceEvent> {
	//vars
	private IVariableRegistry<Location> luckyBlockRegistry = ServiceLocator.getService(LuckyBlockRegistry.class);
	
	private INBTHelper nbtHelper = ServiceLocator.getService(INBTHelper.class);
	
	//constructor
	public LuckyBlockEventCommand() {
		super();
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
