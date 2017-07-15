package me.egg82.tcpp.events.player.playerMove;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.BrittleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;

public class BrittleEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IRegistry brittleRegistry = ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand(PlayerMoveEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (brittleRegistry.hasRegister(player.getUniqueId().toString())) {
			Location to = event.getTo();
			double toY = to.getY();
			double blockY = (double) to.getBlockY();
			
			if (toY == blockY && toY < event.getFrom().getY()) {
				player.setHealth(0.0d);
				entityUtil.damage(player, EntityDamageEvent.DamageCause.FALL, Double.MAX_VALUE);
			}
		}
	}
}
