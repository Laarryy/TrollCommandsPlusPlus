package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.RandomSpeedRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;

public class RandomSpeedTickCommand extends TickCommand {
	//vars
	private IRegistry randomSpeedRegistry = (IRegistry) ServiceLocator.getService(RandomSpeedRegistry.class);
	
	//constructor
	public RandomSpeedTickCommand() {
		super();
		ticks = 55L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = randomSpeedRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) randomSpeedRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		if (Math.random() <= 0.15d) {
			player.setWalkSpeed((float) MathUtil.random(0.05d, 1.0d));
			player.setFlySpeed((float) MathUtil.random(0.05d, 1.0d));
		}
	}
}