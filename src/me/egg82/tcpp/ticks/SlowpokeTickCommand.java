package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.egg82.patterns.ServiceLocator;
import com.egg82.patterns.command.Command;
import com.egg82.registry.interfaces.IRegistry;

import me.egg82.tcpp.enums.PluginServiceType;

public class SlowpokeTickCommand extends Command {
	//vars
	private IRegistry slowpokeRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOWPOKE_REGISTRY);
	
	//constructor
	public SlowpokeTickCommand() {
		super();
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = slowpokeRegistry.registryNames();
		for (String name : names) {
			slowpoke((Player) slowpokeRegistry.getRegister(name));
		}
	}
	private void slowpoke(Player player) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3), true);
	}
}
