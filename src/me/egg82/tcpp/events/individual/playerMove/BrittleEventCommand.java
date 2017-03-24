package me.egg82.tcpp.events.individual.playerMove;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class BrittleEventCommand extends EventCommand {
	//vars
	private IRegistry brittleRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BRITTLE_REGISTRY);
	
	//constrctor
	public BrittleEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("deprecation")
	protected void execute() {
		PlayerMoveEvent e = (PlayerMoveEvent) event;
		
		if (brittleRegistry.contains(e.getPlayer().getUniqueId().toString())) {
			Player player = e.getPlayer();
			Location to = e.getTo();
			double toY = to.getY();
			double blockY = (double) to.getBlockY();
			
			if (toY == blockY && toY < e.getFrom().getY()) {
				player.setHealth(0.0d);
				EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.FALL, Double.MAX_VALUE);
				Bukkit.getPluginManager().callEvent(damageEvent);
				damageEvent.getEntity().setLastDamageCause(damageEvent);
			}
		}
	}
}
