package me.egg82.tcpp.events.entity.entityDeath;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.services.HydraMobRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.utils.BlockUtil;
import ninja.egg82.plugin.utils.LocationUtil;
import ninja.egg82.utils.MathUtil;

public class HydraEventCommand extends EventCommand<EntityDeathEvent> {
	//vars
	private IRegistry<UUID> hydraMobRegistry = ServiceLocator.getService(HydraMobRegistry.class);
	
	//constructor
	public HydraEventCommand(EntityDeathEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		Entity entity = event.getEntity();
		UUID uuid = entity.getUniqueId();
		
		if (hydraMobRegistry.hasRegister(uuid)) {
			Location[] mobLocations = LocationUtil.getCircleAround(entity.getLocation(), MathUtil.random(2.0d,  3.0d), MathUtil.fairRoundedRandom(2, 4));
			
			for (int i = 0; i < mobLocations.length; i++) {
				Location creatureLocation = BlockUtil.getTopWalkableBlock(mobLocations[i]);
				
				Entity e = entity.getWorld().spawn(creatureLocation, event.getEntityType().getEntityClass());
				if (e instanceof PigZombie) {
					((PigZombie) e).setAngry(true);
				}
				if (e instanceof Monster) {
					((Monster) e).setTarget(event.getEntity().getKiller());
				}
				
				hydraMobRegistry.setRegister(e.getUniqueId(), uuid);
			}
		}
	}
}
