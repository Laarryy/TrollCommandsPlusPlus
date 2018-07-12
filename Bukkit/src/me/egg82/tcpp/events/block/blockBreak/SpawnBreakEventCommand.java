package me.egg82.tcpp.events.block.blockBreak;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.block.BlockBreakEvent;

import me.egg82.tcpp.registries.SpawnBreakRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class SpawnBreakEventCommand extends EventHandler<BlockBreakEvent> {
	//vars
	private IVariableRegistry<UUID> spawnBreakRegistry = ServiceLocator.getService(SpawnBreakRegistry.class);
	
	//constructor
	public SpawnBreakEventCommand() {
		super();
	}
	
	//public

	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		
		EntityType type = spawnBreakRegistry.getRegister(uuid, EntityType.class);
		
		if (type != null) {
			Location blockLocation = event.getBlock().getLocation();
			Entity entity = blockLocation.getWorld().spawnEntity(blockLocation, type);
			
			if (entity instanceof PigZombie) {
				((PigZombie) entity).setAngry(true);
			}
			if (entity instanceof Wolf) {
				((Wolf) entity).setAngry(true);
			}
			if (entity instanceof Creature) {
				((Creature) entity).setTarget(player);
			}
		}
	}
}
