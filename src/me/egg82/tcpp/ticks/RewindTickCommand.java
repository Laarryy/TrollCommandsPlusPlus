package me.egg82.tcpp.ticks;

import org.bukkit.entity.Player;

import me.egg82.tcpp.services.RewindRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class RewindTickCommand extends TickCommand {
	//vars
	private IRegistry rewindRegistry = ServiceLocator.getService(RewindRegistry.class);
	
	//constructor
	public RewindTickCommand() {
		super();
		ticks = 5L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = rewindRegistry.getRegistryNames();
		for (String name : names) {
			e(name, rewindRegistry.getRegister(name, Player.class));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		player.setPlayerTime(player.getPlayerTime() - 100L, false);
	}
}
