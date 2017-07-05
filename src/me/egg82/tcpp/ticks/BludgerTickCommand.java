package me.egg82.tcpp.ticks;

import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

import me.egg82.tcpp.services.BludgerBallRegistry;
import me.egg82.tcpp.services.BludgerRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;

public class BludgerTickCommand extends TickCommand {
	//vars
	private IRegistry bludgerRegistry = (IRegistry) ServiceLocator.getService(BludgerRegistry.class);
	private IRegistry bludgerBallRegistry = (IRegistry) ServiceLocator.getService(BludgerBallRegistry.class);
	
	//constructor
	public BludgerTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = bludgerRegistry.getRegistryNames();
		for (String name : names) {
			e(name, (Player) bludgerRegistry.getRegister(name));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		Fireball fireball = (Fireball) bludgerBallRegistry.getRegister(uuid);
		fireball.setVelocity(player.getLocation().toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(0.35d));
	}
}