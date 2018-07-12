package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Squid;

import me.egg82.tcpp.registries.SquidDeathRegistry;
import me.egg82.tcpp.registries.SquidRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class SquidTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> squidRegistry = ServiceLocator.getService(SquidRegistry.class);
	private IVariableRegistry<UUID> squidDeathRegistry = ServiceLocator.getService(SquidDeathRegistry.class);
	
	//constructor
	public SquidTickCommand() {
		super(0L, 10L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : squidRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		int numSquids = MathUtil.fairRoundedRandom(5, 10);
		Location playerLocation = player.getLocation().clone();
		
		for (int i = 0; i < numSquids; i++) {
			Squid s = player.getWorld().spawn(playerLocation.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Squid.class);
			s.setCustomName("Moist");
			s.setCustomNameVisible(true);
			squidDeathRegistry.setRegister(s.getUniqueId(), s);
		}
	}
}
