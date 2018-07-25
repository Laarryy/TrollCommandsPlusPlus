package me.egg82.tcpp.events.entity.entityDeath;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.registries.StampedeRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.patterns.registries.IVariableRegistry;
import ninja.egg82.plugin.handlers.events.EventHandler;

public class StampedeEventCommand extends EventHandler<EntityDeathEvent> {
	//vars
	private IVariableRegistry<UUID> stampedeRegistry = ServiceLocator.getService(StampedeRegistry.class);
	
	//constructor
	public StampedeEventCommand() {
		super();
	}
	
	//public
	
	//private
	@SuppressWarnings("unchecked")
	protected void onExecute(long elapsedMilliseconds) {
		if (event.getEntityType() != EntityType.COW) {
			return;
		}
		
		UUID uuid = event.getEntity().getUniqueId();
		
		for (UUID key : stampedeRegistry.getKeys()) {
			List<Creature> entities = stampedeRegistry.getRegister(key, List.class);
			Creature removed = null;
			for (Creature e : entities) {
				if (e.getUniqueId().equals(uuid)) {
					removed = e;
					break;
				}
			}
			if (removed != null) {
				if (entities.size() > 1) {
					entities.remove(removed);
				} else {
					stampedeRegistry.removeRegister(key);
				}
				
				break;
			}
		}
	}
}
