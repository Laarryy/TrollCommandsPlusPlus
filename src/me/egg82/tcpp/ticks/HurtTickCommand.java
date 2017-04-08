package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.HurtRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class HurtTickCommand extends TickCommand {
	//vars
	private IRegistry hurtRegistry = (IRegistry) ServiceLocator.getService(HurtRegistry.class);
	
	//constructor
	public HurtTickCommand() {
		super();
		ticks = 15L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = hurtRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) hurtRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.damage(1.0d);
	}
}
