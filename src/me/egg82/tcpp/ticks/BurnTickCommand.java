package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class BurnTickCommand extends Command {
	//vars
	private IRegistry burnRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BURN_REGISTRY);
	
	//constructor
	public BurnTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = burnRegistry.registryNames();
		for (String name : names) {
			((Player) burnRegistry.getRegister(name)).setFireTicks(60);
		}
	}
}
