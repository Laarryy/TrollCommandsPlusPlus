package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.command.Command;
import ninja.egg82.registry.interfaces.IRegistry;

public class StarveTickCommand extends Command {
	//vars
	private IRegistry reg = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
	
	//constructor
	public StarveTickCommand() {
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
		
		player.setFoodLevel(player.getFoodLevel() - 1);
	}
}
