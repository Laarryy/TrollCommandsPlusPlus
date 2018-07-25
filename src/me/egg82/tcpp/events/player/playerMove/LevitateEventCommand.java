package me.egg82.tcpp.events.player.playerMove;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.registries.LevitateRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class LevitateEventCommand extends EventHandler<PlayerMoveEvent> {
	//vars
	private IVariableRegistry<UUID> levitateRegistry = ServiceLocator.getService(LevitateRegistry.class);
	
	//constructor
	public LevitateEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		
		if (levitateRegistry.hasRegister(player.getUniqueId())) {
			if (!event.getTo().getWorld().equals(event.getFrom().getWorld())) {
				return;
			}
			
			if (event.getTo().getY() < event.getFrom().getY()) {
				event.setTo(event.getTo().clone().add(0.0d, event.getFrom().getY() - event.getTo().getY(), 0.0d));
			}
		}
	}
}
