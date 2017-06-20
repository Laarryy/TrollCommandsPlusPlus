package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.services.LevitateRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class LevitateTickCommand extends TickCommand {
	//vars
	private IRegistry levitateRegistry = (IRegistry) ServiceLocator.getService(LevitateRegistry.class);
	
	//constructor
	public LevitateTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = levitateRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) levitateRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.setVelocity(new Vector(0.0d, 0.1d, 0.0d));
	}
}
