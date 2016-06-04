package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class SlowMineTickCommand extends Command {
	//vars
	private IRegistry slowmineRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOWMINE_REGISTRY);
	
	//constructor
	public SlowMineTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = slowmineRegistry.registryNames();
		for (String name : names) {
			((Player) slowmineRegistry.getRegister(name)).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 3), true);
		}
	}
}
