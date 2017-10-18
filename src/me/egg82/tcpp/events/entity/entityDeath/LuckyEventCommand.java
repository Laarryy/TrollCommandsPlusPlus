package me.egg82.tcpp.events.entity.entityDeath;

import java.util.UUID;

import org.bukkit.event.entity.EntityDeathEvent;

import me.egg82.tcpp.services.registries.LuckyChickenRegistry;
import me.egg82.tcpp.services.registries.LuckyVillagerRegistry;
import ninja.egg82.patterns.IExpiringRegistry;
import ninja.egg82.patterns.ServiceLocator;
import ninja.egg82.plugin.commands.EventCommand;

public class LuckyEventCommand extends EventCommand<EntityDeathEvent> {
	//vars
	private IExpiringRegistry<UUID> luckyChickenRegistry = ServiceLocator.getService(LuckyChickenRegistry.class);
	private IExpiringRegistry<UUID> luckyVillagerRegistry = ServiceLocator.getService(LuckyVillagerRegistry.class);
	
	//constructor
	public LuckyEventCommand() {
		super();
	}
	
	//public
	
	//private
	protected void onExecute(long elapsedMilliseconds) {
		UUID uuid = event.getEntity().getUniqueId();
		
		luckyChickenRegistry.removeRegister(uuid);
		luckyVillagerRegistry.removeRegister(uuid);
	}
}
