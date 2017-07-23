package me.egg82.tcpp.events.entity.entityDamage;

import java.util.UUID;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import me.egg82.tcpp.services.BrittleRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;
import ninja.egg82.plugin.reflection.entity.IEntityHelper;

public class BrittleEventCommand extends EventCommand<EntityDamageEvent> {
	//vars
	private IEntityHelper entityUtil = ServiceLocator.getService(IEntityHelper.class);
	
	private IRegistry<UUID> brittleRegistry = ServiceLocator.getService(BrittleRegistry.class);
	
	//constructor
	public BrittleEventCommand(EntityDamageEvent event) {
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
		
		if (brittleRegistry.hasRegister(player.getUniqueId())) {
			player.setHealth(0.0d);
			entityUtil.damage(player, event.getCause(), Double.MAX_VALUE);
		}
	}
}
