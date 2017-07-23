package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.RewindRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class RewindTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> rewindRegistry = ServiceLocator.getService(RewindRegistry.class);
	
	//constructor
	public RewindTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = rewindRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		player.setPlayerTime(player.getPlayerTime() - 100L, false);
	}
}
