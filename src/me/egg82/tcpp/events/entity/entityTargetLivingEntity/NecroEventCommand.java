package me.egg82.tcpp.events.entity.entityTargetLivingEntity;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import me.egg82.tcpp.services.NecroRegistry;
import ninja.egg82.patterns.IRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class NecroEventCommand extends EventCommand<EntityTargetLivingEntityEvent> {
	//vars
	private IRegistry<UUID> necroRegistry = ServiceLocator.getService(NecroRegistry.class);
	
	//constructor
	public NecroEventCommand(EntityTargetLivingEntityEvent event) {
		super(event);
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		if (event.isCancelled()) {
			return;
		}
		
		LivingEntity target = event.getTarget();
		
		if (target == null) {
			return;
		}
		
		if (necroRegistry.hasRegister(target.getUniqueId())) {
			event.setCancelled(true);
		}
	}
}
