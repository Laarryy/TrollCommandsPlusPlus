package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.services.SpartaArrowRegistry;
import me.egg82.tcpp.services.SpartaRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.utils.CommandUtil;
import ninja.egg82.utils.MathUtil;

public class SpartaTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> spartaRegistry = ServiceLocator.getService(SpartaRegistry.class);
	private IRegistry<UUID> spartaArrowRegistry = ServiceLocator.getService(SpartaArrowRegistry.class);
	
	//constructor
	public SpartaTickCommand() {
		super();
		ticks = 10L;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID[] keys = spartaRegistry.getRegistryKeys();
		for (UUID key : keys) {
			e(key, CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(UUID uuid, Player player) {
		if (player == null || !player.isOnline()) {
			return;
		}
		
		int numArrows = MathUtil.fairRoundedRandom(5, 10);
		Location playerLocation = player.getLocation().clone();
		
		for (int i = 0; i < numArrows; i++) {
			Arrow arrow = player.getWorld().spawn(playerLocation.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Arrow.class);
			spartaArrowRegistry.setRegister(arrow.getUniqueId(), arrow);
			Location arrowLocation = arrow.getLocation();
			Vector arrowAngle = new Vector(playerLocation.getX() - arrowLocation.getX(), playerLocation.getY() - arrowLocation.getY(), playerLocation.getZ() - arrowLocation.getZ());
			arrow.setVelocity(arrowAngle.normalize().multiply(2.0d));
		}
	}
}
