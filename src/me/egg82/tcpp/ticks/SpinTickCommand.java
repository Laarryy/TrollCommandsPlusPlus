package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.registries.SpinRegistry;
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
		for (UUID key : spinRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), spinRegistry.getRegister(key, Float.class));
		}
	}
	private void e(Player player, float speed) {
		if (player == null) {
			return;
		}
		
		Location newLocation = player.getLocation().clone();
		float newYaw = newLocation.getYaw() + speed;
		
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
