package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.SpinRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.LocationUtil;

public class SpinTickCommand extends TickCommand {
	//vars
	private IRegistry spinRegistry = (IRegistry) ServiceLocator.getService(SpinRegistry.class);
	
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
			e(name, (Player) spinRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		Location newLocation = player.getLocation().clone();
		float newYaw = newLocation.getYaw() - 11.25f;
		
		while (newYaw < 0.0f) {
			newYaw += 360.0f;
		}
		while (newYaw >= 360.0f) {
			newYaw -= 360.0f;
		}
		
		newLocation.setYaw(newYaw);
		player.setVelocity(LocationUtil.moveSmoothly(player.getLocation(), newLocation));
	}
}
