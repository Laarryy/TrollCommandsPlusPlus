package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.SlowpokeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class SlowpokeTickCommand extends TickCommand {
	//vars
	private IRegistry slowpokeRegistry = (IRegistry) ServiceLocator.getService(SlowpokeRegistry.class);
	
	//constructor
	public SlowpokeTickCommand() {
		super();
		ticks = 60L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = slowpokeRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) slowpokeRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 60, 3), true);
	}
}
