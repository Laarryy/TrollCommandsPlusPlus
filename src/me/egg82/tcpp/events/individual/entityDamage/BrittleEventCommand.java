package me.egg82.tcpp.events.individual.entityDamage;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.enums.PluginServiceType;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.registry.interfaces.IRegistry;

public class BrittleEventCommand extends EventCommand {
	//vars
	private IRegistry brittleRegistry = (IRegistry) ServiceLocator.getService(PluginServiceType.BRITTLE_REGISTRY);
	
	//constructor
	public BrittleEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("deprecation")
	protected void execute() {
		EntityDamageEvent e = (EntityDamageEvent) event;
		
		if (e.getEntityType() == EntityType.PLAYER) {
			Player player = (Player) e.getEntity();
			
			brittleRegistry.computeIfPresent(player.getUniqueId().toString(), (k,v) -> {
				player.setHealth(0.0d);
				EntityDamageEvent damageEvent = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.FALL, Double.MAX_VALUE);
				Bukkit.getPluginManager().callEvent(damageEvent);
				damageEvent.getEntity().setLastDamageCause(damageEvent);
				
				return v;
			});
		}
	}
}
