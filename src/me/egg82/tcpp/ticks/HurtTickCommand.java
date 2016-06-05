package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;

public class HurtTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.HURT_REGISTRY);
	
	//constructor
	public HurtTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = reg.registryNames();
		for (String name : names) {
			e((Player) reg.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		player.damage(1.0d);
	}
}
