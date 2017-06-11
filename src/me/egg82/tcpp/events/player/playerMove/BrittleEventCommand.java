package me.egg82.tcpp.events.player.playerMove;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.services.BrittleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityUtil;

public class BrittleEventCommand extends EventCommand {
	//vars
	private IEntityUtil entityUtil = (IEntityUtil) ServiceLocator.getService(IEntityUtil.class);
	
	private IRegistry brittleRegistry = (IRegistry) ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand(Event event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		
		if (e.isCancelled()) {
			return;
		}
		
		Player player = e.getPlayer();
		
		if (brittleRegistry.hasRegister(player.getUniqueId().toString())) {
			Location to = e.getTo();
			double toY = to.getY();
			double blockY = (double) to.getBlockY();
			
			if (toY == blockY && toY < e.getFrom().getY()) {
				player.setHealth(0.0d);
				entityUtil.damage(player, EntityDamageEvent.DamageCause.FALL, Double.MAX_VALUE);
			}
		}
	}
}
