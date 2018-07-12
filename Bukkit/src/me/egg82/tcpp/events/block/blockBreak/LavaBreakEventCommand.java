package me.egg82.tcpp.events.block.blockBreak;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.registries.LavaBreakRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class LavaBreakEventCommand extends EventHandler<BlockBreakEvent> {
	//vars
	private IVariableRegistry<UUID> lavaBreakRegistry = ServiceLocator.getService(LavaBreakRegistry.class);
	
	//constructor
	public LavaBreakEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		if (lavaBreakRegistry.hasRegister(uuid)) {
			event.setCancelled(true);
			event.getBlock().setType(Material.LAVA);
			lavaBreakRegistry.removeRegister(uuid);
		}
	}
}
