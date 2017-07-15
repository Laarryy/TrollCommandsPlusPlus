package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.SpinRegistry;
import me.egg82.tcpp.services.SpinSpeedRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class SpinTickCommand extends TickCommand {
	//vars
	private IRegistry spinRegistry = ServiceLocator.getService(SpinRegistry.class);
	private IRegistry spinSpeedRegistry = ServiceLocator.getService(SpinSpeedRegistry.class);
	
	//constructor
	public SpinTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = spinRegistry.getRegistryNames();
		for (String name : names) {
			e(name, spinRegistry.getRegister(name, Player.class));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		Location newLocation = player.getLocation().clone();
		float newYaw = newLocation.getYaw() + spinSpeedRegistry.getRegister(uuid, Float.class);
		
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
