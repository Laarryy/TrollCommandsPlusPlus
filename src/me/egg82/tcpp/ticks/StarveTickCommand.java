package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class StarveTickCommand extends Command {
	//vars
	private IRegistry starveRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.STARVE_REGISTRY);
	
	//constructor
	public StarveTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = starveRegistry.registryNames();
		for (String name : names) {
			starve((Player) starveRegistry.getRegister(name));
		}
	}
	private void starve(Player player) {
		player.setFoodLevel(player.getFoodLevel() - 1);
	}
}
