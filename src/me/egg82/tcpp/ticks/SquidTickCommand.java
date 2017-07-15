package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;

import me.egg82.tcpp.services.SquidDeathRegistry;
import me.egg82.tcpp.services.SquidRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;

public class SquidTickCommand extends TickCommand {
	//vars
	private IRegistry squidRegistry = ServiceLocator.getService(SquidRegistry.class);
	private IRegistry squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	
	//constructor
	public SquidTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = squidRegistry.getRegistryNames();
		for (String name : names) {
			e(name, squidRegistry.getRegister(name, Player.class));
		}
	}
	private void e(String uuid, Player player) {
		if (!player.isOnline()) {
			return;
		}
		
		int numSquids = MathUtil.fairRoundedRandom(5, 10);
		Location playerLocation = player.getLocation().clone();
		
		for (int i = 0; i < numSquids; i++) {
			Squid s = player.getWorld().spawn(playerLocation.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Squid.class);
			squidDeathRegistry.setRegister(s.getUniqueId().toString(), Squid.class, s);
		}
	}
}
