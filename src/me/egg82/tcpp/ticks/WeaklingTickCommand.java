package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class WeaklingTickCommand extends Command {
	//vars
	private IRegistry weaklingRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.WEAKLING_REGISTRY);
	
	//constructor
	public WeaklingTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = weaklingRegistry.registryNames();
		for (String name : names) {
			((Player) weaklingRegistry.getRegister(name)).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 3), true);
		}
	}
}
