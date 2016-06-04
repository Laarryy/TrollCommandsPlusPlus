package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class NauseaTickCommand extends Command {
	//vars
	private IRegistry nauseaRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.NAUSEA_REGISTRY);
	
	//constructor
	public NauseaTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = nauseaRegistry.registryNames();
		for (String name : names) {
			((Player) nauseaRegistry.getRegister(name)).addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 200, 3), true);
		}
	}
}
