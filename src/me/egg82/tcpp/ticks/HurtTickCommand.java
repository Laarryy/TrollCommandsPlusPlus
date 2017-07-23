package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.HurtRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class HurtTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> hurtRegistry = ServiceLocator.getService(HurtRegistry.class);
	
	//constructor
	public HurtTickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = hurtRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		player.damage(1.0d);
	}
}
