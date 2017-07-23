package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.StarveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class StarveTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> starveRegistry = ServiceLocator.getService(StarveRegistry.class);
	
	//constructor
	public StarveTickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = starveRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		player.setFoodLevel(player.getFoodLevel() - 1);
	}
}
