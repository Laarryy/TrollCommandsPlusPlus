package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.BludgerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class BludgerTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> bludgerRegistry = ServiceLocator.getService(BludgerRegistry.class);
	
	//constructor
	public BludgerTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = bludgerRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		Fireball fireball = bludgerRegistry.getRegister(uuid, Fireball.class);
		fireball.setVelocity(player.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.35d));
	}
}