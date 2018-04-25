package me.egg82.tcpp.events.player.playerMove;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.registries.BrittleRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;

public class BrittleEventCommand extends EventCommand<PlayerMoveEvent> {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IVariableRegistry<UUID> brittleRegistry = ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (brittleRegistry.hasRegister(player.getUniqueId())) {
			if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
				return;
			}
			
			Location to = event.getTo();
			double toY = to.getY();
			double blockY = to.getBlockY();
			
			if (toY == blockY && toY < event.getFrom().getY()) {
				player.setHealth(0.0d);
				entityUtil.damage(player, EntityDamageEvent.DamageCause.FALL, Double.MAX_VALUE);
			}
		}
	}
}
