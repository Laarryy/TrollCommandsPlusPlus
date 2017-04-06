package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.BurnRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class BurnTickCommand extends TickCommand {
	//vars
	private IRegistry burnRegistry = (IRegistry) ServiceLocator.getService(BurnRegistry.class);
	
	//constructor
	public BurnTickCommand() {
		super();
		ticks = 40L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = burnRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) burnRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.setFireTicks(40);
	}
}
