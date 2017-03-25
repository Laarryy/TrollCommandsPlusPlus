package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class SpinTickCommand extends TickCommand {
	//vars
	private IRegistry spinRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SPIN_REGISTRY);
	
	//constructor
	public SpinTickCommand() {
		super();
		ticks = 2l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = spinRegistry.registryNames();
		for (String name : names) {
			e((Player) spinRegistry.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		Location loc = player.getLocation();
		loc.setYaw(loc.getYaw() - 11.25f);
		player.teleport(loc);
	}
}
