package me.egg82.tcpp.ticks;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.egg82.tcpp.services.StampedeRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;
import ninja.egg82.plugin.utils.CommandUtil;

public class StampedeTickCommand extends TickCommand {
	//vars
	private IRegistry<UUID> stampedeRegistry = ServiceLocator.getService(StampedeRegistry.class);
	
	private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public StampedeTickCommand() {
		super();
		ticks = 2L;
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : stampedeRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key), stampedeRegistry.getRegister(key, List.class));
		}
	}
	private void e(Player player, List<Creature> entities) {
		if (player == null) {
			return;
		}
		
		for (Creature e : entities) {
			if (!e.getLocation().getWorld().equals(player.getWorld())) {
				continue;
			}
			
			if (e.getLocation().distanceSquared(player.getLocation()) <= 1.0d) {
				entityHelper.damage(e, player, DamageCause.ENTITY_ATTACK, 1.0d);
			}
			
			e.setTarget(player);
			e.teleport(lookTo(e.getLocation(), player.getEyeLocation()));
			e.setVelocity(player.getLocation().toVector().subtract(e.getLocation().toVector()).normalize().multiply(0.23d));
		}
	}
	
	private Location lookTo(Location current, Location to) {
		double dX = current.getX() - to.getX();
		double dY = (current.getY() + 1.0d) - to.getY();
		double dZ = current.getZ() - to.getZ();
		float yaw = (float) (Math.toDegrees(Math.atan2(dZ, dX)) + 90.0d);
		float pitch = (float) (((Math.atan2(fastSqrt(dZ * dZ + dX * dX), dY) / Math.PI) - 0.5d) * -90.0d);
		
		Location retVal = current.clone();
		retVal.setPitch(pitch);
		retVal.setYaw(yaw);
		return retVal;
	}
	private double fastSqrt(double in) {
		// Fast but inaccurate square root
		double retVal = Double.longBitsToDouble(((Double.doubleToLongBits(in) - (1L << 52)) >> 1) + (1L << 61));
		
		// Newton's method for improving accuracy at the cost of speed. 2 iterations will be slower than Math.sqrt()
		// So we only use 1 iteration
		retVal = (retVal + in / retVal) / 2.0d;
		
		return retVal;
	}
}
