package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.SpinRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;

public class SpinTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> spinRegistry = ServiceLocator.getService(SpinRegistry.class);
	
	//constructor
	public SpinTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = spinRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		Location newLocation = player.getLocation().clone();
		float newYaw = newLocation.getYaw() + spinRegistry.getRegister(uuid, Float.class);
		
		while (newYaw < 0.0f) {
			newYaw += 360.0f;
		}
		while (newYaw >= 360.0f) {
			newYaw -= 360.0f;
		}
		
		newLocation.setYaw(newYaw);
		player.teleport(newLocation);
	}
}
