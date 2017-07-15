package me.egg82.tcpp.events.entity.entityShootBow;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityShootBowEvent;

import me.egg82.tcpp.services.SnowballFightRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class SnowballFightEventCommand extends EventCommand<EntityShootBowEvent> {
	//vars
	private IRegistry snowballFightRegistry = ServiceLocator.getService(SnowballFightRegistry.class);
	
	//constructor
	public SnowballFightEventCommand(EntityShootBowEvent event) {
		super(event);
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
		String uuid = player.getUniqueId().toString();
		
		if (snowballFightRegistry.hasRegister(uuid)) {
			Entity ent = event.getProjectile();
			Snowball b = event.getEntity().getWorld().spawn(player.getEyeLocation(), Snowball.class);
			
			b.setVelocity(ent.getVelocity());
			b.setShooter(player);
			event.setProjectile(b);
		}
	}
}
