package me.egg82.tcpp.ticks;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.registries.BombRegistry;
import ninja.egg82.bukkit.handlers.TickHandler;
import ninja.egg82.bukkit.utils.CommandUtil;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.utils.MathUtil;

public class BombTickCommand extends TickHandler {
	//vars
	private IVariableRegistry<UUID> bombRegistry = ServiceLocator.getService(BombRegistry.class);
	
	//constructor
	public BombTickCommand() {
		super(0L, 10L);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		for (UUID key : bombRegistry.getKeys()) {
			e(CommandUtil.getPlayerByUuid(key));
		}
	}
	private void e(Player player) {
		if (player == null) {
			return;
		}
		
		int numFireballs = MathUtil.fairRoundedRandom(5, 10);
		Location playerLocation = player.getLocation().clone();
		
		for (int i = 0; i < numFireballs; i++) {
			Fireball fireball = player.getWorld().spawn(playerLocation.clone().add(MathUtil.random(-10.0d, 10.0d), MathUtil.random(5.0d, 10.0d), MathUtil.random(-10.0d, 10.0d)), Fireball.class);
			Location fireballLocation = fireball.getLocation();
			Vector fireballAngle = new Vector(playerLocation.getX() - fireballLocation.getX(), playerLocation.getY() - fireballLocation.getY(), playerLocation.getZ() - fireballLocation.getZ());
			fireball.setVelocity(fireballAngle.normalize().multiply(2.0d));
		}
	}
}