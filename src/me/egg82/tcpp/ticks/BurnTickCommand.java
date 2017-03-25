package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class BurnTickCommand extends TickCommand {
	//vars
	private IRegistry burnRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BURN_REGISTRY);
	
	//constructor
	public BurnTickCommand() {
		super();
		ticks = 60l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = burnRegistry.registryNames();
		for (String name : names) {
			e((Player) burnRegistry.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		player.setFireTicks(60);
	}
}
