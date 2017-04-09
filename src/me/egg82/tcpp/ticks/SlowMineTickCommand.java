package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.services.SlowMineRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class SlowMineTickCommand extends TickCommand {
	//vars
	private IRegistry slowMineRegistry = (IRegistry) ServiceLocator.getService(SlowMineRegistry.class);
	
	//constructor
	public SlowMineTickCommand() {
		super();
		ticks = 60L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = slowMineRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) slowMineRegistry.getRegister(name));
		}
	}
	private void e(String name, Player player){
		if (!player.isOnline()) {
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 3), true);
	}
}
