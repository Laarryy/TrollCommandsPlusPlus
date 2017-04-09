package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.WeaklingRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class WeaklingTickCommand extends TickCommand {
	//vars
	private IRegistry weaklingRegistry = (IRegistry) ServiceLocator.getService(WeaklingRegistry.class);
	
	//constructor
	public WeaklingTickCommand() {
		super();
		ticks = 60L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = weaklingRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) weaklingRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, 3), true);
	}
}
