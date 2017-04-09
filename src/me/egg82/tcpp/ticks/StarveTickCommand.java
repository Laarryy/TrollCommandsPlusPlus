package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.StarveRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class StarveTickCommand extends TickCommand {
	//vars
	private IRegistry starveRegistry = (IRegistry) ServiceLocator.getService(StarveRegistry.class);
	
	//constructor
	public StarveTickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = starveRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) starveRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.setFoodLevel(player.getFoodLevel() - 1);
	}
}
