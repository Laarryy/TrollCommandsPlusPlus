package me.egg82.tcpp.commands.unlucky;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.egg82.tcpp.core.LuckyCommand;
import ninja.egg82.bukkit.reflection.entity.IEntityHelper;
import ninja.egg82.patterns.ServiceLocator;

public class KillCommand extends LuckyCommand {
	//vars
	private IEntityHelper entityHelper = ServiceLocator.getService(IEntityHelper.class);
	
	//constructor
	public KillCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		player.setHealth(0.0d);
		entityHelper.damage(player, DamageCause.SUICIDE, Double.MAX_VALUE);
	}
}
