package me.egg82.tcpp.ticks;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.egg82.tcpp.services.BombRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.TickCommand;
import ninja.egg82.utils.MathUtil;

public class BombTickCommand extends TickCommand {
	//vars
	private IRegistry bombRegistry = (IRegistry) ServiceLocator.getService(BombRegistry.class);
	
	//constructor
	public BombTickCommand() {
		super();
		ticks = 10l;
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		String[] names = bombRegistry.getRegistryNames();
		for (String name : names) {
			e((Player) bombRegistry.getRegister(name));
		}
	}
	private void e(Player player) {
		if (!player.isOnline()) {
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