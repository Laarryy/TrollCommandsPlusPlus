package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.BurnRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class BurnTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> burnRegistry = ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnTickCommand() {
		super();
		ticks = 40L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = burnRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		player.setFireTicks(50);
	}
}
