package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class SpinTickCommand extends Command {
	//vars
	private IRegistry spinRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SPIN_REGISTRY);
	
	//constructor
	public SpinTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = spinRegistry.registryNames();
		for (String name : names) {
			spin((Player) spinRegistry.getRegister(name));
		}
	}
	private void spin(Player player) {
		Location loc = player.getLocation();
		loc.setYaw(loc.getYaw() - 11.25f);
		player.teleport(loc);
	}
}
