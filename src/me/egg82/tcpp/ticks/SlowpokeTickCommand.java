package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class SlowpokeTickCommand extends TickCommand {
	//vars
	private IRegistry slowpokeRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOWPOKE_REGISTRY);
	
	//constructor
	public SlowpokeTickCommand() {
		super();
		ticks = 60l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = slowpokeRegistry.registryNames();
		for (String name : names) {
			e((Player) slowpokeRegistry.getRegister(name));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3), true);
	}
}
