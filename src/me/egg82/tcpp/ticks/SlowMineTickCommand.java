package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class SlowMineTickCommand extends TickCommand {
	//vars
	private IRegistry slowMineRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.SLOW_MINE_REGISTRY);
	
	//constructor
	public SlowMineTickCommand() {
		super();
		ticks = 60l;
	}
	
	//public
	
	//private
	protected void execute() {
		String[] names = slowMineRegistry.registryNames();
		for (String name : names) {
			e((Player) slowMineRegistry.getRegister(name));
		}
	}
	private void e(Player player){
		if (player == null) {
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 60, 3), true);
	}
}
