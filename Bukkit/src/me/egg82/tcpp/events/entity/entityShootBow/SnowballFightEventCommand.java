package me.egg82.tcpp.events.entity.entityShootBow;

import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.egg82.tcpp.services.registries.SnowballFightRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.commands.events.EventCommand;

public class SnowballFightEventCommand extends EventCommand<EntityShootBowEvent> {
	//vars
	private IVariableRegistry<UUID> snowballFightRegistry = ServiceLocator.getService(SnowballFightRegistry.class);
	
	//constructor
	public SnowballFightEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		if (event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		
		Player player = (Player) event.getEntity();
		UUID uuid = player.getUniqueId();
		
		if (snowballFightRegistry.hasRegister(uuid)) {
			Entity ent = event.getProjectile();
			Snowball b = event.getEntity().getWorld().spawn(player.getEyeLocation(), Snowball.class);
			
			b.setVelocity(ent.getVelocity());
			b.setShooter(player);
			event.setProjectile(b);
		}
	}
}
