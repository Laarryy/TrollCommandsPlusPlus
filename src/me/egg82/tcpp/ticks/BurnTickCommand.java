package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;

public class BurnTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.BURN_REGISTRY);
	
	//constructor
	public BurnTickCommand() {
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
		
		player.setFireTicks(60);
	}
}
