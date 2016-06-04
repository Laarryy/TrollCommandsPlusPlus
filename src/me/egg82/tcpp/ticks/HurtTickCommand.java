package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class HurtTickCommand extends Command {
	//vars
	private IRegistry hurtRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.HURT_REGISTRY);
	
	//constructor
	public HurtTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = hurtRegistry.registryNames();
		for (String name : names) {
			((Player) hurtRegistry.getRegister(name)).damage(1.0d);
		}
	}
}
