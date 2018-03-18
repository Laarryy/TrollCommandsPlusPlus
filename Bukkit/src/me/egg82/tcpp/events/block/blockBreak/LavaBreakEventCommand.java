package me.egg82.tcpp.events.block.blockBreak;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.services.registries.LavaBreakRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LavaBreakEventCommand extends EventCommand<BlockBreakEvent> {
	//vars
	private IRegistry<UUID> lavaBreakRegistry = ServiceLocator.getService(LavaBreakRegistry.class);
	
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
